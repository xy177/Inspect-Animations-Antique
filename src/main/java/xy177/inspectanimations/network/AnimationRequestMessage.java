package xy177.inspectanimations.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xy177.inspectanimations.common.CommonEventHandler;

public final class AnimationRequestMessage implements IMessage {
    private int entityId;
    private int animationId;
    private int duration;
    private int loops;

    public AnimationRequestMessage() {
    }

    public AnimationRequestMessage(int entityId, int animationId, int duration, int loops) {
        this.entityId = entityId;
        this.animationId = animationId;
        this.duration = duration;
        this.loops = loops;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        animationId = buf.readInt();
        duration = buf.readInt();
        loops = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(animationId);
        buf.writeInt(duration);
        buf.writeInt(loops);
    }

    public static final class Handler implements IMessageHandler<AnimationRequestMessage, IMessage> {
        @Override
        public IMessage onMessage(AnimationRequestMessage message, MessageContext context) {
            EntityPlayerMP sender = context.getServerHandler().player;
            sender.getServerWorld().addScheduledTask(() -> CommonEventHandler.handleAnimationRequest(
                    sender, message.entityId, message.animationId, message.duration, message.loops));
            return null;
        }
    }
}
