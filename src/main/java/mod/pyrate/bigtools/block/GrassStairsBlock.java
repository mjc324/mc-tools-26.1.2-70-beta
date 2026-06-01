package mod.pyrate.bigtools.block;

import mod.pyrate.bigtools.BigTools;
import mod.pyrate.bigtools.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;

public class GrassStairsBlock extends StairBlock
{
    public GrassStairsBlock(Block pBaseBlock, String id)
    {
        super(pBaseBlock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(pBaseBlock).mapColor(MapColor.GRASS).setId(ModBlocks.getBlockKey(id)));
    }
}
