package mod.pyrate.bigtools.init;

import com.google.common.collect.ImmutableSet;
import mod.pyrate.bigtools.items.tools.*;
import mod.pyrate.bigtools.items.weapons.ValyrianSteelSwordItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Supplier;

public class ModMiningToolItems
{
    public static final DeferredRegister<Item> MOD_TOOLS = DeferredRegister.create(BuiltInRegistries.ITEM, mod.pyrate.bigtools.BigTools.MOD_ID);

    public static final int INITIAL_DAMAGE = -1;
    public static final double BASE_AVOID_DAMAGE_CHANCE = 0.05;
    public static final double GOLD_AVOID_DAMAGE_CHANCE_MULTIPLIER = 3.0;

    public static final String WOODEN_SHEARS_ID = "wooden_shears";
    public static final String WOODEN_CRUSHER_ID = "wooden_crusher";
    public static final String WOODEN_DIGGER_ID = "wooden_digger";
    public static final String WOODEN_LOGGER_ID = "wooden_logger";
    public static final String WOODEN_EXCAVATOR_ID = "wooden_excavator";

    public static final Supplier<Item> WOODEN_SHEARS = MOD_TOOLS.register(WOODEN_SHEARS_ID, () -> mkShears(ModTiers.WOOD_TIER, WOODEN_SHEARS_ID));
    public static final Supplier<Item> WOODEN_CRUSHER = MOD_TOOLS.register(WOODEN_CRUSHER_ID, () -> mkCrusher(ModTiers.WOOD_TIER, WOODEN_CRUSHER_ID));
    public static final Supplier<Item> WOODEN_DIGGER = MOD_TOOLS.register(WOODEN_DIGGER_ID, () -> mkDigger(ModTiers.WOOD_TIER, WOODEN_DIGGER_ID));
    public static final Supplier<Item> WOODEN_LOGGER = MOD_TOOLS.register(WOODEN_LOGGER_ID, () -> mkLogger(ModTiers.WOOD_TIER, WOODEN_LOGGER_ID));
    public static final Supplier<Item> WOODEN_EXCAVATOR = MOD_TOOLS.register(WOODEN_EXCAVATOR_ID, () -> mkExcavator(ModTiers.WOOD_TIER, WOODEN_EXCAVATOR_ID, WOODEN_CRUSHER, WOODEN_DIGGER));
    public static Set<Supplier<Item>> WOODEN_TOOLS = Set.of(WOODEN_SHEARS, WOODEN_CRUSHER, WOODEN_DIGGER, WOODEN_LOGGER, WOODEN_EXCAVATOR);

    public static final String STONE_SHEARS_ID = "stone_shears";
    public static final String STONE_CRUSHER_ID = "stone_crusher";
    public static final String STONE_DIGGER_ID = "stone_digger";
    public static final String STONE_LOGGER_ID = "stone_logger";
    public static final String STONE_EXCAVATOR_ID = "stone_excavator";

    public static final Supplier<Item> STONE_SHEARS = MOD_TOOLS.register(STONE_SHEARS_ID, () -> mkShears(ModTiers.STONE_TIER, STONE_SHEARS_ID));
    public static final Supplier<Item> STONE_CRUSHER = MOD_TOOLS.register(STONE_CRUSHER_ID, () -> mkCrusher(ModTiers.STONE_TIER, STONE_CRUSHER_ID));
    public static final Supplier<Item> STONE_DIGGER = MOD_TOOLS.register(STONE_DIGGER_ID, () -> mkDigger(ModTiers.STONE_TIER, STONE_DIGGER_ID));
    public static final Supplier<Item> STONE_LOGGER = MOD_TOOLS.register(STONE_LOGGER_ID, () -> mkLogger(ModTiers.STONE_TIER, STONE_LOGGER_ID));
    public static final Supplier<Item> STONE_EXCAVATOR = MOD_TOOLS.register(STONE_EXCAVATOR_ID, () -> mkExcavator(ModTiers.STONE_TIER, STONE_EXCAVATOR_ID, STONE_CRUSHER, STONE_DIGGER));
    public static Set<Supplier<Item>> STONE_TOOLS = Set.of(STONE_SHEARS, STONE_CRUSHER, STONE_DIGGER, STONE_LOGGER, STONE_EXCAVATOR);

