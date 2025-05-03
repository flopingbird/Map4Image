package com.flopingbird.map4image.component;

import com.flopingbird.map4image.Map4Image;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponentType {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Map4Image.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WIDTH = register("width",
            builder -> builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> HEIGHT = register("height",
            builder -> builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> PLACED_PARENT = register("placed_parent",
            builder -> builder.persistent(BlockPos.CODEC));

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}