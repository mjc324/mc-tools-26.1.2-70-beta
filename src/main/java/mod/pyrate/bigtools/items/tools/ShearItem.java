package mod.pyrate.bigtools.items.tools;

import com.google.common.collect.Sets;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import mod.pyrate.bigtools.functions.MiningFunctions;

import java.util.Set;


public class ShearItem extends ShearsItem
{
    public static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.ACACIA_LEAVES, Blocks.OAK_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.SPRUCE_LEAVES,
                                                                  Blocks.COBWEB, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS, Blocks.VINE);

    //public static final Set<sMaterial> EFFECTIVE_MATERIALS = ImmutableSet.of(Material.LEAVES, Material.MOSS, Material.WEB);

    public ShearItem(Item.Properties props, ToolMaterial tier)
    {
        super(props.durability(tier.durability()));
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        MiningFunctions.doToolShearBlock(stack, world, state, pos, entity);
        return super.mineBlock(stack, world, state, pos, entity);
    }
}
