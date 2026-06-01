package mod.pyrate.bigtools.init;

import mod.pyrate.bigtools.BigTools;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;

import mod.pyrate.bigtools.block.DiamondHopperBlock;
import mod.pyrate.bigtools.blockentity.DiamondHopperBlockEntity;
import mod.pyrate.bigtools.block.DirtStairsBlock;
import mod.pyrate.bigtools.blockentity.DirtStairsBlockEntity;
import mod.pyrate.bigtools.block.GrassStairsBlock;
import mod.pyrate.bigtools.blockentity.GrassStairsBlockEntity;
import net.neoforged.neoforge.registries.NeoForgeRegistries;


import java.util.function.Supplier;

public class ModBlocks
{
    public static ResourceKey<Block> getBlockKey(String block_id) { return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(BigTools.MOD_ID, block_id)); }

    public static BlockItem.Properties basicBlockItemProps(String id)
    {
        return new BlockItem.Properties().setId(ModItems.getItemKey(id));
    }

    public static final DeferredRegister<BlockEntityType<?>> MOD_BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BigTools.MOD_ID);
    public static final DeferredRegister<Block> MOD_BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, BigTools.MOD_ID);
    public static final DeferredRegister<Item> MOD_BLOCK_ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, BigTools.MOD_ID);

    public static final String DIAMOND_HOPPER_ID = "diamond_hopper";
    public static final Supplier<Block> DIAMOND_HOPPER_BLOCK = MOD_BLOCKS.register(DIAMOND_HOPPER_ID, () -> new DiamondHopperBlock(BlockBehaviour.Properties.of().setId(getBlockKey(DIAMOND_HOPPER_ID)).sound(SoundType.GLASS).mapColor(MapColor.COLOR_LIGHT_BLUE).strength(1.0F, 6000F)));
    public static Supplier<BlockItem> DIAMOND_HOPPER_ITEM = MOD_BLOCK_ITEMS.register(DIAMOND_HOPPER_ID, () -> new BlockItem(DIAMOND_HOPPER_BLOCK.get(), basicBlockItemProps(DIAMOND_HOPPER_ID)));
    public static final Supplier<BlockEntityType<DiamondHopperBlockEntity>> DIAMOND_HOPPER_ENTITY = MOD_BLOCK_ENTITIES.register(DIAMOND_HOPPER_ID, () -> new BlockEntityType<>(DiamondHopperBlockEntity::new, ModBlocks.DIAMOND_HOPPER_BLOCK.get()));

    public static final String DIRT_STAIRS_ID = "dirt_stairs";
    public static final Supplier<Block> DIRT_STAIRS_BLOCK = MOD_BLOCKS.register(DIRT_STAIRS_ID, () -> new DirtStairsBlock(Blocks.DIRT, DIRT_STAIRS_ID));
    public static Supplier<BlockItem> DIRT_STAIRS_ITEM = MOD_BLOCK_ITEMS.register(DIRT_STAIRS_ID, () -> new BlockItem(DIRT_STAIRS_BLOCK.get(), basicBlockItemProps(DIRT_STAIRS_ID)));
    public static final Supplier<BlockEntityType<DirtStairsBlockEntity>> DIRT_STAIRS_ENTITY = MOD_BLOCK_ENTITIES.register(DIRT_STAIRS_ID, () -> new BlockEntityType<>(DirtStairsBlockEntity::new, ModBlocks.DIRT_STAIRS_BLOCK.get()));

    public static final String GRASS_STAIRS_ID = "grass_stairs";
    public static final Supplier<Block> GRASS_STAIRS_BLOCK = MOD_BLOCKS.register(GRASS_STAIRS_ID, () -> new GrassStairsBlock(Blocks.GRASS_BLOCK, GRASS_STAIRS_ID));
    public static Supplier<BlockItem> GRASS_STAIRS_ITEM = MOD_BLOCK_ITEMS.register(GRASS_STAIRS_ID, () -> new BlockItem(GRASS_STAIRS_BLOCK.get(), basicBlockItemProps(GRASS_STAIRS_ID)));
    public static final Supplier<BlockEntityType<GrassStairsBlockEntity>> GRASS_STAIRS_ENTITY = MOD_BLOCK_ENTITIES.register(GRASS_STAIRS_ID, () -> new BlockEntityType<>(GrassStairsBlockEntity::new, ModBlocks.GRASS_STAIRS_BLOCK.get()));



}
