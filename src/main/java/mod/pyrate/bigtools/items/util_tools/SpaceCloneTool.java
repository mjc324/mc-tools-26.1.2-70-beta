package mod.pyrate.bigtools.items.util_tools;

import mod.pyrate.bigtools.functions.PavingFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SpaceCloneTool extends Item
{
    public static final int INITIAL_SPACING = 0;
    public static final int MIN_SPACING = 0;
    public static final int MAX_SPACING = 9;
    public static final int MIN_CLONE_COUNT = 3;
    public static final int MAX_CLONE_COUNT = 5;
    public static final int MAX_RANGE = 30;

    private int mySpacing;
    private final int myMaxSpacing;

    public SpaceCloneTool(Item.Properties p_40626_, int initialSpacing, int maxSpacing)
    {
        super(p_40626_);
        this.mySpacing = initialSpacing;
        this.myMaxSpacing = maxSpacing;
    }

    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        Level clientLevel = context.getLevel();
        ItemStack tool = context.getItemInHand();

        if (context.isSecondaryUseActive())
        {
            // Player is sneaking -- sample the target block
            if (player instanceof ServerPlayer serverPlayer)
            {
                if (tool.has(DataComponents.DAMAGE)) mySpacing = tool.get(DataComponents.DAMAGE);
                int newSpacing = mySpacing + 1;
                if (newSpacing > myMaxSpacing)
                {
                    mySpacing = MIN_SPACING;
                }
                else
                {
                    mySpacing = newSpacing;
                }

                String newName = "Clone Tool (" + mySpacing + ")";
                tool.set(DataComponents.CUSTOM_NAME, Component.literal(newName));
                tool.set(DataComponents.DAMAGE, mySpacing);
            }
        }
        else
        {
            if (tool.has(DataComponents.DAMAGE)) mySpacing = tool.get(DataComponents.DAMAGE);
            String newName = "Clone Tool (" + mySpacing + ")";
            tool.set(DataComponents.CUSTOM_NAME, Component.literal(newName));

            if (player instanceof ServerPlayer serverPlayer)
            {
                BlockPos target = context.getClickedPos();
                ServerLevel serverLevel = serverPlayer.level();
                BlockState copyBlock = serverLevel.getBlockState(target);
                Item pathItem = copyBlock.getBlock().asItem();

                int cloneCount = (MAX_RANGE / (mySpacing + 1));
                if (cloneCount > MAX_CLONE_COUNT) cloneCount = MAX_CLONE_COUNT;
                if (cloneCount < MIN_CLONE_COUNT) cloneCount = MIN_CLONE_COUNT;

                if (PavingFunctions.CloneBlock(player, serverLevel, target, pathItem, copyBlock, cloneCount, mySpacing + 1))
                {
                    return (clientLevel.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
                }
            }
        }
        return InteractionResult.PASS;
    }
}
