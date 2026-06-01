package mod.pyrate.bigtools.init;

import mod.pyrate.bigtools.BigTools;
import mod.pyrate.bigtools.items.weapons.ValyrianSteelSwordItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModWeapons
{
    public static final DeferredRegister<Item> MOD_WEAPONS = DeferredRegister.create(BuiltInRegistries.ITEM, BigTools.MOD_ID);

    public static final String VALYRIAN_STEEL_SWORD_ID = "valyrian_steel_sword";
    public static Supplier<Item> VALYRIAN_STEEL_SWORD =
            MOD_WEAPONS.register(VALYRIAN_STEEL_SWORD_ID, () ->
                    new ValyrianSteelSwordItem(ModTiers.VALYRIAN_TIER, ModItems.basicItemProps(VALYRIAN_STEEL_SWORD_ID)));

}