    public static final String IRON_SHEARS_ID = "iron_shears";
    public static final String IRON_CRUSHER_ID = "iron_crusher";
    public static final String IRON_DIGGER_ID = "iron_digger";
    public static final String IRON_LOGGER_ID = "iron_logger";
    public static final String IRON_EXCAVATOR_ID = "iron_excavator";
    public static final Supplier<Item> IRON_SHEARS = MOD_TOOLS.register(IRON_SHEARS_ID, () -> mkShears(ModTiers.IRON_TIER, IRON_SHEARS_ID));
    public static final Supplier<Item> IRON_CRUSHER = MOD_TOOLS.register(IRON_CRUSHER_ID, () -> mkCrusher(ModTiers.IRON_TIER, IRON_CRUSHER_ID));
    public static final Supplier<Item> IRON_DIGGER = MOD_TOOLS.register(IRON_DIGGER_ID, () -> mkDigger(ModTiers.IRON_TIER, IRON_DIGGER_ID));
    public static final Supplier<Item> IRON_LOGGER = MOD_TOOLS.register(IRON_LOGGER_ID, () -> mkLogger(ModTiers.IRON_TIER, IRON_LOGGER_ID));
    public static final Supplier<Item> IRON_EXCAVATOR = MOD_TOOLS.register(IRON_EXCAVATOR_ID, () -> mkExcavator(ModTiers.IRON_TIER, IRON_EXCAVATOR_ID, IRON_CRUSHER, IRON_DIGGER));
    public static Set<Supplier<Item>> IRON_TOOLS = Set.of(IRON_SHEARS, IRON_CRUSHER, IRON_DIGGER, IRON_LOGGER, IRON_EXCAVATOR);


    public static final String GOLD_SHEARS_ID = "gold_shears";
    public static final String GOLD_CRUSHER_ID = "gold_crusher";
    public static final String GOLD_DIGGER_ID = "gold_digger";
    public static final String GOLD_LOGGER_ID = "gold_logger";
    public static final String GOLD_EXCAVATOR_ID = "gold_excavator";
    public static final Supplier<Item> GOLD_SHEARS = MOD_TOOLS.register(GOLD_SHEARS_ID, () -> mkShears(ModTiers.GOLD_TIER, GOLD_SHEARS_ID));
    public static final Supplier<Item> GOLD_CRUSHER = MOD_TOOLS.register(GOLD_CRUSHER_ID, () -> mkCrusher(ModTiers.GOLD_TIER, GOLD_CRUSHER_ID));
    public static final Supplier<Item> GOLD_DIGGER = MOD_TOOLS.register(GOLD_DIGGER_ID, () -> mkDigger(ModTiers.GOLD_TIER, GOLD_DIGGER_ID));
    public static final Supplier<Item> GOLD_LOGGER = MOD_TOOLS.register(GOLD_LOGGER_ID, () -> mkLogger(ModTiers.GOLD_TIER, GOLD_LOGGER_ID));
    public static final Supplier<Item> GOLD_EXCAVATOR = MOD_TOOLS.register(GOLD_EXCAVATOR_ID, () -> mkExcavator(ModTiers.GOLD_TIER, GOLD_EXCAVATOR_ID, GOLD_CRUSHER, GOLD_DIGGER));
    public static Set<Supplier<Item>> GOLD_TOOLS = Set.of(GOLD_SHEARS, GOLD_CRUSHER, GOLD_DIGGER, GOLD_LOGGER, GOLD_EXCAVATOR);


