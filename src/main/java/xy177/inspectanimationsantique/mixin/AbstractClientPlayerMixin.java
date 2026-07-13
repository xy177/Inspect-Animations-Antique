package xy177.inspectanimationsantique.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xy177.inspectanimationsantique.animation.AnimationState;
import xy177.inspectanimationsantique.animation.IAnimationState;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin implements IAnimationState {
    @Unique
    private final AnimationState inspectAnimationsAntique$animationState = new AnimationState();

    @Override
    public AnimationState inspectAnimationsAntique$getAnimationState() {
        return inspectAnimationsAntique$animationState;
    }
}
