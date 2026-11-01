package com.aubreyjensen.mothmod.entity.client;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/*
I am not going to add a lot of comments here because I don't understand what most of the lines of code do
I used BlockBench to create a model of my moth, and then I exported it as a .java
Most of this code is just telling the program how the moth looks but, since I modeled it in a modeling software,
I created it, but I didn't type it line by line.
 */
public class MothModel extends EntityModel<MothRenderState> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart bone2;
    private final ModelPart bone;
    private final ModelPart body;
    private final ModelPart left_wing;
    private final ModelPart outer_left_wing;
    private final ModelPart right_wing;
    private final ModelPart outer_right_wing;
    private final ModelPart feet;

    // This adds the animations
    private final KeyframeAnimation flyingAnimation;
    private final KeyframeAnimation idlingAnimation;

    public MothModel(ModelPart root) {
        super(root);

        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
        this.bone2 = this.head.getChild("bone2");
        this.bone = this.head.getChild("bone");
        this.body = this.root.getChild("body");
        this.left_wing = this.root.getChild("left_wing");
        this.outer_left_wing = this.root.getChild("outer_left_wing");
        this.right_wing = this.root.getChild("right_wing");
        this.outer_right_wing = this.root.getChild("outer_right_wing");
        this.feet = this.root.getChild("feet");

        // this bakes the animations
        this.flyingAnimation = MothAnimations.FLYING.bake(root);
        this.idlingAnimation = MothAnimations.IDLE.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 11).addBox(-2.0F, -3.0F, -1.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bone2 = head.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(14, 17).addBox(-1.0F, -13.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition bone = head.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(16, 17).addBox(0.0F, -13.0F, 0.0F, 1.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(10, 11).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_wing = root.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(4, 15).addBox(0.0F, -2.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, 0.0F));

        PartDefinition outer_left_wing = root.addOrReplaceChild("outer_left_wing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -4.0F, 0.0F, 5.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 0.0F, 0.0F));

        PartDefinition right_wing = root.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 0.0F, 0.0F));

        PartDefinition outer_right_wing = root.addOrReplaceChild("outer_right_wing", CubeListBuilder.create().texOffs(10, 0).addBox(-5.0F, -4.0F, 0.0F, 5.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 0.0F, 0.0F));

        PartDefinition feet = root.addOrReplaceChild("feet", CubeListBuilder.create().texOffs(8, 17).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    // This sets up the animations to work with the model
    public void setupAnim(MothRenderState state) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.idlingAnimation.apply(state.idleAnimationState, state.ageInTicks, 1f);
        this.flyingAnimation.apply(state.flyingAnimationState, state.ageInTicks, 1f);
    }
}
