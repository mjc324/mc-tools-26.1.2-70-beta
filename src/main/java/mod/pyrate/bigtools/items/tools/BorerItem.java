package mod.pyrate.bigtools.items.tools;


import mod.pyrate.bigtools.functions.MiningFunctions;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
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

import java.util.function.Supplier;


public class BorerItem extends net.minecraft.world.item.Item
{
    private final Supplier<Item> myCrusher;
    private final Supplier<Item> myDigger;
    private BlockState myWallBlock;
    private String myName;

    public BorerItem(ToolMaterial tier, TagKey<Block> tagBlock, Item.Properties props,
                     Supplier<Item> crusher, Supplier<Item> digger)
    {
        super(props.tool(tier, tagBlock, tier.attackDamageBonus(), tier.speed(), 0.0f));
        myCrusher = crusher;
        myDigger = digger;
        myWallBlock = null;
        myName = null;
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction)
    {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        MiningFunctions.doToolBorerBlock(stack, world, state, pos, entity, null);
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
}