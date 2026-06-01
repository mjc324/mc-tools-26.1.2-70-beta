package mod.pyrate.bigtools.items.tools;
// Copyright (c) 2019 Alexander Strada - MIT License (This header, with links, must not be removed)
//     https://github.com/astradamus/PracticalTools
//     https://curseforge.com/minecraft/mc-mods/practical-tools
//     https://twitch.tv/neurodr0me

import mod.pyrate.bigtools.functions.MiningFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.function.Supplier;


public class ExcavatorItem extends net.minecraft.world.item.Item
{
    private final Supplier<Item> myCrusher;
    private final Supplier<Item> myDigger;
    private final TagKey<Block> myTags;

    public ExcavatorItem(ToolMaterial tier, TagKey<Block> tagBlock, Properties props,
                         Supplier<Item> crusher, Supplier<Item> digger)
    {
        super(props.tool(tier, tagBlock, tier.attackDamageBonus(), tier.speed(), 0.0f));
        myCrusher = crusher;
        myDigger = digger;
        myTags = tagBlock;
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        MiningFunctions.doToolMine11x11(stack, world, state, pos, entity);
        return super.mineBlock(stack, world, state, pos, entity);
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