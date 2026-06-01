package mod.pyrate.bigtools.blockentity;

import mod.pyrate.bigtools.init.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BooleanSupplier;

public class DiamondHopperBlockEntity extends HopperBlockEntity
{
    private BlockEntityType<@NotNull DiamondHopperBlockEntity> type;

    private static final double ORB_RANGE = 1.5f;
    private static final double PLAYER_RANGE = 4.0f;
    public static final int MOVE_ITEM_SPEED = 10;
    public static final int HOPPER_CONTAINER_SIZE = 5;
    private static final int[][] CACHED_SLOTS = new int[54][];
    private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
    private int cooldownTime = -1;
    private long tickedGameTime;
    private Direction facing;

    public DiamondHopperBlockEntity(BlockPos pPos, BlockState pBlockState)
    {
        super(pPos, pBlockState);
        this.facing = pBlockState.getValue(HopperBlock.FACING);
        this.type = ModBlocks.DIAMOND_HOPPER_ENTITY.get();
    }

    @Override
    public @NotNull BlockEntityType<?> getType()
    {
        this.type = ModBlocks.DIAMOND_HOPPER_ENTITY.get();
        return this.type;
    }

    //@Override
    //protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries)
    //{
    //    super.loadAdditional(pTag, pRegistries);
    //    this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    //    if (!this.tryLoadLootTable(pTag))
    //    {
    //        ContainerHelper.loadAllItems(pTag, this.items, pRegistries);
    //    }
    //    this.cooldownTime = pTag.getInt("TransferCooldown");
    //}

    //@Override
    //protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries)
    //{
    //    super.saveAdditional(pTag, pRegistries);
    //    if (!this.trySaveLootTable(pTag))
    //    {
    //        ContainerHelper.saveAllItems(pTag, this.items, pRegistries);
    //    }
    //    pTag.putInt("TransferCooldown", this.cooldownTime);
    //}

    @Override
    public int getContainerSize()
    {
        return this.items.size();
    }

    private static Vec3 UnitVector(Vec3 a, Vec3 b)
    {
        Vec3 c = new Vec3(a.x - b.x, a.y - b.y, a.z - b.z);
        return c.normalize();
    }

    private static Vec3 UnitVector(Vec3 a, Vec3 b, double scaleFactor)
    {
        Vec3 c = UnitVector(a, b);
        return new Vec3(c.x * scaleFactor, c.y * scaleFactor, c.z * scaleFactor);
    }

    private static boolean isItemGrindable(ItemStack pItem)
    {
        //BigTools.LOGGER.debug("DiamondHopperBlockEntity::isItemGrindable()");
        if (pItem.is(Items.BOOK))
        {
            return pItem.has(DataComponents.STORED_ENCHANTMENTS);
        }
        else
        {
            return pItem.has(DataComponents.ENCHANTMENTS);
        }
    }

    public ItemStack removeItem(int pIndex, int pCount, boolean grind)
    {
        //BigTools.LOGGER.debug("DiamondHopperBlockEntity::removeItem()");
        this.unpackLootTable(null);
        ItemStack beingRemoved = ContainerHelper.removeItem(this.getItems(), pIndex, pCount);
        if (grind)
        {
            beingRemoved = tryGrindItem(beingRemoved);
        }

        return beingRemoved;
    }

