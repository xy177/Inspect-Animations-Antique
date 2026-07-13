package xy177.inspectanimationsantique.common;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import xy177.inspectanimationsantique.InspectAnimationsAntique;
import xy177.inspectanimationsantique.network.AnimationSyncMessage;
import xy177.inspectanimationsantique.network.HandshakeMessage;
import xy177.inspectanimationsantique.network.NetworkHandler;

public final class CommonEventHandler {
    public static final String INSPECT_RADIUS = "inspect_radius";
    public static final String SEND_INSPECTIONS = "send_inspections";

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld().isRemote) {
            return;
        }
        GameRules rules = event.getWorld().getGameRules();
        if (!rules.hasRule(INSPECT_RADIUS)) {
            rules.addGameRule(INSPECT_RADIUS, "100", GameRules.ValueType.NUMERICAL_VALUE);
        }
        if (!rules.hasRule(SEND_INSPECTIONS)) {
            rules.addGameRule(SEND_INSPECTIONS, "true", GameRules.ValueType.BOOLEAN_VALUE);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        if (hasRemoteMod(player)) {
            NetworkHandler.CHANNEL.sendTo(new HandshakeMessage(
                    player.world.getGameRules().getBoolean(SEND_INSPECTIONS)), player);
        }
    }

    public static void handleAnimationRequest(EntityPlayerMP sender, int entityId,
                                              int animationId, int duration, int loops) {
        if (sender.getEntityId() != entityId
                || !NetworkHandler.isValidAnimation(animationId, duration, loops)
                || !sender.world.getGameRules().getBoolean(SEND_INSPECTIONS)) {
            return;
        }

        int radius = sender.world.getGameRules().getInt(INSPECT_RADIUS);
        if (radius <= 0 || !(sender.world instanceof WorldServer)) {
            return;
        }

        double radiusSquared = (double) radius * radius;
        AnimationSyncMessage sync = new AnimationSyncMessage(
                sender.getEntityId(), animationId, duration, loops);
        for (EntityPlayerMP target : ((WorldServer) sender.world).getMinecraftServer().getPlayerList().getPlayers()) {
            if (target == sender || target.dimension != sender.dimension || !hasRemoteMod(target)) {
                continue;
            }
            if (target.getDistanceSq(sender) <= radiusSquared) {
                NetworkHandler.CHANNEL.sendTo(sync, target);
            }
        }
    }

    private static boolean hasRemoteMod(EntityPlayerMP player) {
        NetworkDispatcher dispatcher = NetworkDispatcher.get(player.connection.getNetworkManager());
        return dispatcher != null && dispatcher.getModList() != null
                && dispatcher.getModList().containsKey(InspectAnimationsAntique.MOD_ID);
    }
}
