package mod.pyrate.bigtools.init;

import mod.pyrate.bigtools.BigTools;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

public class ModItems
{
    public static ResourceKey<Item> getItemKey(String item_id) { return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BigTools.MOD_ID, item_id)); }

    public static Item.Properties basicItemProps(String id)
    {
        return new Item.Properties().setId(getItemKey(id));
    }

    public static Item basicItem(String id)
    {
        return new Item(basicItemProps(id));
    }
}