    private ItemStack tryGrindItem(ItemStack beingRemoved)
    {
        //BigTools.LOGGER.debug("DiamondHopperBlockEntity::tryGrindItem()");
        if (this.getLevel() != null)
        {
            if (!this.getLevel().isClientSide())
            {
                if (beingRemoved.getMaxStackSize() == 1)
                {
                    // Only bother if it's a non-stackable item
                    if (isItemGrindable(beingRemoved))
                    {
                        // Only bother if it's enchanted
                        if (level instanceof ServerLevel sLevel)
                        {
                            int xp = getExperienceAmount(sLevel, beingRemoved);
                            Vec3 hopLoc = new Vec3(this.getLevelX(),this.getLevelY(),this.getLevelZ());

                            Player nearestPlayer = sLevel.getNearestPlayer(this.getLevelX(),
                                                                           this.getLevelY(),
                                                                           this.getLevelZ(),
                                                                           PLAYER_RANGE, false);
                            Vec3 orbLoc = hopLoc;
                            if (nearestPlayer != null)
                            {
                                Vec3 playerLoc = new Vec3(nearestPlayer.getX(), nearestPlayer.getY(), nearestPlayer.getZ());
                                orbLoc = hopLoc.add(UnitVector(playerLoc, hopLoc, ORB_RANGE));

                            }
                            else
                            {
                                switch (this.facing)
                                {
                                    case Direction.DOWN -> orbLoc = hopLoc.add(0, 0, 0.25);
                                    case Direction.UP -> orbLoc = hopLoc.add(0, 0, 0.25);
                                    case Direction.NORTH-> orbLoc = hopLoc.add(0, 0, -0.25);
                                    case Direction.SOUTH-> orbLoc = hopLoc.add(0, 0, 0.25);
                                    case Direction.EAST-> orbLoc = hopLoc.add(0.25, 0, 0);
                                    case Direction.WEST-> orbLoc = hopLoc.add(-0.25, 0, 0);
                                }
                            }
                            ExperienceOrb.award(sLevel, orbLoc, xp);
                        }

                        beingRemoved = removeNonCursesFrom(beingRemoved);
                        beingRemoved.setCount(0);
                        beingRemoved = ItemStack.EMPTY;
                    }
                }
            }
        }
        //if (beingRemoved.is(Items.BOOK))
        //{
        //    return beingRemoved;
        //}
        return beingRemoved;
    }

    private int getExperienceAmount(Level level, ItemStack item)
    {
        int itemXP = this.getExperienceFromItem(item);
        if (itemXP > 0)
        {
            int minXP = (int)Math.ceil((double)itemXP / 2.0);
            return minXP + level.getRandom().nextInt(minXP);
        }
        else
        {
            return 0;
        }
    }
    private int getExperienceFromItem(ItemStack p_39637_)
    {
        int itemXP = 0;
        ItemEnchantments itemenchantments = EnchantmentHelper.getEnchantmentsForCrafting(p_39637_);

        for (Entry<Holder<Enchantment>> entry : itemenchantments.entrySet())
        {
            Holder<Enchantment> holder = entry.getKey();
            int i1 = entry.getIntValue();
            if (!holder.is(EnchantmentTags.CURSE))
            {
                itemXP += holder.value().getMinCost(i1);
            }
        }

        return itemXP;
    }
    private ItemStack removeNonCursesFrom(ItemStack pItem)
    {
        ItemEnchantments itemenchantments = EnchantmentHelper.updateEnchantments(
                pItem, p_330066_ -> p_330066_.removeIf(p_344368_ -> !p_344368_.is(EnchantmentTags.CURSE))
        );
        if (pItem.is(Items.ENCHANTED_BOOK) && itemenchantments.isEmpty())
        {
            pItem = pItem.transmuteCopy(Items.BOOK);
        }

        int i = 0;

        for (int j = 0; j < itemenchantments.size(); j++)
        {
            i = AnvilMenu.calculateIncreasedRepairCost(i);
        }


        if (pItem.is(Items.BOOK))
        {
            if (pItem.has(DataComponents.STORED_ENCHANTMENTS))
            {
                pItem.remove(DataComponents.STORED_ENCHANTMENTS);
            }
        }
        else
        {
            if (pItem.has(DataComponents.ENCHANTMENTS))
            {
                pItem.remove(DataComponents.ENCHANTMENTS);
            }
        }
        pItem.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);

