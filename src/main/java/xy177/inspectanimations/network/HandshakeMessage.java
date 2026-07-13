package xy177.inspectanimations.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xy177.inspectanimations.InspectAnimations;

public final class HandshakeMessage implements IMessage {
    private boolean enabled;

    public HandshakeMessage() {
    }

    public HandshakeMessage(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        enabled = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(enabled);
    }

    public static final class Handler implements IMessageHandler<HandshakeMessage, IMessage> {
        @Override
        public IMessage onMessage(HandshakeMessage message, MessageContext context) {
            IThreadListener thread = FMLCommonHandler.instance().getWorldThread(context.netHandler);
            thread.addScheduledTask(() -> InspectAnimations.PROXY.handleHandshake(message.enabled));
            return null;
        }
    }
}
