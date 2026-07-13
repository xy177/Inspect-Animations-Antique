package xy177.inspectanimationsantique.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xy177.inspectanimationsantique.animation.AnimationState;
import xy177.inspectanimationsantique.animation.IAnimationState;
import xy177.inspectanimationsantique.animation.InspectAnimation;
import xy177.inspectanimationsantique.client.AnimationTransforms;

@Mixin(LayerHeldItem.class)
public abstract class LayerHeldItemMixin {
    @Shadow
    protected abstract void translateToHand(EnumHandSide side);

    @Redirect(
            method = "renderHeldItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Lnet/minecraft/util/EnumHandSide;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/layers/LayerHeldItem;translateToHand(Lnet/minecraft/util/EnumHandSide;)V"
            ),
            require = 1
    )
    private void inspectAnimationsAntique$translateToHand(
            LayerHeldItem owner, EnumHandSide side, EntityLivingBase entity) {
        if (entity instanceof AbstractClientPlayer) {
            AnimationState state = ((IAnimationState) entity).inspectAnimationsAntique$getAnimationState();
            if (state.getAnimation().isTwoHanded()) {
                return;
            }
        }
        translateToHand(side);
    }

    @Redirect(
            method = "renderHeldItem(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Lnet/minecraft/util/EnumHandSide;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemSide(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"
            ),
            require = 1
    )
    private void inspectAnimationsAntique$renderThirdPersonItem(
            ItemRenderer renderer, EntityLivingBase entity, ItemStack stack,
            ItemCameraTransforms.TransformType transform, boolean leftHanded) {
        if (!(entity instanceof AbstractClientPlayer)) {
            renderer.renderItemSide(entity, stack, transform, leftHanded);
            return;
        }

        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        AnimationState state = ((IAnimationState) player).inspectAnimationsAntique$getAnimationState();
        if (state.getAnimation() == InspectAnimation.NONE) {
            renderer.renderItemSide(entity, stack, transform, leftHanded);
            return;
        }

        EnumHandSide renderedSide = leftHanded ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
        if (renderedSide == player.getPrimaryHand()) {
            GlStateManager.pushMatrix();
            AnimationTransforms.applyThirdPerson(
                    state, renderedSide == EnumHandSide.RIGHT, stack, entity);
            renderer.renderItemSide(entity, stack, transform, leftHanded);
            GlStateManager.popMatrix();
        } else if (!state.getAnimation().isTwoHanded()) {
            renderer.renderItemSide(entity, stack, transform, leftHanded);
        }
    }
}
