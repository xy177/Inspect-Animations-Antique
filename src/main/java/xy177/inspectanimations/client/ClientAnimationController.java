package xy177.inspectanimations.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.oredict.OreDictionary;
import xy177.inspectanimations.InspectAnimations;
import xy177.inspectanimations.animation.AnimationState;
import xy177.inspectanimations.animation.IAnimationState;
import xy177.inspectanimations.animation.InspectAnimation;
import xy177.inspectanimations.config.InspectConfig;
import xy177.inspectanimations.network.AnimationRequestMessage;
import xy177.inspectanimations.network.NetworkHandler;

public final class ClientAnimationController {
    private static InspectAnimation selectedAnimation = InspectAnimation.TURN;
    private static boolean inspecting;
    private static boolean handshake;
    private static int heldSlot = -1;
    private static ItemStack heldItem = ItemStack.EMPTY;

    private ClientAnimationController() {
    }

    public static void initialize() {
        selectedAnimation = InspectConfig.GENERAL.defaultAnimation;
    }

    public static void handleKeys() {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;

        while (ClientKeyBindings.CONFIG.isPressed()) {
            minecraft.displayGuiScreen(new GuiConfig(minecraft.currentScreen,
                    InspectAnimations.MOD_ID, InspectAnimations.NAME));
        }
        if (player == null) {
            return;
        }

        while (ClientKeyBindings.INSPECT.isPressed()) {
            if (ClientKeyBindings.MODIFIER.isKeyDown()) {
                selectedAnimation = cycle(selectedAnimation);
                player.sendStatusMessage(new TextComponentTranslation(
                        "text." + InspectAnimations.MOD_ID + ".switch_animation")
                        .appendText(" ")
                        .appendSibling(new TextComponentTranslation(selectedAnimation.getTranslationKey())), true);
                if (inspecting) {
                    cancel(player, true);
                }
            } else {
                playSelected(player);
            }
        }
        while (ClientKeyBindings.TURN.isPressed()) {
            play(player, InspectAnimation.TURN);
        }
        while (ClientKeyBindings.TOSS.isPressed()) {
            play(player, InspectAnimation.TOSS);
        }
        while (ClientKeyBindings.FLIP.isPressed()) {
            play(player, InspectAnimation.FLIP);
        }
        while (ClientKeyBindings.FLOURISH.isPressed()) {
            play(player, InspectAnimation.FLOURISH);
        }
        while (ClientKeyBindings.RANDOM.isPressed()) {
            play(player, InspectAnimation.RANDOM);
        }
    }

    public static void updateAnimations() {
        Minecraft minecraft = Minecraft.getMinecraft();
        WorldClient world = minecraft.world;
        if (world == null) {
            return;
        }

        long now = System.currentTimeMillis();
        for (Object object : world.playerEntities) {
            if (object instanceof AbstractClientPlayer) {
                AbstractClientPlayer player = (AbstractClientPlayer) object;
                boolean finished = state(player).update(now, InspectConfig.PERFORMANCE.maxDelta);
                if (finished && player == minecraft.player) {
                    inspecting = false;
                }
            }
        }

        EntityPlayerSP player = minecraft.player;
        if (player != null && inspecting) {
            ItemStack current = player.getHeldItemMainhand();
            if (player.inventory.currentItem != heldSlot || current.isEmpty()
                    || !ItemStack.areItemsEqual(current, heldItem)
                    || !ItemStack.areItemStackTagsEqual(current, heldItem)
                    || player.isHandActive()) {
                cancel(player, true);
            }
        }
    }

