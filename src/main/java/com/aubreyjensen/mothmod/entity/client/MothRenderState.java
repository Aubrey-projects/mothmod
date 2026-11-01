package com.aubreyjensen.mothmod.entity.client;

// My imports
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

// Per-frame data of the moth's renderer
public class MothRenderState extends LivingEntityRenderState {
    // The resting animation for the moth
    public final AnimationState idleAnimationState = new AnimationState();
    // The flying animation for the moth
    public final AnimationState flyingAnimationState = new AnimationState();
}
