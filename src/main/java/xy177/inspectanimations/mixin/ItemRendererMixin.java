package xy177.inspectanimations.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xy177.inspectanimations.animation.AnimationState;
import xy177.inspectanimations.animation.IAnimationState;
import xy177.inspectanimations.animation.InspectAnimation;
import xy177.inspectanimations.client.AnimationTransforms;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Redirect(
            method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemSide(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V"
            ),
            require = 1
    )
    private void inspectAnimations$renderFirstPersonItem(
            ItemRenderer renderer, EntityLivingBase entity, ItemStack stack,
            ItemCameraTransforms.TransformType transform, boolean leftHanded) {
        if (!(entity instanceof AbstractClientPlayer)) {
            renderer.renderItemSide(entity, stack, transform, leftHanded);
            return;
        }

        AbstractClientPlayer player = (AbstractClientPlayer) entity;
        AnimationState state = ((IAnimationState) player).inspectAnimations$getAnimationState();
        if (state.getAnimation() == InspectAnimation.NONE) {
            renderer.renderItemSide(entity, stack, transform, leftHanded);
            return;
        }

        EnumHandSide renderedSide = leftHanded ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
        if (renderedSide == player.getPrimaryHand()) {
            GlStateManager.pushMatrix();
            AnimationTransforms.applyFirstPerson(state, renderedSide == EnumHandSide.RIGHT);
            renderer.renderItemSide(entity, stack, transform, leftHanded);
            GlStateManager.popMatrix();
        } else if (!state.getAnimation().isTwoHanded()) {
            renderer.renderItemSide(entity, stack, transform, leftHanded);
        }
    }
}
