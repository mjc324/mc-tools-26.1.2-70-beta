package mod.pyrate.bigtools.functions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;

public class PavingFunctions
{
    public static boolean MakePath(Player player, Level world, BlockPos target, Item pathItem, BlockState pathBlock, int pathWidth, int pathLength)
{
    int dx = (pathWidth / 2);
    return PavePathway(player, (ServerLevel)world, target, pathItem, pathBlock, dx, 0, 0,0, pathLength);
}

    private static boolean PavePathway(Player player, ServerLevel world, BlockPos pos,
                                       Item pathItem, BlockState pathState,
                                       int dWidth, int yStart, int yEnd, int zStart, int zEnd)
    {
        if ((pathItem == null) || (pathState == null))
        {
            pathState = world.getBlockState(pos);
            pathItem = pathState.getBlock().asItem();
        }
        if (SharedFunctions.ExcludedBlocks.contains(pathState.getBlock()))
            return false;

        BlockHitResult trace = SharedFunctions.calcRayTrace(world, player, ClipContext.Fluid.ANY);

        if (trace.getType() == BlockHitResult.Type.BLOCK)
        {
            Direction face = trace.getDirection();

            Vec3i blockVec = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
            ArrayList<BlockPos> blocksToPave = new ArrayList<>();

            // Need to determine if facing N/S or E/W
            Direction playerFacing = SharedFunctions.getDirection(player);

            for (int b = zStart; b <= zEnd; b++)
            {
                for (int c = yStart; c <= yEnd; c++)
                {
                    for (int w = 0; w <= dWidth; w++)
                    {
                        for (int side = 1; side >= -1; side -= 2)
                        {
                            int a = side * w;
                            Vec3i offsetVec = new Vec3i(0, 0, 0);

                            if (face == Direction.UP || face == Direction.DOWN)
                            {
                                if (playerFacing == Direction.NORTH) offsetVec = new Vec3i(-a, c, -b);
                                if (playerFacing == Direction.SOUTH) offsetVec = new Vec3i(a, c, b);
                                if (playerFacing == Direction.EAST) offsetVec = new Vec3i(b, c, a);
                                if (playerFacing == Direction.WEST) offsetVec = new Vec3i(-b, c, -a);
                            }
                            else if (face == Direction.NORTH || face == Direction.SOUTH)
                            {
                                offsetVec = new Vec3i(a, b, c);
                            }
                            else if (face == Direction.EAST || face == Direction.WEST)
                            {
                                offsetVec = new Vec3i(c, b, a);
                            }

                            BlockPos testBlockPos = new BlockPos(blockVec.offset(offsetVec));
                            blocksToPave.add(testBlockPos);
                            if (a == 0)
                                break;
                        }
                    }
                }
            }

            for (int i = 0; i < blocksToPave.size(); i++)
            {
                if (!SharedFunctions.TryReplaceBlock(player, world, blocksToPave.get(i), pathItem, pathState))
                {
                    break;
                }
            }
        }
        return true;
    }

    public static boolean MakeWall(Player player, Level world, BlockPos target, Item pathItem, BlockState pathBlock, int wallHeight, int wallLength)
    {
        return BuildWall(player, (ServerLevel)world, target, pathItem, pathBlock, wallHeight, wallLength);
    }

