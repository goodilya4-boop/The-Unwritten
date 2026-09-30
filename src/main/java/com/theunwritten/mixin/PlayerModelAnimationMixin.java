package com.theunwritten.mixin;

import com.theunwritten.animation.AnimationStateMachine;
import com.theunwritten.animation.MovementAnimationState;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.entity.animation.json.AnimationLoader;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resources.ResourceLocation;

@Mixin(PlayerModel.class)
public abstract class PlayerModelAnimationMixin {
    @Unique
    private static final Vector3f THEUNWRITTEN_ANIMATION_CACHE = new Vector3f();

    @Unique
    private static final ResourceLocation THEUNWRITTEN_IDLE =
            ResourceLocation.fromNamespaceAndPath("theunwritten", "movement/idle");

    @Unique
    private static final ResourceLocation THEUNWRITTEN_WALK =
            ResourceLocation.fromNamespaceAndPath("theunwritten", "movement/walk");

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void theunwritten$applyMovementAnimation(
            LivingEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo callbackInfo
    ) {
        if (!(entity instanceof Player player)) {
            return;
        }

        MovementAnimationState state = AnimationStateMachine.resolveMovementState(player);
        HierarchicalModel<?> model = (HierarchicalModel<?>) (Object) this;

        switch (state) {
            case IDLE -> KeyframeAnimations.animate(
                    model,
                    AnimationLoader.INSTANCE.getAnimation(THEUNWRITTEN_IDLE),
                    Math.round(ageInTicks * 50.0F),
                    1.0F,
                    THEUNWRITTEN_ANIMATION_CACHE
            );
            case WALK -> KeyframeAnimations.animate(
                    model,
                    AnimationLoader.INSTANCE.getAnimation(THEUNWRITTEN_WALK),
                    Math.round(limbSwing * 50.0F),
                    Math.min(limbSwingAmount, 1.0F),
                    THEUNWRITTEN_ANIMATION_CACHE
            );
            default -> {
                // Keep vanilla animation for states that do not have custom keyframes yet.
            }
        }
    }
}
