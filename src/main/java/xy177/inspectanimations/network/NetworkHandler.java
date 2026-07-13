package xy177.inspectanimations.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import xy177.inspectanimations.InspectAnimations;
import xy177.inspectanimations.animation.InspectAnimation;

public final class NetworkHandler {
    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(InspectAnimations.MOD_ID);

    private NetworkHandler() {
    }

    public static void init() {
        CHANNEL.registerMessage(AnimationRequestMessage.Handler.class,
                AnimationRequestMessage.class, 0, Side.SERVER);
        CHANNEL.registerMessage(AnimationSyncMessage.Handler.class,
                AnimationSyncMessage.class, 1, Side.CLIENT);
        CHANNEL.registerMessage(HandshakeMessage.Handler.class,
                HandshakeMessage.class, 2, Side.CLIENT);
    }

    public static boolean isValidAnimation(int animationId, int duration, int loops) {
        InspectAnimation animation = InspectAnimation.byId(animationId);
        if (animation.getId() != animationId) {
            return false;
        }
        switch (animation) {
            case NONE:
                return duration == 0 && loops == 0;
            case TURN:
                return duration >= 20 && duration <= 1800 && loops == 6;
            case TOSS:
                return duration >= 20 && duration <= 1800 && loops == 1;
            case FLIP:
                return duration >= 20 && duration <= 1800 && loops >= 0 && loops <= 19;
            case FLOURISH:
                return duration >= 20 && duration <= 2400 && loops >= 1 && loops <= 39 && loops % 2 == 1;
            default:
                return false;
        }
    }

}
