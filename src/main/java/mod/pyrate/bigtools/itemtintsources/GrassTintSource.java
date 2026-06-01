package mod.pyrate.bigtools.itemtintsources;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GrassColor;

public record GrassTintSource(int defaultColor) implements ItemTintSource
{
    // The map codec to register
    public static final MapCodec<GrassTintSource> MAP_CODEC = ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default")
            .xmap(GrassTintSource::new, GrassTintSource::defaultColor);

    public GrassTintSource(int defaultColor)
    {
        // Make sure the passed in color is opaque
        this.defaultColor = ARGB.opaque(defaultColor);
    }

    @Override
    public int calculate(ItemStack stack, ClientLevel level, LivingEntity entity)
    {
        return GrassColor.getDefaultColor();
    }

    @Override
    public MapCodec<GrassTintSource> type()
    {
        return MAP_CODEC;
    }
}
