package com.aubreyjensen.mothmod;

// My imports
import com.aubreyjensen.mothmod.entity.ModEntities;
import com.aubreyjensen.mothmod.entity.client.ModModelLayerLocations;
import com.aubreyjensen.mothmod.entity.client.MothModel;
import com.aubreyjensen.mothmod.entity.client.MothRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;


public class MothModClient implements ClientModInitializer {

    /*
    This method runs automatically when Minecraft loads your mod.
    It registers the moth's model layers and renderer so that you can see the moth in-game
    */
    @Override
    public void onInitializeClient() {
        // Registers the moth's structure/parts
        ModelLayerRegistry.registerModelLayer(ModModelLayerLocations.MOTH, MothModel::createBodyLayer);

        // Registers the renderer which will display the moth
        EntityRenderers.register(ModEntities.MOTH, MothRenderer::new);
    }
}
