package com.flopingbird.map4image;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = Map4Image.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue MAX_WIDTH = BUILDER
            .comment("Max width for map art in item frame size")
            .defineInRange("maxWidth", 32, 1, 320);
    private static final ModConfigSpec.IntValue MAX_HEIGHT = BUILDER
            .comment("Max height for map art in item frame size")
            .defineInRange("maxHeight", 32, 1, 320);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int maxWidth;
    public static int maxHeight;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        maxWidth = MAX_WIDTH.get();
        maxHeight = MAX_HEIGHT.get();
    }
}