        pItem.set(DataComponents.REPAIR_COST, i);
        return pItem;
    }

    @Override
    public void setItem(int pIndex, @NotNull ItemStack pStack)
    {
        this.unpackLootTable(null);
        this.getItems().set(pIndex, pStack);
        pStack.limitSize(this.getMaxStackSize(pStack));
    }

    @Override
    public void setBlockState(@NotNull BlockState pBlockState)
    {
        super.setBlockState(pBlockState);
        this.facing = pBlockState.getValue(HopperBlock.FACING);
    }

    @Override
    protected @NotNull Component getDefaultName()
    {
        return Component.literal("Diamond Grinder");
    }

    public static void pushItemsTickD(Level pLevel, BlockPos pPos, BlockState pState, DiamondHopperBlockEntity pBlockEntity)
    {
        pBlockEntity.cooldownTime--;
        pBlockEntity.tickedGameTime = pLevel.getGameTime();
        if (!pBlockEntity.isOnCooldown())
        {
            pBlockEntity.setCooldown(0);
            tryMoveItems(pLevel, pPos, pState, pBlockEntity, () -> suckInItems(pLevel, pBlockEntity));
        }
    }

    private static void tryMoveItems(Level pLevel, BlockPos pPos, BlockState pState, DiamondHopperBlockEntity pBlockEntity, BooleanSupplier pValidator)
    {
        if (!pLevel.isClientSide())
        {
            if (!pBlockEntity.isOnCooldown() && pState.getValue(HopperBlock.ENABLED))
            {
                boolean flag = false;
                if (!pBlockEntity.isEmpty())
                {
                    flag = ejectItems(pLevel, pPos, pBlockEntity);
                }

                if (!pBlockEntity.inventoryFull())
                {
                    flag |= pValidator.getAsBoolean();
                }

                if (flag)
                {
                    pBlockEntity.setCooldown(MOVE_ITEM_SPEED);
                    setChanged(pLevel, pPos, pState);
                }
            }

        }
    }

    private boolean inventoryFull()
    {
        for (ItemStack itemstack : this.items)
        {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
            {
                return false;
            }
        }

        return true;
    }

    private static boolean ejectItems(Level pLevel, BlockPos pPos, DiamondHopperBlockEntity pBlockEntity)
    {
        //if (net.neoforged.neoforge.items.VanillaInventoryCodeHooks.insertHook(pBlockEntity)) return true;
        Container container = getAttachedContainer(pLevel, pPos, (DiamondHopperBlockEntity)pBlockEntity);
        if (container == null)
        {
            return false;
        }
        else
        {
            //BigTools.LOGGER.debug("DiamondHopperBlockEntity::ejectItems()");
            Direction direction = ((DiamondHopperBlockEntity)pBlockEntity).facing.getOpposite();
            if (isFullContainer(container, direction))
            {
                return false;
            }
            else
            {
                for (int i = 0; i < pBlockEntity.getContainerSize(); i++)
                {
                    ItemStack itemstack = pBlockEntity.getItem(i);
                    if (!itemstack.isEmpty())
                    {
                        int j = itemstack.getCount();
                        ItemStack beingRemoved = pBlockEntity.removeItem(i, 1, true);
                        if (beingRemoved == null)
                        {
                            return true;
                        }
                        if (beingRemoved.isEmpty())
                        {
                            return true;
                        }
                        ItemStack itemstack1 = addItem(pBlockEntity, container, beingRemoved, direction);
                        if (itemstack1.isEmpty())
                        {
                            container.setChanged();
                            return true;
                        }

                        itemstack.setCount(j);
                        if (j == 1)
                        {
                            pBlockEntity.setItem(i, itemstack);
                        }
                    }
                }

                return false;
            }
        }
    }

    private static int[] getSlots(Container pContainer, Direction pDirection)
    {
        if (pContainer instanceof WorldlyContainer worldlycontainer)
        {
            return worldlycontainer.getSlotsForFace(pDirection);
        }
        else
        {
            int i = pContainer.getContainerSize();
            if (i < CACHED_SLOTS.length) {
                int[] aint = CACHED_SLOTS[i];
                if (aint != null)
                {
                    return aint;
                }
                else
                {
                    int[] aint1 = createFlatSlots(i);
                    CACHED_SLOTS[i] = aint1;
                    return aint1;
                }
            }
            else
            {
                return createFlatSlots(i);
            }
        }
    }

    private static int[] createFlatSlots(int pSize)
    {
        int[] aint = new int[pSize];
        int i = 0;

        while (i < aint.length)
        {
            aint[i] = i++;
        }

        return aint;
    }

    /**
     * @return {@code false} if the {@code container} has any room to place items in
     */
    private static boolean isFullContainer(Container pContainer, Direction pDirection)
    {
        int[] aint = getSlots(pContainer, pDirection);

        for (int i : aint)
        {
            ItemStack itemstack = pContainer.getItem(i);
            if (itemstack.getCount() < itemstack.getMaxStackSize())
            {
                return false;
            }
        }

        return true;
    }

    public static boolean suckInItems(Level pLevel, DiamondHopperBlockEntity pHopper)
    {
        BlockPos blockpos = BlockPos.containing(pHopper.getLevelX(), pHopper.getLevelY() + 1.0, pHopper.getLevelZ());
        BlockState blockstate = pLevel.getBlockState(blockpos);
        //Boolean ret = net.neoforged.neoforge.items.VanillaInventoryCodeHooks.extractHook(pLevel, pHopper);
        //if (ret != null) return ret;
        Container container = getSourceContainer(pLevel, pHopper, blockpos, blockstate);
        if (container != null)
        {
            //BigTools.LOGGER.debug("DiamondHopperBlockEntity::suckInItems()");
            Direction direction = Direction.DOWN;

            for (int i : getSlots(container, direction))
            {
                if (tryTakeInItemFromSlot(pHopper, container, i, direction))
                {
                    return true;
                }
            }
            for (int idx = 0; idx < HOPPER_CONTAINER_SIZE; idx++)
            {
                ItemStack is = pHopper.getItem(idx);
                // If there is an item...
                if (!is.isEmpty())
                {
                    // Try to grind the item
                    ItemStack plain = pHopper.tryGrindItem(is);

                    //// "Destroy" the item
                    //pHopper.setItem(idx, ItemStack.EMPTY);

                    // "Replace" the item
                    pHopper.setItem(idx, plain);
                }
            }
            return false;
        }
        else
        {
            boolean flag = pHopper.isGridAligned()
                    && blockstate.isCollisionShapeFullBlock(pLevel, blockpos)
                    && !blockstate.is(BlockTags.DOES_NOT_BLOCK_HOPPERS);
            if (!flag)
            {
                for (ItemEntity itementity : getItemsAtAndAbove(pLevel, pHopper))
                {
                    if (addItem(pHopper, itementity))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Pulls from the specified slot in the container and places in any available slot in the hopper.
     * @return {@code true} if the entire stack was moved.
     */
    private static boolean tryTakeInItemFromSlot(DiamondHopperBlockEntity pHopper, Container pContainer, int pSlot, Direction pDirection)
    {
        ItemStack itemstack = pContainer.getItem(pSlot);
        if (!itemstack.isEmpty() && canTakeItemFromContainer(pHopper, pContainer, itemstack, pSlot, pDirection))
        {
            int i = itemstack.getCount();
            ItemStack itemstack1 = addItem(pContainer, pHopper, pContainer.removeItem(pSlot, 1), null);
            if (itemstack1.isEmpty())
            {
                pContainer.setChanged();
                return true;
            }

            itemstack.setCount(i);
            if (i == 1)
            {
                pContainer.setItem(pSlot, itemstack);
            }
        }

        return false;
    }

    public static boolean addItem(Container pContainer, ItemEntity pItem)
    {
        boolean flag = false;
        ItemStack itemstack = pItem.getItem().copy();
        ItemStack itemstack1 = addItem(null, pContainer, itemstack, null);
        if (itemstack1.isEmpty())
        {
            flag = true;
            pItem.setItem(ItemStack.EMPTY);
            pItem.discard();
        }
        else
        {
            pItem.setItem(itemstack1);
        }

        return flag;
    }

    /**
     * Attempts to place the passed stack in the container, using as many slots as required.
     * @return any leftover stack
     */
    public static @NotNull ItemStack addItem(@Nullable Container pSource, Container pDestination, @NotNull ItemStack pStack, @Nullable Direction pDirection)
    {
        if (pDestination instanceof WorldlyContainer worldlycontainer && pDirection != null)
        {
            int[] aint = worldlycontainer.getSlotsForFace(pDirection);

            for (int k = 0; k < aint.length && !pStack.isEmpty(); k++)
            {
                pStack = tryMoveInItem(pSource, pDestination, pStack, aint[k], pDirection);
            }

            return pStack;
        }

        int i = pDestination.getContainerSize();

        for (int j = 0; j < i && !pStack.isEmpty(); j++)
        {
            pStack = tryMoveInItem(pSource, pDestination, pStack, j, pDirection);
        }

        return pStack;
    }

    private static boolean canPlaceItemInContainer(Container pContainer, ItemStack pStack, int pSlot, @Nullable Direction pDirection)
    {
        if (!pContainer.canPlaceItem(pSlot, pStack))
        {
            return false;
        }
        else
        {
            return !(pContainer instanceof WorldlyContainer worldlycontainer) || worldlycontainer.canPlaceItemThroughFace(pSlot, pStack, pDirection);
        }
    }

    private static boolean canTakeItemFromContainer(Container pSource, Container pDestination, ItemStack pStack, int pSlot, Direction pDirection)
    {
        if (!pDestination.canTakeItem(pSource, pSlot, pStack))
        {
            return false;
        }
        else
        {
            return !(pDestination instanceof WorldlyContainer worldlycontainer) || worldlycontainer.canTakeItemThroughFace(pSlot, pStack, pDirection);
        }
    }

    private static ItemStack tryMoveInItem(@Nullable Container pSource, Container pDestination, ItemStack pStack, int pSlot, @Nullable Direction pDirection)
    {
        ItemStack itemstack = pDestination.getItem(pSlot);
        if (canPlaceItemInContainer(pDestination, pStack, pSlot, pDirection))
        {
            boolean flag = false;
            boolean flag1 = pDestination.isEmpty();
            if (itemstack.isEmpty())
            {
                if (pDestination instanceof DiamondHopperBlockEntity pDHop)
                {
                    pDestination.setItem(pSlot, pDHop.tryGrindItem(pStack));
                }
                else
                {
                    pDestination.setItem(pSlot, pStack);
                }
                pStack = ItemStack.EMPTY;
                flag = true;
            }
            else if (canMergeItems(itemstack, pStack))
            {
                int i = pStack.getMaxStackSize() - itemstack.getCount();
                int j = Math.min(pStack.getCount(), i);
                pStack.shrink(j);
                itemstack.grow(j);
                flag = j > 0;
            }

            if (flag)
            {
                if (flag1 && pDestination instanceof DiamondHopperBlockEntity hopperblockentity1 && !hopperblockentity1.isOnCustomCooldown())
                {
                    int k = 0;
                    if (pSource instanceof DiamondHopperBlockEntity hopperblockentity && hopperblockentity1.tickedGameTime >= hopperblockentity.tickedGameTime)
                    {
                        k = 1;
                    }

                    hopperblockentity1.setCooldown(MOVE_ITEM_SPEED - k);
                }

                pDestination.setChanged();
            }
        }

        return pStack;
    }

    @Nullable
    private static Container getAttachedContainer(Level pLevel, BlockPos pPos, DiamondHopperBlockEntity pBlockEntity)
    {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof Container)
        {
            return (Container) blockEntity;
        }
        return null;
    }

    @Nullable
    private static Container getSourceContainer(Level pLevel, DiamondHopperBlockEntity pHopper, BlockPos pPos, BlockState pState)
    {
        return getContainerAt(pLevel, pPos, pState, pHopper.getLevelX(), pHopper.getLevelY() + 1.0, pHopper.getLevelZ());
    }

    public static List<ItemEntity> getItemsAtAndAbove(Level pLevel, DiamondHopperBlockEntity pHopper)
    {
        AABB aabb = pHopper.getSuckAabb().move(pHopper.getLevelX() - 0.5, pHopper.getLevelY() - 0.5, pHopper.getLevelZ() - 0.5);
        return pLevel.getEntitiesOfClass(ItemEntity.class, aabb, EntitySelector.ENTITY_STILL_ALIVE);
    }

    @Nullable
    private static Container getContainerAt(Level pLevel, BlockPos pPos, BlockState pState, double pX, double pY, double pZ)
    {
        Container container = getBlockContainer(pLevel, pPos, pState);
        if (container == null)
        {
            container = getEntityContainer(pLevel, pX, pY, pZ);
        }

        return container;
    }

    @Nullable
    private static Container getBlockContainer(Level pLevel, BlockPos pPos, BlockState pState)
    {
        Block block = pState.getBlock();
        if (block instanceof WorldlyContainerHolder)
        {
            return ((WorldlyContainerHolder)block).getContainer(pState, pLevel, pPos);
        }
        else if (pState.hasBlockEntity() && pLevel.getBlockEntity(pPos) instanceof Container container)
        {
            if (container instanceof ChestBlockEntity && block instanceof ChestBlock)
            {
                container = ChestBlock.getContainer((ChestBlock)block, pState, pLevel, pPos, true);
            }

            return container;
        }
        else
        {
            return null;
        }
    }

    @Nullable
    private static Container getEntityContainer(Level pLevel, double pX, double pY, double pZ)
    {
        List<Entity> list = pLevel.getEntities((Entity)null, new AABB(pX - 0.5, pY - 0.5, pZ - 0.5, pX + 0.5, pY + 0.5, pZ + 0.5), EntitySelector.CONTAINER_ENTITY_SELECTOR);
        return !list.isEmpty() ? (Container)list.get(pLevel.getRandom().nextInt(list.size())) : null;
    }

    public static void entityInside(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity, DiamondHopperBlockEntity pBlockEntity)
    {
        if (pEntity instanceof ItemEntity itementity
                && !itementity.getItem().isEmpty()
                && pEntity.getBoundingBox()
                .move((double)(-pPos.getX()), (double)(-pPos.getY()), (double)(-pPos.getZ()))
                .intersects(pBlockEntity.getSuckAabb()))
        {
            tryMoveItems(pLevel, pPos, pState, pBlockEntity, () -> addItem(pBlockEntity, itementity));
        }
    }
    private static boolean canMergeItems(ItemStack pStack1, ItemStack pStack2) { return pStack1.getCount() <= pStack1.getMaxStackSize() && ItemStack.isSameItemSameComponents(pStack1, pStack2); }
    @Override
    public double getLevelX() { return (double)this.worldPosition.getX() + 0.5; }
    @Override
    public double getLevelY() { return (double)this.worldPosition.getY() + 0.5; }
    @Override
    public double getLevelZ() { return (double)this.worldPosition.getZ() + 0.5; }
    //@Override
    //public boolean isGridAligned() { return true; }
    public void setCooldown(int pCooldownTime) { this.cooldownTime = pCooldownTime; }
    private boolean isOnCooldown() { return this.cooldownTime > 0; }
    public boolean isOnCustomCooldown() { return this.cooldownTime > MOVE_ITEM_SPEED; }
    @Override
    protected @NotNull NonNullList<ItemStack> getItems() { return this.items; }
    @Override
    protected void setItems(@NotNull NonNullList<ItemStack> pItems) { this.items = pItems; }
    @Override
    protected @NotNull AbstractContainerMenu createMenu(int pId, @NotNull Inventory pPlayer) { return new HopperMenu(pId, pPlayer, this); }
    public long getLastUpdateTime() { return this.tickedGameTime; }

}
