package xy177.inspectanimations.proxy;

import net.minecraftforge.common.MinecraftForge;
import xy177.inspectanimations.client.ClientAnimationController;
import xy177.inspectanimations.client.ClientEventHandler;
import xy177.inspectanimations.client.ClientKeyBindings;

public final class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        ClientKeyBindings.register();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        ClientAnimationController.initialize();
    }

    @Override
    public void handleAnimationSync(int entityId, int animationId, int duration, int loops) {
        ClientAnimationController.receiveAnimation(entityId, animationId, duration, loops);
    }

    @Override
    public void handleHandshake(boolean enabled) {
        ClientAnimationController.setHandshake(enabled);
    }
}
