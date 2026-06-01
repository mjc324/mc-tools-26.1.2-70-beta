package mod.pyrate.bigtools.items.weapons;

import mod.pyrate.bigtools.init.ModTiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Weapon;

public class ValyrianSteelSwordItem extends Item
{
    public ValyrianSteelSwordItem(ToolMaterial tier, Properties props)
    {
        super(props.sword(tier, tier.attackDamageBonus(), tier.speed()));
    }
}
