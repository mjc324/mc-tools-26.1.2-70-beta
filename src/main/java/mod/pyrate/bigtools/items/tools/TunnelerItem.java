package mod.pyrate.bigtools.items.tools;

import mod.pyrate.bigtools.functions.MiningFunctions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class TunnelerItem extends net.minecraft.world.item.Item
{
    private final Supplier<Item> myCrusher;
    private final Supplier<Item> myDigger;
    private BlockState myWallBlock;
    private String myName;

    public TunnelerItem(ToolMaterial tier, TagKey<Block> tagBlock, Item.Properties props,
                        Supplier<Item> crusher, Supplier<Item> digger)
    {
        super(props.tool(tier, tagBlock, tier.attackDamageBonus(), tier.speed(), 0.0f));
        myCrusher = crusher;
        myDigger = digger;
        myWallBlock = null;
        myName = null;
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        MiningFunctions.doToolBorerBlock(stack, world, state, pos, entity, myWallBlock);
        return true;//super.mineBlock(stack, world, state, pos, entity);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state)
    {
        boolean supCorrect = super.isCorrectToolForDrops(stack, state);
        boolean isCrushable = myCrusher.get().isCorrectToolForDrops(stack, state);
        boolean isDiggable = myDigger.get().isCorrectToolForDrops(stack, state);
        boolean isPowderSnow = state.is(Blocks.POWDER_SNOW);
        boolean isTierAppropriate = true;//net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
        return isTierAppropriate && (isCrushable || isDiggable || isPowderSnow);
    }

    public @NotNull InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        Level clientLevel = context.getLevel();
        ItemStack tool = context.getItemInHand();
        if (myWallBlock == null)
        {
            if (tool.has(DataComponents.CUSTOM_NAME))
            {
                tool.remove(DataComponents.CUSTOM_NAME);
            }
        }

        if (context.isSecondaryUseActive())
        {
            // Player is sneaking -- sample the target block
            if (player instanceof ServerPlayer serverPlayer)
            {
                BlockPos target = context.getClickedPos();
                ServerLevel serverLevel = serverPlayer.level();
                BlockState selectedBlock = serverLevel.getBlockState(target);

                if (myWallBlock != null)
                {
                    if (myWallBlock.is(selectedBlock.getBlock()))
                    {
                        // Selected the same block, clear the block
                        myWallBlock = null;
                        tool.remove(DataComponents.CUSTOM_NAME);
                        return (clientLevel.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
                    }
                }
                // Either myWallBlock is null (not set) -OR- we selected a different block and need to change
                myWallBlock = selectedBlock;
                if (myName == null)
                {
                    myName = tool.getDisplayName().getString();
                    myName = myName.replace("[","").replace("]","");
                }
                String newName = myName + " (" + myWallBlock.getBlock().getName().getString() + ")";
                tool.set(DataComponents.CUSTOM_NAME, Component.literal(newName));
                return (clientLevel.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
            }
        }
        return InteractionResult.PASS;
    }
}
