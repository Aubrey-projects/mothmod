package com.aubreyjensen.mothmod.entity.client;

// My imports
import com.aubreyjensen.mothmod.MothMod;
import com.aubreyjensen.mothmod.entity.custom.MothEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class MothRenderer  extends MobRenderer<MothEntity, MothRenderState, MothModel> {
    public MothRenderer(EntityRendererProvider.Context context) {
        super(context, new MothModel(context.bakeLayer(ModModelLayerLocations.MOTH)), 0.45f);
    }

    // Finds the moth's texture file
    @Override
    public Identifier getTextureLocation(MothRenderState state) {
        return Identifier.fromNamespaceAndPath(MothMod.MOD_ID, "textures/entity/moth/moth.png");
    }

    // Creates the state of the moth that will get filled in each frame
    @Override
    public MothRenderState createRenderState() {
        return new MothRenderState();
    }

    // Allows the moths to use their animations
    @Override
    public void extractRenderState(MothEntity entity, MothRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.flyingAnimationState.copyFrom(entity.flyAnimationState);
    }
}
