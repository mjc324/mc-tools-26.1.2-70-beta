package mod.pyrate.bigtools;

import mod.pyrate.bigtools.init.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(mod.pyrate.bigtools.BigTools.MOD_ID)
public class BigTools
{
    public static final String MOD_ID = "bigtools";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public BigTools(IEventBus modBus)
    {
        LOGGER.info("Pyrate BigTools Universal constructor initialized.");

        LOGGER.info("Pyrate BigTools registering Ingredient Items.");
        ModIngredientItems.MOD_ITEMS.register(modBus);

        LOGGER.info("Pyrate BigTools registering Mining Tool Items.");
        ModMiningToolItems.MOD_TOOLS.register(modBus);

        LOGGER.info("Pyrate BigTools registering Utility Tool Items.");
        ModUtilityToolItems.MOD_ITEMS.register(modBus);

        LOGGER.info("Pyrate BigTools registering Weapon Items.");
        ModWeapons.MOD_WEAPONS.register(modBus);

        LOGGER.info("Pyrate BigTools registering Blocks.");
        ModBlocks.MOD_BLOCKS.register(modBus);
        LOGGER.info("Pyrate BigTools registering Block Items.");
        ModBlocks.MOD_BLOCK_ITEMS.register(modBus);
        LOGGER.info("Pyrate BigTools registering Block Entities.");
        ModBlocks.MOD_BLOCK_ENTITIES.register(modBus);

        LOGGER.info("Pyrate BigTools Universal constructor completed.");

    }
}
