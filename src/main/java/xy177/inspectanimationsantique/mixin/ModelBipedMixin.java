package xy177.inspectanimationsantique.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHandSide;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xy177.inspectanimationsantique.animation.AnimationState;
import xy177.inspectanimationsantique.animation.IAnimationState;
import xy177.inspectanimationsantique.animation.InspectAnimation;
import xy177.inspectanimationsantique.client.AnimationTransforms;

@Mixin(ModelBiped.class)
public abstract class ModelBipedMixin {
    @Inject(
            method = "setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/model/ModelBiped;leftArmPose:Lnet/minecraft/client/model/ModelBiped$ArmPose;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            order = 900,
            require = 1
    )
    private void inspectAnimationsAntique$applyPose(
            float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, float scale, Entity entity, CallbackInfo callbackInfo) {
        if (entity instanceof AbstractClientPlayer) {
            AbstractClientPlayer player = (AbstractClientPlayer) entity;
            AnimationState state = ((IAnimationState) player).inspectAnimationsAntique$getAnimationState();
            if (state.getAnimation() != InspectAnimation.NONE) {
                AnimationTransforms.applyArmPose((ModelBiped) (Object) this, player, state);
            }
        }
    }

    @Redirect(
            method = "setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/model/ModelBiped;leftArmPose:Lnet/minecraft/client/model/ModelBiped$ArmPose;",
                    opcode = Opcodes.GETFIELD
            ),
            require = 2
    )
    private ModelBiped.ArmPose inspectAnimationsAntique$leftArmPose(
            ModelBiped owner, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, float scale, Entity entity) {
        return controlsArm(entity, EnumHandSide.LEFT) ? ModelBiped.ArmPose.EMPTY : owner.leftArmPose;
    }

    @Redirect(
            method = "setRotationAngles(FFFFFFLnet/minecraft/entity/Entity;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/model/ModelBiped;rightArmPose:Lnet/minecraft/client/model/ModelBiped$ArmPose;",
                    opcode = Opcodes.GETFIELD
            ),
            require = 2
    )
    private ModelBiped.ArmPose inspectAnimationsAntique$rightArmPose(
            ModelBiped owner, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, float scale, Entity entity) {
        return controlsArm(entity, EnumHandSide.RIGHT) ? ModelBiped.ArmPose.EMPTY : owner.rightArmPose;
    }

    private static boolean controlsArm(Entity entity, EnumHandSide side) {
        if (!(entity instanceof AbstractClientPlayer)) {
            return false;
        }
        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        AnimationState state = ((IAnimationState) player).inspectAnimationsAntique$getAnimationState();
        return state.getAnimation() != InspectAnimation.NONE
                && (state.getAnimation().isTwoHanded() || player.getPrimaryHand() == side);
    }
}