    private static boolean BuildWall(Player player, ServerLevel world, BlockPos pos,
                                     Item pathItem, BlockState pathState,
                                     int wallHeight, int wallLength)
    {
        if ((pathItem == null) || (pathState == null))
        {
            pathState = world.getBlockState(pos);
            pathItem = pathState.getBlock().asItem();
        }
        if (SharedFunctions.ExcludedBlocks.contains(pathState.getBlock()))
            return false;

        BlockHitResult trace = SharedFunctions.calcRayTrace(world, player, ClipContext.Fluid.ANY);

        if (trace.getType() == BlockHitResult.Type.BLOCK)
        {
            Direction face = trace.getDirection();

            if (face == Direction.UP || face == Direction.DOWN)
            {
                return false;
            }

            Vec3i blockVec = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
            ArrayList<BlockPos> blocksToPave = new ArrayList<>();

            int x = pos.getX(), y = pos.getY(), z = pos.getZ();
            int startX = 0, endX = 0, startZ = 0, endZ = 0;
            switch(face)
            {
                case Direction.NORTH:
                {
                    startX = -1;
                    endX = 1;
                    break;
                }
                case Direction.SOUTH:
                {
                    startX = -1;
                    endX = 1;
                    break;
                }
                case Direction.WEST:
                {
                    startZ = -1;
                    endZ = 1;
                    break;
                }
                case Direction.EAST:
                {
                    startZ = -1;
                    endZ = 1;
                    break;
                }
            }
            for (int bz = startZ; bz <= endZ; bz++)
            {
                for (int bx = startX; bx <= endX; bx++)
                {
                    for (int by = -1; by <= 1; by++)
                    {
                        BlockPos testBlockPos = new BlockPos(x + bx, y + by, z + bz);
                        blocksToPave.add(testBlockPos);
                    }
                }
            }

            for (BlockPos blockPos : blocksToPave)
            {
                if (!SharedFunctions.TryReplaceBlock(player, world, blockPos, pathItem, pathState))
                {
                    break;
                }
            }
        }
        return true;
    }
    public static boolean BuildStairs(Player player, Level world, BlockPos target, Item stairItem, BlockState stairBlock, int pathWidth, int stairLength)
    {
        return attemptBuildStairs(player, (ServerLevel) world, target, stairItem, stairBlock, stairLength);
    }
    private static boolean attemptBuildStairs(Player player, ServerLevel world, BlockPos pos,
                                              Item stairItem, BlockState stairBlock,
                                              int stairLength)
    {

        if (SharedFunctions.ExcludedBlocks.contains(stairBlock.getBlock()))
            return false;

        int playerY = (int)Math.round(player.position().y() - 0.5);
        int blockX = pos.getX();
        int blockY = pos.getY();
        int blockZ = pos.getZ();

        int delta = 1; // By default, make stairs go up
        if (blockY < playerY)
        {
            // Stair is below, make stairs go down
            delta = -1;
        }
        // Otherwise, Stair is even or above, make stairs go up


        BlockHitResult trace = SharedFunctions.calcRayTrace(world, player, ClipContext.Fluid.ANY);
        if (trace.getType() == BlockHitResult.Type.BLOCK)
        {
            /*
            // Need to determine if facing N/S or E/W
            Direction playerFacing = SharedFunctions.getDirection(player);
            */

            Direction blockFacing = stairBlock.getBedDirection(world, pos);
            Vec3i blockVec = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
            ArrayList<BlockPos> blocksToPave = new ArrayList<>();
            Vec3i offsetVec = new Vec3i(0, 0, 0);

            for (int i = 0; i < stairLength; i++)
            {
                if (blockFacing == Direction.NORTH) offsetVec = new Vec3i(0, (delta * i), -(delta * i));
                if (blockFacing == Direction.SOUTH) offsetVec = new Vec3i(0, (delta * i), +(delta * i));
                if (blockFacing == Direction.WEST) offsetVec = new Vec3i(-(delta * i),  (delta * i), 0);
                if (blockFacing == Direction.EAST) offsetVec = new Vec3i(+(delta * i) ,  (delta * i), 0);
                BlockPos testBlockPos = new BlockPos(blockVec.offset(offsetVec));
                blocksToPave.add(testBlockPos);
            }

            for (int i = 0; i < blocksToPave.size(); i++)
            {
                if (!SharedFunctions.TryReplaceBlock(player, world, blocksToPave.get(i), stairItem, stairBlock))
                {
                    break;
                }
            }
        }
        return true;
    }


    public static boolean CloneBlock(Player player, Level world, BlockPos target, Item cloneItem, BlockState cloneBlock, int cloneCount, int cloneSpacing)
    {
        return attemptCloneBlock(player, (ServerLevel) world, target, cloneItem, cloneBlock, cloneCount, cloneSpacing);
    }
    private static boolean attemptCloneBlock(Player player, ServerLevel world, BlockPos pos,
                                             Item cloneItem, BlockState cloneBlock,
                                             int cloneCount, int cloneSpacing)
    {
        int playerY = (int)Math.round(player.position().y() - 0.5);
        int blockX = pos.getX();
        int blockY = pos.getY();
        int blockZ = pos.getZ();

        BlockHitResult trace = SharedFunctions.calcRayTrace(world, player, ClipContext.Fluid.ANY);
        if (trace.getType() == BlockHitResult.Type.BLOCK)
        {
            Direction face = trace.getDirection();

            Vec3i blockVec = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
            ArrayList<BlockPos> cloneTargets = new ArrayList<>();

            // Need to determine if facing N/S or E/W
            Direction playerFacing = SharedFunctions.getDirection(player);

            int db = 1;
            int dc = 1;
            int b = 0;
            int c = 0;
            if (face == Direction.UP || face == Direction.DOWN)
            {
                db = cloneSpacing;
                dc = 0;
            }
            else
            {
                return false;
            }
            for (int cloneIdx = 0; cloneIdx < cloneCount; cloneIdx++)
            {
                Vec3i offsetVec = new Vec3i(0, 0, 0);

                if (face == Direction.UP || face == Direction.DOWN)
                {
                    if (playerFacing == Direction.NORTH) { offsetVec = new Vec3i(0, c, b); b -= db; }
                    if (playerFacing == Direction.SOUTH) { offsetVec = new Vec3i(0, c, b); b += db; }
                    if (playerFacing == Direction.EAST) { offsetVec = new Vec3i(b, c, 0); b += db; }
                    if (playerFacing == Direction.WEST) { offsetVec = new Vec3i(b, c, 0); b -= db; }
                }
                else if (face == Direction.NORTH || face == Direction.SOUTH)
                {
                    offsetVec = new Vec3i(0, b, c);
                    c += dc;
                }
                else if (face == Direction.EAST || face == Direction.WEST)
                {
                    offsetVec = new Vec3i(c, b, 0);
                    c += dc;
                }

                BlockPos testBlockPos = new BlockPos(blockVec.offset(offsetVec));
                cloneTargets.add(testBlockPos);
            }

            for (int i = 0; i < cloneTargets.size(); i++)
            {
                if (!SharedFunctions.TryReplaceBlock(player, world, cloneTargets.get(i), cloneItem, cloneBlock))
                {
                    break;
                }
            }
        }
        return true;
    }
}
