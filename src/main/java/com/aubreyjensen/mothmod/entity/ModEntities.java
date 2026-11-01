package com.aubreyjensen.mothmod.entity;

// My imports
import com.aubreyjensen.mothmod.MothMod;
import com.aubreyjensen.mothmod.entity.custom.MothEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

// Class to register all custom entities into the game
public class ModEntities {

    // Saves the identifier so we don't have to type it twice
    private static final Identifier MOTH_ID = Identifier.fromNamespaceAndPath(MothMod.MOD_ID, "moth");

    // Uses the MOTH_ID we just saved to create a ResourceKey
    public static final ResourceKey<EntityType<?>> MOTH_KEY = ResourceKey.create(Registries.ENTITY_TYPE, MOTH_ID);

    /*
    Creates and registers the moth but does not automatically spawn it into the world,
    our renderer and client classes will handle that stuff
     */
    public static final EntityType<MothEntity> MOTH = Registry.register(BuiltInRegistries.ENTITY_TYPE, MOTH_ID, EntityType.Builder.of(MothEntity::new, MobCategory.CREATURE)
            .sized(0.5f, 0.9f) // Defines the moth's collision box
            .build(MOTH_KEY)); // finishes building the moth

    // Logs to the console that the mobs are being added to the game
    public static void registerModEntities() {
        MothMod.LOGGER.info("Registering Mod Entities for " + MothMod.MOD_ID);
    }
}
