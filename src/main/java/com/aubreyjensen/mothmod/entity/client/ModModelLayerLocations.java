package com.aubreyjensen.mothmod.entity.client;

// My imports
import com.aubreyjensen.mothmod.MothMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class ModModelLayerLocations {
    // Body model layer for the moth
    public static final ModelLayerLocation MOTH =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(MothMod.MOD_ID, "moth"), "main");
}
