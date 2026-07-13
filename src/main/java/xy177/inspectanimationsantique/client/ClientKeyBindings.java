package xy177.inspectanimationsantique.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;
import xy177.inspectanimationsantique.InspectAnimationsAntique;

public final class ClientKeyBindings {
    private static final String CATEGORY = "key.categories." + InspectAnimationsAntique.MOD_ID;

    public static final KeyBinding INSPECT = new KeyBinding(
            "key." + InspectAnimationsAntique.MOD_ID + ".inspect", Keyboard.KEY_R, CATEGORY);
    public static final KeyBinding MODIFIER = new KeyBinding(
            "key." + InspectAnimationsAntique.MOD_ID + ".modifier", Keyboard.KEY_LSHIFT, CATEGORY);
    public static final KeyBinding CONFIG = new KeyBinding(
            "key." + InspectAnimationsAntique.MOD_ID + ".config", Keyboard.KEY_NONE, CATEGORY);
    public static final KeyBinding TURN = new KeyBinding(
            "key." + InspectAnimationsAntique.MOD_ID + ".play_turn", Keyboard.KEY_NONE, CATEGORY);
    public static final KeyBinding TOSS = new KeyBinding(
            "key." + InspectAnimationsAntique.MOD_ID + ".play_toss", Keyboard.KEY_NONE, CATEGORY);
    public static final KeyBinding FLIP = new KeyBinding(
            "key." + InspectAnimationsAntique.MOD_ID + ".play_flip", Keyboard.KEY_NONE, CATEGORY);
    public static final KeyBinding FLOURISH = new KeyBinding(
            "key." + InspectAnimationsAntique.MOD_ID + ".play_flourish", Keyboard.KEY_NONE, CATEGORY);
    public static final KeyBinding RANDOM = new KeyBinding(
            "key." + InspectAnimationsAntique.MOD_ID + ".play_random", Keyboard.KEY_NONE, CATEGORY);

    private ClientKeyBindings() {
    }

    public static void register() {
        ClientRegistry.registerKeyBinding(INSPECT);
        ClientRegistry.registerKeyBinding(MODIFIER);
        ClientRegistry.registerKeyBinding(CONFIG);
        ClientRegistry.registerKeyBinding(TURN);
        ClientRegistry.registerKeyBinding(TOSS);
        ClientRegistry.registerKeyBinding(FLIP);
        ClientRegistry.registerKeyBinding(FLOURISH);
        ClientRegistry.registerKeyBinding(RANDOM);
    }
}
