package xy177.inspectanimations.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import xy177.inspectanimations.InspectAnimations;

public final class ClientEventHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientAnimationController.handleKeys();
        }
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ClientAnimationController.updateAnimations();
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getEntityPlayer() == Minecraft.getMinecraft().player) {
            ClientAnimationController.cancelLocalAnimation();
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent.Start event) {
        if (event.getEntityLiving() == Minecraft.getMinecraft().player) {
            ClientAnimationController.cancelLocalAnimation();
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (event.getEntityPlayer() == Minecraft.getMinecraft().player) {
            ClientAnimationController.cancelLocalAnimation();
        }
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        ClientAnimationController.resetConnection();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (InspectAnimations.MOD_ID.equals(event.getModID())) {
            ConfigManager.sync(InspectAnimations.MOD_ID, Config.Type.INSTANCE);
            ClientAnimationController.onConfigChanged();
        }
    }
}