    public static final String DIAMOND_SHEARS_ID = "diamond_shears";
    public static final String DIAMOND_CRUSHER_ID = "diamond_crusher";
    public static final String DIAMOND_DIGGER_ID = "diamond_digger";
    public static final String DIAMOND_LOGGER_ID = "diamond_logger";
    public static final String DIAMOND_EXCAVATOR_ID = "diamond_excavator";
    public static final String DIAMOND_DRILL_ID = "diamond_drill";
    public static final String DIAMOND_BORER_ID = "diamond_borer";
    public static final String DIAMOND_TUNNELER_ID = "diamond_tunneler";
    public static final Supplier<Item> DIAMOND_SHEARS = MOD_TOOLS.register(DIAMOND_SHEARS_ID, () -> mkShears(ModTiers.DIAMOND_TIER, DIAMOND_SHEARS_ID));
    public static final Supplier<Item> DIAMOND_CRUSHER = MOD_TOOLS.register(DIAMOND_CRUSHER_ID, () -> mkCrusher(ModTiers.DIAMOND_TIER, DIAMOND_CRUSHER_ID));
    public static final Supplier<Item> DIAMOND_DIGGER = MOD_TOOLS.register(DIAMOND_DIGGER_ID, () -> mkDigger(ModTiers.DIAMOND_TIER, DIAMOND_DIGGER_ID));
    public static final Supplier<Item> DIAMOND_LOGGER = MOD_TOOLS.register(DIAMOND_LOGGER_ID, () -> mkLogger(ModTiers.DIAMOND_TIER, DIAMOND_LOGGER_ID));
    public static final Supplier<Item> DIAMOND_EXCAVATOR = MOD_TOOLS.register(DIAMOND_EXCAVATOR_ID, () -> mkExcavator(ModTiers.DIAMOND_TIER, DIAMOND_EXCAVATOR_ID, DIAMOND_CRUSHER, DIAMOND_DIGGER));
    public static final Supplier<Item> DIAMOND_DRILL = MOD_TOOLS.register(DIAMOND_DRILL_ID, () -> mkDrill(ModTiers.DIAMOND_TIER, DIAMOND_DRILL_ID, DIAMOND_CRUSHER, DIAMOND_DIGGER));
    public static final Supplier<Item> DIAMOND_BORER = MOD_TOOLS.register(DIAMOND_BORER_ID, () -> mkBorer(ModTiers.DIAMOND_TIER, DIAMOND_BORER_ID, DIAMOND_CRUSHER, DIAMOND_DIGGER));
    public static final Supplier<Item> DIAMOND_TUNNELER = MOD_TOOLS.register(DIAMOND_TUNNELER_ID, () -> mkTunneler(ModTiers.DIAMOND_TIER, DIAMOND_TUNNELER_ID, DIAMOND_CRUSHER, DIAMOND_DIGGER));
    public static Set<Supplier<Item>> DIAMOND_TOOLS = Set.of(DIAMOND_SHEARS, DIAMOND_CRUSHER, DIAMOND_DIGGER, DIAMOND_LOGGER, DIAMOND_EXCAVATOR, DIAMOND_DRILL, DIAMOND_BORER, DIAMOND_TUNNELER);


    public static final String OBSIDIAN_SHEARS_ID = "obsidian_shears";
    public static final String OBSIDIAN_CRUSHER_ID = "obsidian_crusher";
    public static final String OBSIDIAN_DIGGER_ID = "obsidian_digger";
    public static final String OBSIDIAN_LOGGER_ID = "obsidian_logger";
    public static final String OBSIDIAN_EXCAVATOR_ID = "obsidian_excavator";
    public static final Supplier<Item> OBSIDIAN_SHEARS = MOD_TOOLS.register(OBSIDIAN_SHEARS_ID, () -> mkShears(ModTiers.OBSIDIAN_TIER, OBSIDIAN_SHEARS_ID));
    public static final Supplier<Item> OBSIDIAN_CRUSHER = MOD_TOOLS.register(OBSIDIAN_CRUSHER_ID, () -> mkCrusher(ModTiers.OBSIDIAN_TIER, OBSIDIAN_CRUSHER_ID));
    public static final Supplier<Item> OBSIDIAN_DIGGER = MOD_TOOLS.register(OBSIDIAN_DIGGER_ID, () -> mkDigger(ModTiers.OBSIDIAN_TIER, OBSIDIAN_DIGGER_ID));
    public static final Supplier<Item> OBSIDIAN_LOGGER = MOD_TOOLS.register(OBSIDIAN_LOGGER_ID, () -> mkLogger(ModTiers.OBSIDIAN_TIER, OBSIDIAN_LOGGER_ID));
    public static final Supplier<Item> OBSIDIAN_EXCAVATOR = MOD_TOOLS.register(OBSIDIAN_EXCAVATOR_ID, () -> mkExcavator(ModTiers.OBSIDIAN_TIER, OBSIDIAN_EXCAVATOR_ID, OBSIDIAN_CRUSHER, OBSIDIAN_DIGGER));
    public static Set<Supplier<Item>> OBSIDIAN_TOOLS = Set.of(OBSIDIAN_SHEARS, OBSIDIAN_CRUSHER, OBSIDIAN_DIGGER, OBSIDIAN_LOGGER, OBSIDIAN_EXCAVATOR);


