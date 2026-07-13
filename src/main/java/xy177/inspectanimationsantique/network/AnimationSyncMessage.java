package xy177.inspectanimationsantique.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import xy177.inspectanimationsantique.InspectAnimationsAntique;

public final class AnimationSyncMessage implements IMessage {
    private int entityId;
    private int animationId;
    private int duration;
    private int loops;

    public AnimationSyncMessage() {
    }

    public AnimationSyncMessage(int entityId, int animationId, int duration, int loops) {
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

    public static final class Handler implements IMessageHandler<AnimationSyncMessage, IMessage> {
        @Override
        public IMessage onMessage(AnimationSyncMessage message, MessageContext context) {
            if (!NetworkHandler.isValidAnimation(message.animationId, message.duration, message.loops)) {
                return null;
            }
            IThreadListener thread = FMLCommonHandler.instance().getWorldThread(context.netHandler);
            thread.addScheduledTask(() -> InspectAnimationsAntique.PROXY.handleAnimationSync(
                    message.entityId, message.animationId, message.duration, message.loops));
            return null;
        }
    }
}
