package mod.pyrate.bigtools.items.util_tools;

import mod.pyrate.bigtools.functions.PavingFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Simple_Path_Roller extends Item
{
    public static final int DEFAULT_PATH_WIDTH = 3;
    public static final int DEFAULT_PATH_LENGTH = 5;

    private final int myPathWidth;
    private final int myPathLength;
    private final BlockState myBlock;
    private final Item myItem;

    public Simple_Path_Roller(Item.Properties p_40626_, int width, int length)
    {
        super(p_40626_);
        this.myPathWidth = width;
        this.myPathLength = length;
        this.myBlock = null;
        this.myItem = null;

    }

    public InteractionResult useOn(UseOnContext context)
    {
        Player player = context.getPlayer();
        Level clientLevel = context.getLevel();
        if (player instanceof ServerPlayer serverPlayer)
        {
            BlockPos target = context.getClickedPos();
            ServerLevel serverLevel = serverPlayer.level();
            BlockState pathBlock = serverLevel.getBlockState(target);
            Item pathItem = pathBlock.getBlock().asItem();

            if (PavingFunctions.MakePath(player, serverLevel, target, this.myItem, this.myBlock, myPathWidth, myPathLength - 1))
            {
                return (clientLevel.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER);
            }
        }
        return InteractionResult.PASS;
    }
}