    public static final String VALYRIAN_STEEL_SHEARS_ID = "valyrian_steel_shears";
    public static final String VALYRIAN_STEEL_CRUSHER_ID = "valyrian_steel_crusher";
    public static final String VALYRIAN_STEEL_DIGGER_ID = "valyrian_steel_digger";
    public static final String VALYRIAN_STEEL_LOGGER_ID = "valyrian_steel_logger";
    public static final String VALYRIAN_STEEL_DRILL_ID = "valyrian_steel_drill";
    public static final String VALYRIAN_STEEL_EXCAVATOR_ID = "valyrian_steel_excavator";
    public static final String VALYRIAN_STEEL_BORER_ID = "valyrian_steel_borer";
    public static final String VALYRIAN_STEEL_TUNNELER_ID = "valyrian_steel_tunneler";
    public static final Supplier<Item> VALYRIAN_STEEL_SHEARS = MOD_TOOLS.register(VALYRIAN_STEEL_SHEARS_ID, () -> mkShears(ModTiers.VALYRIAN_TIER, VALYRIAN_STEEL_SHEARS_ID));
    public static final Supplier<Item> VALYRIAN_STEEL_CRUSHER = MOD_TOOLS.register(VALYRIAN_STEEL_CRUSHER_ID, () -> mkCrusher(ModTiers.VALYRIAN_TIER, VALYRIAN_STEEL_CRUSHER_ID));
    public static final Supplier<Item> VALYRIAN_STEEL_DIGGER = MOD_TOOLS.register(VALYRIAN_STEEL_DIGGER_ID, () -> mkDigger(ModTiers.VALYRIAN_TIER, VALYRIAN_STEEL_DIGGER_ID));
    public static final Supplier<Item> VALYRIAN_STEEL_LOGGER = MOD_TOOLS.register(VALYRIAN_STEEL_LOGGER_ID, () -> mkLogger(ModTiers.VALYRIAN_TIER, VALYRIAN_STEEL_LOGGER_ID));
    public static final Supplier<Item> VALYRIAN_STEEL_DRILL = MOD_TOOLS.register(VALYRIAN_STEEL_DRILL_ID, () -> mkDrill(ModTiers.VALYRIAN_TIER, VALYRIAN_STEEL_DRILL_ID, VALYRIAN_STEEL_CRUSHER, VALYRIAN_STEEL_DIGGER));
    public static final Supplier<Item> VALYRIAN_STEEL_EXCAVATOR = MOD_TOOLS.register(VALYRIAN_STEEL_EXCAVATOR_ID, () -> mkExcavator(ModTiers.VALYRIAN_TIER, VALYRIAN_STEEL_EXCAVATOR_ID, VALYRIAN_STEEL_CRUSHER, VALYRIAN_STEEL_DIGGER));
    public static final Supplier<Item> VALYRIAN_STEEL_BORER = MOD_TOOLS.register(VALYRIAN_STEEL_BORER_ID, () -> mkBorer(ModTiers.VALYRIAN_TIER, VALYRIAN_STEEL_BORER_ID, VALYRIAN_STEEL_CRUSHER, VALYRIAN_STEEL_DIGGER));
    public static final Supplier<Item> VALYRIAN_STEEL_TUNNELER = MOD_TOOLS.register(VALYRIAN_STEEL_TUNNELER_ID, () -> mkTunneler(ModTiers.VALYRIAN_TIER, VALYRIAN_STEEL_TUNNELER_ID, VALYRIAN_STEEL_CRUSHER, VALYRIAN_STEEL_DIGGER));
    public static Set<Supplier<Item>> VALYRIAN_STEEL_TOOLS = Set.of(VALYRIAN_STEEL_SHEARS, VALYRIAN_STEEL_CRUSHER, VALYRIAN_STEEL_DIGGER, VALYRIAN_STEEL_LOGGER, VALYRIAN_STEEL_EXCAVATOR, VALYRIAN_STEEL_BORER, VALYRIAN_STEEL_TUNNELER);


