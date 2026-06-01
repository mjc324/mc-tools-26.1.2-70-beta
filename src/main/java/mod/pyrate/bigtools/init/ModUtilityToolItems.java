package mod.pyrate.bigtools.init;

//import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import mod.pyrate.bigtools.BigTools;
import mod.pyrate.bigtools.items.util_tools.Simple_Path_Roller;
import mod.pyrate.bigtools.items.util_tools.Simple_Wall_Roller;
import mod.pyrate.bigtools.items.util_tools.SpaceCloneTool;
import mod.pyrate.bigtools.items.util_tools.Stairmaster;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModUtilityToolItems
{
    public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, BigTools.MOD_ID);

    public static final String SIMPLE_THIN_PATH_ROLLER_ID = "simple_thin_path_roller";
    public static final Supplier<Item> SIMPLE_THIN_PATH_ROLLER = MOD_ITEMS.register(SIMPLE_THIN_PATH_ROLLER_ID, () -> new Simple_Path_Roller(ModItems.basicItemProps(SIMPLE_THIN_PATH_ROLLER_ID), 1, Simple_Path_Roller.DEFAULT_PATH_LENGTH));
    public static final String SIMPLE_PATH_ROLLER_ID = "simple_path_roller";
    public static final Supplier<Item> SIMPLE_PATH_ROLLER = MOD_ITEMS.register(SIMPLE_PATH_ROLLER_ID, () -> new Simple_Path_Roller(ModItems.basicItemProps(SIMPLE_PATH_ROLLER_ID), Simple_Path_Roller.DEFAULT_PATH_WIDTH, Simple_Path_Roller.DEFAULT_PATH_LENGTH));
    public static final String SIMPLE_WIDE_PATH_ROLLER_ID = "simple_wide_path_roller";
    public static final Supplier<Item> SIMPLE_WIDE_PATH_ROLLER = MOD_ITEMS.register(SIMPLE_WIDE_PATH_ROLLER_ID, () -> new Simple_Path_Roller(ModItems.basicItemProps(SIMPLE_WIDE_PATH_ROLLER_ID), 5, Simple_Path_Roller.DEFAULT_PATH_LENGTH));

    public static final String SIMPLE_WALL_ROLLER_ID = "wall_roller";
    public static final Supplier<Item> SIMPLE_WALL_ROLLER = MOD_ITEMS.register(SIMPLE_WALL_ROLLER_ID, () -> new Simple_Wall_Roller(ModItems.basicItemProps(SIMPLE_WALL_ROLLER_ID), 3, Simple_Path_Roller.DEFAULT_PATH_LENGTH));

    public static final String STAIRMASTER_ID = "stairmaster";
    public static final Supplier<Item> STAIRMASTER = MOD_ITEMS.register(STAIRMASTER_ID, () -> new Stairmaster(ModItems.basicItemProps(STAIRMASTER_ID), 1, Stairmaster.DEFAULT_PATH_LENGTH));

    public static final String CLONE_TOOL_ID = "clone_spacer";
    public static final Supplier<Item> CLONE_TOOL = MOD_ITEMS.register(CLONE_TOOL_ID, () -> new SpaceCloneTool(ModItems.basicItemProps(CLONE_TOOL_ID), SpaceCloneTool.INITIAL_SPACING, SpaceCloneTool.MAX_SPACING));

}
