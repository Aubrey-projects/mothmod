package com.aubreyjensen.mothmod.item;

import com.aubreyjensen.mothmod.MothMod;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ModItems {

    // These call the registerItem method to create a moth_wing item and a moth_spawn_egg item
    public static final Item MOTH_WING = registerItem("moth_wing", Item::new);
    public static final Item MOTH_SPAWN_EGG = registerItem("moth_spawn_egg", Item::new);

    /*
    This method creates, configures, and registers a new item in Minecraft
    so that we don't need to create the identifier, key, and properties
    every time we want to make a new item
    */
    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        // This combines the mod's namespace with the item's name into a single identifier (e.g. mothmod:moth_wing)
        Identifier identifier = Identifier.fromNamespaceAndPath(MothMod.MOD_ID, name);

        // Creates a key for the item inside Minecraft's item registry
        ResourceKey<@NotNull Item> key = ResourceKey.create(Registries.ITEM, identifier);

        // Assigns the key to the item properties
        Item.Properties properties = new Item.Properties().setId(key);

        // Creates the item
        Item item = function.apply(properties);

        // Adds the item into Minecraft
        return Registry.register(BuiltInRegistries.ITEM, identifier, item);
    }

    /*
    This adds the items to their corresponding Creative Mode Tab and
    logs it to the console
     */
    public static void registerModItems() {
        // This writes to the console that the mod items are being loaded
        MothMod.LOGGER.info("Registering Mod Items for " + MothMod.MOD_ID);

        // This adds items that belong under the INGREDIENT category to the Creative Menu,
        // so that they are easily obtainable when you play
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(MOTH_WING);
        });

        // This adds items that belong under the SPAWN_EGGS category to the Creative Menu,
        // so that they are easily obtainable when you play
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(output -> {
            output.accept(MOTH_SPAWN_EGG);
        });
    }
}
