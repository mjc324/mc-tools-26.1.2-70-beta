package mod.pyrate.bigtools.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.abs;

public class SharedFunctions
{
    public static final Random random = new Random();
    public static final Set<Block> ExcludedBlocks = Set.of(Blocks.TORCH, Blocks.WALL_TORCH, Blocks.LANTERN,
            Blocks.SOUL_TORCH, Blocks.SOUL_WALL_TORCH, Blocks.SOUL_LANTERN,
            Blocks.REDSTONE_TORCH, Blocks.REDSTONE_WALL_TORCH,
            Blocks.CAMPFIRE, Blocks.TRIPWIRE);

    public static boolean HasBlockInInventory(Player player, Item item)
    {
        return player.getInventory().hasAnyOf(new HashSet<Item>(Arrays.asList(item)));
    }
    public static void RemoveBlockFromInventory(Player player, Item item)
    {
        int iCount = player.getInventory().getContainerSize();
        for (int iIndex = iCount - 1; iIndex >= 0; iIndex--)
        //for (ItemStack stack: player.getInventory().items)
        {
            ItemStack stack = player.getInventory().getItem(iIndex);
            if (stack.is(item))
            {
                stack.setCount(stack.getCount() - 1);
                break;
            }
        }
    }
    public static boolean TryReplaceBlock(Player player, ServerLevel world, BlockPos pos, Item pathItem, BlockState wallBlock)
    {
        BlockState state = world.getBlockState(pos);
        if (state.is(Blocks.BEDROCK)) return true;  // Block is Bedrock -- don't mess with it

        if (!state.is(wallBlock.getBlock()))
        {
            if (HasBlockInInventory(player, pathItem))
            {
                RemoveBlockFromInventory(player, pathItem);
                world.setBlock(pos, wallBlock, 3);      // Third parameter is block flags, currently guessing? (1 + 2 = block update and send change to clients)
                return true;        // Return TRUE -- block updated successfully
            }
        }
        else
        {
            // The block is already the desired block, skip it
            return true;
        }
        return false; // Return FALSE -- block update failed due to insufficient blocks
    }
    /** Copy-pasted from "Item.getPlayerPOVHitResult" which is protected static, making it unusable in my own static helper methods.*/
    public static BlockHitResult calcRayTrace(ServerLevel worldIn, Player player, ClipContext.Fluid fluidContext)
    {
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = Mth.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -Mth.cos(-f * ((float)Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = 10;//player.getAttribute(ForgeMod.ENTITY_REACH.get()).getValue();
        Vec3 vec31 = vec3.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return worldIn.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, fluidContext, player));
    }

    public static @NotNull Direction getDirection(Player player)
    {
        return player.getDirection();
        // Cardinal Directions
        // North <= +/-180
        // East <= -90
        // South <= 0
        // West <= 90

        // Accepted angular ranges
        // North : -135 to 135 (through -180)
        // East : 45 to 135
        // South : 45 to -45 (through 0)
        // West : 45 to 135
        /*
        double playerAngle = player.yHeadRot;
        double absAngle = abs(playerAngle);
        if (absAngle >= 135.0) return Direction.NORTH;
        if (absAngle <= 45.0) return Direction.SOUTH;

        if ((playerAngle < 0) && (absAngle >= 45.0)) return Direction.EAST;
        if ((playerAngle > 0) && (absAngle >= 45.0)) return Direction.WEST;
        return Direction.WEST;
        */
    }
}
