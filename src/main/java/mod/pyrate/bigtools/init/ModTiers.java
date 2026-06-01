package mod.pyrate.bigtools.init;

import mod.pyrate.bigtools.BigTools;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ModTiers
{
    public static final ToolMaterial WOOD_TIER = new ToolMaterial(ToolMaterial.WOOD.incorrectBlocksForDrops(),
            ToolMaterial.WOOD.durability(),
            ToolMaterial.WOOD.speed(),
            ToolMaterial.WOOD.attackDamageBonus(),
            ToolMaterial.WOOD.enchantmentValue(),
            ToolMaterial.WOOD.repairItems());
    public static final ToolMaterial STONE_TIER = new ToolMaterial(ToolMaterial.STONE.incorrectBlocksForDrops(),
            ToolMaterial.STONE.durability(),
            ToolMaterial.STONE.speed(),
            ToolMaterial.STONE.attackDamageBonus(),
            ToolMaterial.STONE.enchantmentValue(),
            ToolMaterial.STONE.repairItems());
    public static final ToolMaterial IRON_TIER = new ToolMaterial(ToolMaterial.IRON.incorrectBlocksForDrops(),
            ToolMaterial.IRON.durability(),
            ToolMaterial.IRON.speed(),
            ToolMaterial.IRON.attackDamageBonus(),
            ToolMaterial.IRON.enchantmentValue(),
            ToolMaterial.IRON.repairItems());
    public static final ToolMaterial GOLD_TIER = new ToolMaterial(ToolMaterial.GOLD.incorrectBlocksForDrops(),
            ToolMaterial.GOLD.durability(),
            ToolMaterial.GOLD.speed(),
            ToolMaterial.GOLD.attackDamageBonus(),
            ToolMaterial.GOLD.enchantmentValue(),
            ToolMaterial.GOLD.repairItems());
    public static final ToolMaterial DIAMOND_TIER = new ToolMaterial(ToolMaterial.DIAMOND.incorrectBlocksForDrops(),
            ToolMaterial.DIAMOND.durability(),
            ToolMaterial.DIAMOND.speed(),
            ToolMaterial.DIAMOND.attackDamageBonus(),
            ToolMaterial.DIAMOND.enchantmentValue(),
            ToolMaterial.DIAMOND.repairItems());


    public static final int OBSIDIAN_DURABILITY = 3500;
    public static final float OBSIDIAN_SPEED = 12;
    public static final float OBSIDIAN_DAMAGE = 5;
    public static final int OBSIDIAN_ENCHANTABILITY = 15;
    public static final TagKey<Item> OBSIDIAN_TOOL_MATERIALS = ItemTags.create(Identifier.fromNamespaceAndPath("minecraft","obsidian"));
    public static final ToolMaterial OBSIDIAN_TIER = new ToolMaterial(ToolMaterial.NETHERITE.incorrectBlocksForDrops(),
            OBSIDIAN_DURABILITY,
            OBSIDIAN_SPEED,
            OBSIDIAN_DAMAGE,
            OBSIDIAN_ENCHANTABILITY,
            OBSIDIAN_TOOL_MATERIALS);


    public static final int REDSTONE_DURABILITY = 750;
    public static final float REDSTONE_SPEED = 12;
    public static final float REDSTONE_DAMAGE = 3;
    public static final int REDSTONE_ENCHANTABILITY = 25;
    public static final TagKey<Item> REDSTONE_TOOL_MATERIALS = ItemTags.create(Identifier.fromNamespaceAndPath("minecraft","redstone_block"));
    public static final ToolMaterial REDSTONE_TIER = new ToolMaterial(ToolMaterial.NETHERITE.incorrectBlocksForDrops(),
            REDSTONE_DURABILITY,
            REDSTONE_SPEED,
            REDSTONE_DAMAGE,
            REDSTONE_ENCHANTABILITY,
            REDSTONE_TOOL_MATERIALS);


    public static final int VALYRIAN_DURABILITY = 6000;
    public static final float VALYRIAN_SPEED = 14;
    public static final float VALYRIAN_DAMAGE = 7;
    public static final int VALYRIAN_ENCHANTABILITY = 30;
    public static final TagKey<Item> VALYRIAN_TOOL_MATERIALS = ItemTags.create(Identifier.fromNamespaceAndPath(BigTools.MOD_ID, ModIngredientItems.VALYRIAN_STEEL_INGOT_ID));
    public static final ToolMaterial VALYRIAN_TIER = new ToolMaterial(ToolMaterial.NETHERITE.incorrectBlocksForDrops(),
            VALYRIAN_DURABILITY,
            VALYRIAN_SPEED,
            VALYRIAN_DAMAGE,
            VALYRIAN_ENCHANTABILITY,
            VALYRIAN_TOOL_MATERIALS);
}
