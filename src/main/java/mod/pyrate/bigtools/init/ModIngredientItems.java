package mod.pyrate.bigtools.init;

import mod.pyrate.bigtools.BigTools;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModIngredientItems
{
    public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, mod.pyrate.bigtools.BigTools.MOD_ID);

    public static final String BLOCK_OF_WOOD_ID = "polished_wooden_block";
    public static final Supplier<Item> BLOCK_OF_WOOD = MOD_ITEMS.register(BLOCK_OF_WOOD_ID, () -> ModItems.basicItem(BLOCK_OF_WOOD_ID));
    public static final String BLOCK_OF_HARDENED_STONE_ID = "hardened_stone_block";
    public static final Supplier<Item> BLOCK_OF_HARDENED_STONE = MOD_ITEMS.register(BLOCK_OF_HARDENED_STONE_ID, () -> ModItems.basicItem(BLOCK_OF_HARDENED_STONE_ID));
    public static final String BLOCK_OF_OBSIDIAN_ID = "diamond_flecked_obsidian";
    public static Supplier<Item> BLOCK_OF_OBSIDIAN = MOD_ITEMS.register(BLOCK_OF_OBSIDIAN_ID, () -> ModItems.basicItem(BLOCK_OF_OBSIDIAN_ID));
    public static final String REDSTONE_POWER_CELL_ID = "redstone_power_cell";
    public static Supplier<Item> REDSTONE_POWER_CELL = MOD_ITEMS.register(REDSTONE_POWER_CELL_ID, () -> ModItems.basicItem(REDSTONE_POWER_CELL_ID));
    public static final String VALYRIAN_STEEL_INGOT_ID = "valyrian_steel_ingot";
    public static Supplier<Item> VALYRIAN_STEEL_INGOT = MOD_ITEMS.register(VALYRIAN_STEEL_INGOT_ID, () -> ModItems.basicItem(VALYRIAN_STEEL_INGOT_ID));

}
