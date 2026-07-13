package xy177.inspectanimations.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xy177.inspectanimations.animation.AnimationState;
import xy177.inspectanimations.animation.IAnimationState;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin implements IAnimationState {
    @Unique
    private final AnimationState inspectAnimations$animationState = new AnimationState();

    @Override
    public AnimationState inspectAnimations$getAnimationState() {
        return inspectAnimations$animationState;
    }
}
