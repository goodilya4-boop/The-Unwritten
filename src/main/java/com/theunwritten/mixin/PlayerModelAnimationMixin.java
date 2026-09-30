package com.theunwritten.mixin;

import com.theunwritten.animation.AnimationStateMachine;
import com.theunwritten.animation.MovementAnimationState;
import com.theunwritten.animation.PlayerAnimationPose;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelAnimationMixin<T extends LivingEntity> {
    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void theunwritten$applyMovementAnimation(
            T entity,
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
        PlayerAnimationPose.apply(
                (PlayerModel<? extends Player>) (Object) this,
                player,
                state,
                limbSwing,
                limbSwingAmount,
                ageInTicks
        );
    }
}
