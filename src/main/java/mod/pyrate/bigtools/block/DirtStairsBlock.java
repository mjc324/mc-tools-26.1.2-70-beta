package mod.pyrate.bigtools.block;

import mod.pyrate.bigtools.BigTools;
import mod.pyrate.bigtools.init.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class DirtStairsBlock extends StairBlock
{
    public DirtStairsBlock(BlockState p_56862_, Properties p_56863_)
    {
        super(p_56862_, p_56863_);
    }

    public DirtStairsBlock(Block pBaseBlock, String id)
    {
        super(pBaseBlock.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(pBaseBlock).setId(ModBlocks.getBlockKey(id)));
    }
}
