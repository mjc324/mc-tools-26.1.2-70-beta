package mod.pyrate.bigtools.items.tools;
// Copyright (c) 2019 Alexander Strada - MIT License (This header, with links, must not be removed)
//     https://github.com/astradamus/PracticalTools
//     https://curseforge.com/minecraft/mc-mods/practical-tools
//     https://twitch.tv/neurodr0me

import mod.pyrate.bigtools.functions.MiningFunctions;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.function.Supplier;


public class DrillItem extends net.minecraft.world.item.Item
{
    private final Supplier<Item> myCrusher;
    private final Supplier<Item> myDigger;

    public DrillItem(ToolMaterial tier, TagKey<Block> tagBlock, Item.Properties props,
                     Supplier<Item> crusher, Supplier<Item> digger)
    {
        super(props.tool(tier, tagBlock, tier.attackDamageBonus(), tier.speed(), 0.0f));
        myCrusher = crusher;
        myDigger = digger;
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity)
    {
        MiningFunctions.doToolDrillBlock(stack, world, state, pos, entity);
        return super.mineBlock(stack, world, state, pos, entity);
    }
}