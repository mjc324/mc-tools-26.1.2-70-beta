package mod.pyrate.bigtools.items.tools;
// Copyright (c) 2019 Alexander Strada - MIT License (This header, with links, must not be removed)
//     https://github.com/astradamus/PracticalTools
//     https://curseforge.com/minecraft/mc-mods/practical-tools
//     https://twitch.tv/neurodr0me

import mod.pyrate.bigtools.functions.MiningFunctions;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;


public class CrusherItem extends net.minecraft.world.item.Item
{
    public CrusherItem(ToolMaterial tier, TagKey<Block> tagBlock, Item.Properties props)
    {
        super(props.pickaxe(tier, tier.attackDamageBonus(), tier.speed()));
    }

    //@Override
    //public boolean canPerformAction(ItemStack stack, ItemAbility toolAction)
    //{
    //    return super.canPerformAction(stack, toolAction);
    //}

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        MiningFunctions.doToolMine3x3(stack, world, state, pos, entity);
        return true;//super.mineBlock(stack, world, state, pos, entity);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state)
    {
        boolean isCrushable = super.isCorrectToolForDrops(stack, state);
        boolean isPowderSnow = state.is(Blocks.POWDER_SNOW);
        boolean isTierAppropriate = true;//net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
        return isTierAppropriate && (isCrushable || isPowderSnow);
    }
}