    public static Set<Supplier<Item>> CUSTOM_PICKAXES = ImmutableSet.of(WOODEN_CRUSHER, STONE_CRUSHER, IRON_CRUSHER,
            GOLD_CRUSHER, DIAMOND_CRUSHER, OBSIDIAN_CRUSHER, VALYRIAN_STEEL_CRUSHER,
            WOODEN_EXCAVATOR, STONE_EXCAVATOR, IRON_EXCAVATOR,
            GOLD_EXCAVATOR, DIAMOND_EXCAVATOR, OBSIDIAN_EXCAVATOR, VALYRIAN_STEEL_DRILL, VALYRIAN_STEEL_EXCAVATOR,
            DIAMOND_DRILL, DIAMOND_BORER, VALYRIAN_STEEL_BORER, DIAMOND_TUNNELER, VALYRIAN_STEEL_TUNNELER);
    public static Set<Supplier<Item>> CUSTOM_SHOVELS = ImmutableSet.of(WOODEN_DIGGER, STONE_DIGGER, IRON_DIGGER,
            GOLD_DIGGER, DIAMOND_DIGGER, OBSIDIAN_DIGGER, VALYRIAN_STEEL_DIGGER,
            WOODEN_EXCAVATOR, STONE_EXCAVATOR, IRON_EXCAVATOR,
            GOLD_EXCAVATOR, DIAMOND_EXCAVATOR, OBSIDIAN_EXCAVATOR, VALYRIAN_STEEL_DRILL, VALYRIAN_STEEL_EXCAVATOR,
            DIAMOND_DRILL, DIAMOND_BORER, VALYRIAN_STEEL_BORER, DIAMOND_TUNNELER, VALYRIAN_STEEL_TUNNELER);
    public static Set<Supplier<Item>> CUSTOM_AXES = ImmutableSet.of(WOODEN_LOGGER, STONE_LOGGER, IRON_LOGGER,
            GOLD_LOGGER, DIAMOND_LOGGER, OBSIDIAN_LOGGER, VALYRIAN_STEEL_LOGGER);
    public static Set<Supplier<Item>> CUSTOM_SHEARS = ImmutableSet.of(WOODEN_SHEARS, STONE_SHEARS, IRON_SHEARS,
            GOLD_SHEARS, DIAMOND_SHEARS, OBSIDIAN_SHEARS, VALYRIAN_STEEL_SHEARS);




    private static Item mkDigger(ToolMaterial tier, String id)
    {
        return new DiggerItem(tier, BlockTags.MINEABLE_WITH_SHOVEL, ModItems.basicItemProps(id));
    }

    private static Item mkCrusher(ToolMaterial tier, String id)
    {
        return new CrusherItem(tier, BlockTags.MINEABLE_WITH_PICKAXE, ModItems.basicItemProps(id));
    }

    private static Item mkLogger(ToolMaterial tier, String id)
    {
        return new LoggerItem(tier, BlockTags.MINEABLE_WITH_AXE, ModItems.basicItemProps(id));
    }

    private static Item mkShears(ToolMaterial tier, String id)
    {
        return new ShearItem(ModItems.basicItemProps(id), tier);
    }

    private static Item mkDrill(ToolMaterial tier, String id, Supplier<Item> crusherTier, Supplier<Item> diggerTier)
    {
        Identifier excItems = Identifier.fromNamespaceAndPath(mod.pyrate.bigtools.BigTools.MOD_ID, "mineable/excavator");
        TagKey<Block> excTags = BlockTags.create(excItems);
        return new DrillItem(tier, excTags, ModItems.basicItemProps(id), crusherTier, diggerTier);
    }

    private static Item mkExcavator(ToolMaterial tier, String id, Supplier<Item> crusherTier, Supplier<Item> diggerTier)
    {
        Identifier excItems = Identifier.fromNamespaceAndPath(mod.pyrate.bigtools.BigTools.MOD_ID, "mineable/excavator");
        TagKey<Block> excTags = BlockTags.create(excItems);
        return new ExcavatorItem(tier, excTags, ModItems.basicItemProps(id), crusherTier, diggerTier);
    }

    private static Item mkBorer(ToolMaterial tier, String id, Supplier<Item> crusherTier, Supplier<Item> diggerTier) {
        Identifier excItems = Identifier.fromNamespaceAndPath(mod.pyrate.bigtools.BigTools.MOD_ID, "mineable/excavator");
        TagKey<Block> excTags = BlockTags.create(excItems);
        return new BorerItem(tier, excTags, ModItems.basicItemProps(id), crusherTier, diggerTier);
    }

    private static Item mkTunneler(ToolMaterial tier, String id, Supplier<Item> crusherTier, Supplier<Item> diggerTier) {
        Identifier excItems = Identifier.fromNamespaceAndPath(mod.pyrate.bigtools.BigTools.MOD_ID, "mineable/excavator");
        TagKey<Block> excTags = BlockTags.create(excItems);
        return new TunnelerItem(tier, excTags, ModItems.basicItemProps(id), crusherTier, diggerTier);
    }
}
