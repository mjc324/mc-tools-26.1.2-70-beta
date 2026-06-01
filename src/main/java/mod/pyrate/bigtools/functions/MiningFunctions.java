package mod.pyrate.bigtools.functions;

import mod.pyrate.bigtools.init.ModMiningToolItems;
import mod.pyrate.bigtools.items.tools.ShearItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class MiningFunctions
{
    private static final int MAX_LOG_SHEAR_BREAK = 400;

    public static void doToolMine3x3(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        int damageCount = 0;
        if (entity instanceof Player)
            damageCount = attemptBreakNeighbors(stack, (ServerLevel) world, pos, (Player) entity, true);
        doCustomDamage(stack, damageCount, entity);
    }
    public static void doToolMine11x11(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        int damageCount = 0;
        if (entity instanceof Player)
        {
            damageCount += attemptBreakRegion(stack, (ServerLevel) world, pos, pos, (Player) entity,
                    -5, 5, 0, 0, 0, 10, true);
        }
        doCustomDamage(stack, damageCount, entity);
    }
    public static void doToolDrillBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        int damageCount = 0;
        if (entity instanceof Player)
        {
            List<BlockPos> borderBlocks = new ArrayList<BlockPos>();
            List<BlockPos> innerBlocks = new ArrayList<BlockPos>();
            damageCount = attemptDrillNeighbors(stack, (ServerLevel) world, pos, pos, (Player) entity, 1, 3, 3, null, borderBlocks, innerBlocks, null);
            innerBlocks.addAll(borderBlocks);
            fillBorerWalls(stack, (ServerLevel) world, pos, (Player) entity, null, innerBlocks, null);
        }
        doCustomDamage(stack, damageCount, entity);
    }
    public static void doToolBorerBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity, BlockState wallBlock)
    {
        int damageCount = 0;
        if (entity instanceof Player)
        {
            List<BlockPos> borderBlocks = new ArrayList<BlockPos>();
            List<BlockPos> innerBlocks = new ArrayList<BlockPos>();
            damageCount = attemptDrillNeighbors(stack, (ServerLevel) world, pos, pos, (Player) entity, 2, 4, 4, null, borderBlocks, innerBlocks, wallBlock);
            fillBorerWalls(stack, (ServerLevel) world, pos, (Player) entity, borderBlocks, innerBlocks, wallBlock);
        }
        doCustomDamage(stack, damageCount, entity);
    }
    public static void doToolLogBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        int damageCount = 0;
        if (entity instanceof Player)
            damageCount = attemptLogNeighbors(stack, (ServerLevel) world, pos, pos, (Player) entity, true);
        doCustomDamage(stack, damageCount, entity);
    }
    public static void doToolShearBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        int damageCount = 0;
        if (entity instanceof Player)
            damageCount = attemptShearNeighbors(stack, (ServerLevel) world, pos, pos, (Player) entity);
        doCustomDamage(stack, damageCount, entity);
    }

    private static boolean isLog(BlockState state)
    {
        return state.is(BlockTags.LOGS);
    }

    private static boolean isCustomMember(Item itm, Set<Supplier<Item>> customItems)
    {
        for (Supplier<Item> reg : customItems)
        {
            if (reg.get().toString().equals(itm.toString()))
            {
                return true;
            }
        }
        return false;
    }
    private static boolean isToolEffective(ItemStack stack, BlockState state)
    {
        // All tools are effective on powder snow!
        boolean isEffective = state.is(Blocks.POWDER_SNOW);

        Item itm = stack.getItem();
        if (!isEffective)
        {
            if (state.is(BlockTags.MINEABLE_WITH_PICKAXE))
            {
                // Drills and Excavators are included as Pickaxes
                isEffective |= isCustomMember(itm, ModMiningToolItems.CUSTOM_PICKAXES);
            }
        }
        if (!isEffective)
        {
            if (state.is(BlockTags.MINEABLE_WITH_SHOVEL))
            {
                // Drills and Excavators are included as Shovels
                isEffective |= isCustomMember(itm, ModMiningToolItems.CUSTOM_SHOVELS);
            }
        }
        if (!isEffective)
        {
            if (state.is(BlockTags.MINEABLE_WITH_AXE))
            {
                isEffective |= isCustomMember(itm, ModMiningToolItems.CUSTOM_AXES);
            }
        }
        if (!isEffective)
        {
            if (isCustomMember(itm, ModMiningToolItems.CUSTOM_SHEARS))
            {
                // Shears need to handle what's listed in ShearItem.EFFECTIVE_ON or ShearItem.EFFECTIVE_MATERIALS
                isEffective |= ShearItem.EFFECTIVE_ON.contains(state.getBlock()); //|| ShearItem.EFFECTIVE_MATERIALS.contains(state.getMaterial());
            }
        }

        return isEffective;
    }
    private static boolean isToolTierValid(ItemStack stack, BlockState state)
    {
        boolean isValid = false;

        Item itm = stack.getItem();
        // Valyrian Steel Tools work on everything
        isValid = isCustomMember(itm, ModMiningToolItems.VALYRIAN_STEEL_TOOLS);
        if (!isValid)
        {
            // Obsidian Tools work on everything
            isValid |= isCustomMember(itm, ModMiningToolItems.OBSIDIAN_TOOLS);
        }
        if (!isValid)
        {
            // Diamond Tools work on everything
            isValid |= isCustomMember(itm, ModMiningToolItems.DIAMOND_TOOLS);
        }
        if (!isValid)
        {
            if (itm instanceof net.minecraft.world.item.Item)
            {
                isValid |= ((Item)itm).isCorrectToolForDrops(stack, state);
            }
        }

        return isValid;
    }

    private static void doCustomDamage(ItemStack stack, int damageCount, LivingEntity entity)
    {
        for (int i = 0; i < damageCount; i++)
        {
            // Don't allow the item to break on anything but the first point of damage (may allow "uncharged" damage, but will allow the user to prevent loss of item unexpectedly)
            int damageDone = stack.getDamageValue();
            int damageMax = stack.getMaxDamage();
            int damageLeft = damageMax - damageDone;
            if ((i == 0) || (damageLeft > 2))
            {
                stack.hurtAndBreak(1, entity, EquipmentSlot.MAINHAND);
            }
        }
    }


    /** Attempt to break blocks around the given pos in a 3x3x1 square relative to the targeted face.*/
    private static int attemptBreakNeighbors(ItemStack stack, ServerLevel world,
                                             BlockPos pos,
                                             Player player,
                                             boolean checkHarvestLevel) {

        int damageCount = ModMiningToolItems.INITIAL_DAMAGE;
        BlockHitResult trace = SharedFunctions.calcRayTrace(world, player, ClipContext.Fluid.ANY);

        if (trace.getType() == BlockHitResult.Type.BLOCK)
        {
            Direction face = trace.getDirection();

            Vec3i blockVec = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
            ArrayList<BlockPos> blocksToBreak = new ArrayList<>();
            for (int a = -1; a <= 1; a++)
            {
                for (int b = -1; b <= 1; b++)
                {
                    Vec3i offsetVec = new Vec3i(0, 0, 0);

                    if (face == Direction.UP || face == Direction.DOWN) offsetVec = new Vec3i(a, 0, b);
                    if (face == Direction.NORTH || face == Direction.SOUTH) offsetVec = new Vec3i(a, b, 0);
                    if (face == Direction.EAST || face == Direction.WEST) offsetVec = new Vec3i(0, a, b);

                    BlockPos newBreak = new BlockPos(blockVec.offset(offsetVec));
                    blocksToBreak.add(newBreak);
                }
            }

            for (int i = 0; i < blocksToBreak.size(); i++)
            {
                if (attemptBreak(stack, world, pos, blocksToBreak.get(i), player, checkHarvestLevel))
                {
                    damageCount++;
                }
            }
        }
        return damageCount;
    }
    private static int attemptDrillNeighbors(ItemStack stack, ServerLevel world,
                                             BlockPos dropPos,
                                             BlockPos pos,
                                             Player player,
                                             int spanFromCenter,
                                             int maxDepth,
                                             int remainingDepth,
                                             Direction drillDirection,
                                             List<BlockPos> borderBlocks,
                                             List<BlockPos> innerBlocks,
                                             BlockState wallBlock) {

        int damageCount = ModMiningToolItems.INITIAL_DAMAGE;
        Direction face;
        if (maxDepth == remainingDepth)
        {
            BlockHitResult trace = SharedFunctions.calcRayTrace(world, player, ClipContext.Fluid.ANY);

            if (trace.getType() == BlockHitResult.Type.BLOCK)
            {
                face = trace.getDirection();
            }
            else
            {
                return damageCount;
            }
        }
        else
        {
            face = drillDirection;
        }

        ArrayList<BlockPos> blocksToBreak = new ArrayList<>();
        Vec3i blockVec = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
        for (int a = -spanFromCenter; a <= spanFromCenter; a++)
        {
            for (int b = -spanFromCenter; b <= spanFromCenter; b++)
            {
                Vec3i offsetVec = new Vec3i(0, 0, 0);

                if (face == Direction.UP    || face == Direction.DOWN)  offsetVec = new Vec3i(a, 0, b);
                if (face == Direction.NORTH || face == Direction.SOUTH) offsetVec = new Vec3i(a, b, 0);
                if (face == Direction.EAST  || face == Direction.WEST)  offsetVec = new Vec3i(0, a, b);

                BlockPos candidatePos = new BlockPos(blockVec.offset(offsetVec));
                BlockState candidateState = world.getBlockState(candidatePos);
                boolean shouldBreak = !isExemptedBlock(candidateState);

                if ((a == -spanFromCenter) || (a == spanFromCenter) || (b == -spanFromCenter) || (b == spanFromCenter))
                {
                    if (borderBlocks != null)
                    {
                        borderBlocks.add(candidatePos);
                        // Don't break a border block IF
                        // 1) We have a wall-block set
                        // 2) This border block is not already that block
                        if (wallBlock != null && candidateState.is(wallBlock.getBlock()))
                        {
                            shouldBreak = false;
                        }
                    }
                }
                else
                {
                    if (innerBlocks != null)
                    {
                        innerBlocks.add(candidatePos);
                    }
                }
                if (shouldBreak) blocksToBreak.add(candidatePos);
            }

            for (int i = 0; i < blocksToBreak.size(); i++)
            {
                if (attemptBreak(stack, world, dropPos, blocksToBreak.get(i), player, false))
                {
                    damageCount++;
                }
            }
        }
        if (remainingDepth > 0)
        {
            Vec3i offsetVec = switch (face)
            {
                case UP -> new Vec3i(0, -1, 0);
                case DOWN -> new Vec3i(0, 1, 0);
                case NORTH -> new Vec3i(0, 0, 1);
                case SOUTH -> new Vec3i(0, 0, -1);
                case EAST -> new Vec3i(-1, 0, 0);
                case WEST -> new Vec3i(1, 0, 0);
            };
            BlockPos target = new BlockPos(blockVec.offset(offsetVec));

            damageCount += attemptDrillNeighbors(stack, world, dropPos, target, player, spanFromCenter, maxDepth, remainingDepth - 1, face, borderBlocks, innerBlocks, wallBlock);
        }
        return damageCount;
    }

    private static boolean isExemptedBlock(BlockState state)
    {
        boolean exempted = false;
        if (state.is(Blocks.BEDROCK)) exempted = true;   // Block is BEDROCK -- don't mess with it
        if (state.is(Blocks.TORCH)) exempted = true;   // Block is TORCH -- don't mess with it
        if (state.is(Blocks.WALL_TORCH)) exempted = true;   // Block is WALL_TORCH -- don't mess with it
        if (state.is(Blocks.LANTERN)) exempted = true;   // Block is LANTERN -- don't mess with it
        if (state.is(Blocks.SOUL_TORCH)) exempted = true;   // Block is SOUL_TORCH -- don't mess with it
        if (state.is(Blocks.SOUL_WALL_TORCH)) exempted = true;   // Block is SOUL_WALL_TORCH -- don't mess with it
        if (state.is(Blocks.SOUL_LANTERN)) exempted = true;   // Block is SOUL_LANTERN -- don't mess with it
        return exempted;
    }
    public static void fillBorerWalls(ItemStack stack, ServerLevel world,
                                      BlockPos pos,
                                      Player player,
                                      List<BlockPos> borderBlocks,
                                      List<BlockPos> innerBlocks,
                                      BlockState wallBlock)
    {
        if (innerBlocks != null)
        {
            for (int i = 0; i < innerBlocks.size(); i++)
            {
                // Ignore specific kinds of blocks
                if (isExemptedBlock(world.getBlockState(innerBlocks.get(i)))) continue;
                world.setBlock(innerBlocks.get(i), Blocks.AIR.defaultBlockState(), 3);
            }
        }
        if ((wallBlock != null) && (borderBlocks != null))
        {
            for (int i = 0; i < borderBlocks.size(); i++)
            {
                if (!SharedFunctions.TryReplaceBlock(player, world, borderBlocks.get(i),
                        wallBlock.getBlock().asItem(),
                        wallBlock))
                {
                    break;
                }
            }
        }
    }

    public static int attemptLogNeighbors(ItemStack stack, ServerLevel world,
                                           BlockPos dropPos,
                                           BlockPos pos,
                                           Player player,
                                           boolean checkHarvestLevel)
{
    int damageCount = ModMiningToolItems.INITIAL_DAMAGE;
    ArrayList<BlockPos> logs = new ArrayList<>();
    ArrayList<BlockPos> candidates = new ArrayList<>();
    candidates.add(pos);

    for (int i = 0; i < candidates.size(); i++)
    {
        if (logs.size() > MAX_LOG_SHEAR_BREAK)
            break; // Whatever this is, it's too big! I don't want to know what happens if I let you use this in an all-log RFTDim.

        BlockPos candidate = candidates.get(i);
        BlockState state = world.getBlockState(candidate);

        boolean isEffective = isToolEffective(stack, state);

        if (isEffective)
        {
            // We found an axe-able block, check for neighboring axe-able block
            for (int z = 0; z <= 1; z++)
            {
                // No good reason to check downwards, cuts 1/3 off this loop
                for (int x = -1; x <= 1; x++)
                {
                    for (int y = -1; y <= 1; y++)
                    {
                        BlockPos neighbor = candidate.offset(x, z, y);
                        if (candidates.contains(neighbor)) continue; // Don't check positions twice
                        candidates.add(neighbor);
                        if (!state.is(Blocks.CHEST)
                                && !state.is(Blocks.TRAPPED_CHEST))
                        {
                            // Skip chests, but add otherwise
                            logs.add(neighbor);
                        }
                    }
                }
            }
        }
    }

    // Now logs contains positions of all log blocks within reach
    for (BlockPos log : logs)
    {
        if (attemptBreak(stack, world, dropPos, log, player, false))
        {
            damageCount++;
        }
    }
    return damageCount;
}

    public static int attemptShearNeighbors(ItemStack stack, ServerLevel world,
                                            BlockPos dropPos,
                                            BlockPos pos,
                                            Player player)
    {
        int damageCount = ModMiningToolItems.INITIAL_DAMAGE;
        ArrayList<BlockPos> leaves = new ArrayList<>();
        ArrayList<BlockPos> candidates = new ArrayList<>();
        candidates.add(pos);

        for (int i = 0; i < candidates.size(); i++)
        {
            if (leaves.size() > MAX_LOG_SHEAR_BREAK)
                break;

            BlockPos candidate = candidates.get(i);
            BlockState state = world.getBlockState(candidate);

            boolean isEffective = isToolEffective(stack, state);
            if (isEffective)
            {
                leaves.add(candidate);

                // We found a leaf, check for neighboring leaves
                for (int z = -1; z <= 1; z++)
                {
                    for (int x = -1; x <= 1; x++)
                    {
                        for (int y = -1; y <= 1; y++)
                        {
                            BlockPos neighbor = candidate.offset(x, z, y);
                            if (candidates.contains(neighbor)) continue; // Don't check positions twice
                            candidates.add(neighbor);
                        }
                    }
                }
            }
        }

        // Now leaves contains positions of all log blocks within reach
        for (BlockPos leaf : leaves)
        {
            if (attemptBreak(stack, world, dropPos, leaf, player, false))
            {
                damageCount++;
            }
        }
        return damageCount;
    }
    public static boolean attemptBreak(ItemStack stack, ServerLevel world,
                                       BlockPos dropPos,
                                       BlockPos pos,
                                       Player player,
                                       boolean checkHarvestLevel)
    {
        BlockState state = world.getBlockState(pos);

        boolean isSnow = false;
        boolean takesDamage = false;

        Item item = stack.getItem();
        if (state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST) || state.is(Blocks.BARREL))
        {
            if (isCustomMember(item, ModMiningToolItems.CUSTOM_AXES))
                return false;
        }
        else if (state.is(Blocks.ENDER_CHEST)) {
            if (isCustomMember(item, ModMiningToolItems.CUSTOM_PICKAXES))
                return false;
        }
        isSnow = state.is(Blocks.SNOW);



        //int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player);
        //int silkLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player);
        //int unbreakLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, player);

        boolean validHarvest = !checkHarvestLevel;
        if (!validHarvest)
        {
            validHarvest = isToolTierValid(stack, state);
        }
        boolean isEffective = isToolEffective(stack, state);
        boolean witherImmune = state.is(BlockTags.WITHER_IMMUNE);

        if (validHarvest && isEffective && !witherImmune)
        {
            world.destroyBlock(pos, false);
            double avoidDamageChance = ModMiningToolItems.BASE_AVOID_DAMAGE_CHANCE;
            if (ModMiningToolItems.GOLD_TOOLS.contains(item))
            {
                // Gold tools: Triple chance to avoid damage
                avoidDamageChance *= ModMiningToolItems.GOLD_AVOID_DAMAGE_CHANCE_MULTIPLIER;
            }
            //double damageRoll = random.nextFloat() / (unbreakLevel + 1);
            double damageRoll = SharedFunctions.random.nextFloat();
            //  Unbreaking      Max Roll    Normal Avoid Chance     Gold Avoid Chance
            //  0               1.0         5% (0.05 / 1.0)         15% (0.15 / 1.0)
            //  1               0.5         10% (0.05 / 0.5)        30% (0.15 / 0.5)
            //  2               0.33        15% (0.05 / 0.33)       50% (0.15 / 0.33)
            //  3               0.25        20% (0.05 / 0.25)       60% (0.15 / 0.25)
            if (damageRoll >= avoidDamageChance)
            {
                if (!world.isClientSide() && !state.is(BlockTags.FIRE))
                {
                    takesDamage = true;
                }
            }
            //itm.mineBlock(stack, world, state, pos, player);
            List<ItemStack> blockDrops;
            if (ModMiningToolItems.CUSTOM_SHEARS.contains(player.getMainHandItem().getItem()))
            {
                // Fake like we're actually using the vanilla shears, to ensure we get the correct drops
                blockDrops = Block.getDrops(state, world, pos, null, player, new ItemStack(Items.SHEARS));
            }
            else
            {
                blockDrops = Block.getDrops(state, world, pos, null, player, player.getMainHandItem());
            }
            for (ItemStack blockDrop : blockDrops)
            {
                //Block.dropResources(state, world, pos);
                Block.popResource(world, dropPos, blockDrop);
                if (ModMiningToolItems.CUSTOM_SHEARS.contains(player.getMainHandItem().getItem()))
                {
                    // We're using the custom shears, and this is a "shearable" leafy block
                    Block.popResource(world, dropPos, blockDrop);
                }
            }
            RandomSource rnd = RandomSource.create();
            int exp = state.getExpDrop(world, dropPos, null, player, stack);
            if (exp > 0) state.getBlock().popExperience(world, dropPos, exp);
        }
        if (isSnow) return false;
        return takesDamage;
    }
    /** Attempt to break blocks around the given pos in a 3x3x1 square relative to the targeted face.*/
    public static int attemptBreakRegion(ItemStack stack, ServerLevel world,
                                         BlockPos dropPos,
                                         BlockPos pos,
                                         Player player,
                                         int xStart, int xEnd, int yStart, int yEnd, int zStart, int zEnd,
                                         boolean checkHarvestLevel) {

        int damageCount = ModMiningToolItems.INITIAL_DAMAGE;
        BlockHitResult trace = SharedFunctions.calcRayTrace(world, player, ClipContext.Fluid.ANY);

        if (trace.getType() == BlockHitResult.Type.BLOCK)
        {
            Direction face = trace.getDirection();
            // Need to determine if facing N/S or E/W
            Direction playerFacing = SharedFunctions.getDirection(player);
            Vec3i blockVec = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
            ArrayList<BlockPos> blocksToBreak = new ArrayList<>();
            for (int a = xStart; a <= xEnd; a++)
            {
                for (int b = zStart; b <= zEnd; b++)
                {
                    for (int c = yStart; c <= yEnd; c++)
                    {
                        Vec3i offsetVec = new Vec3i(0, 0, 0);

                        if (face == Direction.UP || face == Direction.DOWN)
                        {
                            if (playerFacing == Direction.NORTH) offsetVec = new Vec3i(-a, c, -b);
                            if (playerFacing == Direction.SOUTH) offsetVec = new Vec3i(a, c, b);
                            if (playerFacing == Direction.EAST) offsetVec = new Vec3i(b, c, a);
                            if (playerFacing == Direction.WEST) offsetVec = new Vec3i(-b, c, -a);
                        }
                        if (face == Direction.NORTH || face == Direction.SOUTH) offsetVec = new Vec3i(a, b, c);
                        if (face == Direction.EAST || face == Direction.WEST) offsetVec = new Vec3i(c, b, a);

                        BlockPos testBlockPos = new BlockPos(blockVec.offset(offsetVec));
                        BlockState state = world.getBlockState(testBlockPos);
                        if (!state.isAir()
                                && !state.is(Blocks.TRAPPED_CHEST)
                                && !state.is(Blocks.CHEST)
                                && !state.is(Blocks.ENDER_CHEST))
                        {
                            blocksToBreak.add(testBlockPos);
                        }
                    }
                }
            }

            for (int i = 0; i < blocksToBreak.size(); i++)
            {
                if (attemptBreak(stack, world, dropPos, blocksToBreak.get(i), player, checkHarvestLevel))
                {
                    damageCount++;
                }
            }
        }
        return damageCount;
    }
}