    public static void receiveAnimation(int entityId, int animationId, int duration, int loops) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.world == null) {
            return;
        }
        if (minecraft.world.getEntityByID(entityId) instanceof AbstractClientPlayer) {
            AbstractClientPlayer player = (AbstractClientPlayer) minecraft.world.getEntityByID(entityId);
            InspectAnimation animation = InspectAnimation.byId(animationId);
            if (animation == InspectAnimation.NONE) {
                state(player).clear();
            } else {
                state(player).start(animation, duration, loops);
            }
        }
    }

    public static void cancelLocalAnimation() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player != null) {
            cancel(player, true);
        }
    }

    public static void setHandshake(boolean enabled) {
        handshake = enabled;
    }

    public static void resetConnection() {
        handshake = false;
        inspecting = false;
        heldSlot = -1;
        heldItem = ItemStack.EMPTY;
    }

    public static void onConfigChanged() {
        selectedAnimation = InspectConfig.GENERAL.defaultAnimation;
        cancelLocalAnimation();
    }

    public static AnimationState state(AbstractClientPlayer player) {
        return ((IAnimationState) player).inspectAnimations$getAnimationState();
    }

    private static void playSelected(EntityPlayerSP player) {
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty() || player.isHandActive()) {
            return;
        }

        if (InspectConfig.CUSTOM_ANIMATIONS.enableBlockAnimation && stack.getItem() instanceof ItemBlock) {
            selectedAnimation = InspectConfig.CUSTOM_ANIMATIONS.blockAnimation;
        } else {
            applyCustomItemAnimation(stack);
        }
        play(player, selectedAnimation);
    }

    private static void play(EntityPlayerSP player, InspectAnimation animation) {
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.isEmpty() || player.isHandActive()) {
            return;
        }
        if (animation == InspectAnimation.RANDOM) {
            InspectAnimation[] animations = InspectAnimation.playableValues();
            play(player, animations[player.getRNG().nextInt(animations.length)]);
            return;
        }
        if (animation == InspectAnimation.NONE) {
            cancel(player, true);
            return;
        }

        int duration;
        int loops;
        switch (animation) {
            case TURN:
                duration = InspectConfig.ANIMATIONS.turnDuration;
                loops = AnimationTransforms.TURN_LOOPS;
                break;
            case TOSS:
                duration = InspectConfig.ANIMATIONS.tossDuration;
                loops = AnimationTransforms.TOSS_LOOPS;
                break;
            case FLIP:
                duration = InspectConfig.ANIMATIONS.flipDuration;
                loops = InspectConfig.ANIMATIONS.flipLoops - 1;
                break;
            case FLOURISH:
                duration = InspectConfig.ANIMATIONS.flourishDuration;
                loops = InspectConfig.ANIMATIONS.flourishLoops * 2 - 1;
                break;
            default:
                return;
        }

        inspecting = true;
        heldSlot = player.inventory.currentItem;
        heldItem = stack.copy();
        state(player).start(animation, duration, loops);
        if (handshake) {
            NetworkHandler.CHANNEL.sendToServer(new AnimationRequestMessage(
                    player.getEntityId(), animation.getId(), duration, loops));
        }
    }

    private static void cancel(EntityPlayerSP player, boolean notifyServer) {
        if (!inspecting && state(player).getAnimation() == InspectAnimation.NONE) {
            return;
        }
        state(player).clear();
        inspecting = false;
        heldSlot = -1;
        heldItem = ItemStack.EMPTY;
        if (notifyServer && handshake) {
            NetworkHandler.CHANNEL.sendToServer(new AnimationRequestMessage(
                    player.getEntityId(), InspectAnimation.NONE.getId(), 0, 0));
        }
    }

    private static InspectAnimation cycle(InspectAnimation current) {
        for (int offset = 1; offset < InspectAnimation.values().length; offset++) {
            InspectAnimation candidate = InspectAnimation.byId(
                    (current.getId() + offset) % InspectAnimation.values().length);
            if (InspectConfig.isEnabled(candidate)) {
                return candidate;
            }
        }
        return InspectAnimation.NONE;
    }

    private static void applyCustomItemAnimation(ItemStack stack) {
        if (!InspectConfig.CUSTOM_ANIMATIONS.enableItemAnimations || stack.getItem().getRegistryName() == null) {
            return;
        }

        String itemId = stack.getItem().getRegistryName().toString();
        for (InspectAnimation animation : InspectAnimation.values()) {
            for (String selector : InspectConfig.getSelectors(animation)) {
                if (itemId.equals(selector)) {
                    selectedAnimation = animation;
                    return;
                }
            }
        }

        int[] oreIds = OreDictionary.getOreIDs(stack);
        for (InspectAnimation animation : InspectAnimation.values()) {
            for (String selector : InspectConfig.getSelectors(animation)) {
                if (!selector.startsWith("#")) {
                    continue;
                }
                String oreName = selector.substring(1);
                for (int oreId : oreIds) {
                    if (oreName.equals(OreDictionary.getOreName(oreId))) {
                        selectedAnimation = animation;
                        return;
                    }
                }
            }
        }
    }

}
