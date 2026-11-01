package com.aubreyjensen.mothmod;

// My imports
import com.aubreyjensen.mothmod.entity.ModEntities;
import com.aubreyjensen.mothmod.entity.custom.MothEntity;
import com.aubreyjensen.mothmod.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MothMod implements ModInitializer {
    // The identifier for this mod (We use this a LOT in the other classes)
	public static final String MOD_ID = "mothmod";

    // Used to print information to the console (helpful for debugging)
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /*
    This starts when you run the game,
    it calls the methods to register and add all of our new content to the game
     */
	@Override
	public void onInitialize() {
        // This will add in all of the new items
        ModItems.registerModItems();

        // This will add in all of the new creatures
        ModEntities.registerModEntities();

        // This adds the moth's attributes like health, speed, etc.
        FabricDefaultAttributeRegistry.register(ModEntities.MOTH, MothEntity.createAttributes());
	}

    // Creates an identifier using the mod's ID and the provided path
	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
