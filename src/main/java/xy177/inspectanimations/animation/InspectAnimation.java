package xy177.inspectanimations.animation;

import xy177.inspectanimations.InspectAnimations;

public enum InspectAnimation {
    NONE(0, "none"),
    TURN(1, "turn"),
    TOSS(2, "toss"),
    FLIP(3, "flip"),
    FLOURISH(4, "flourish"),
    RANDOM(5, "random");

    private static final InspectAnimation[] PLAYABLE = {TURN, TOSS, FLIP, FLOURISH};

    private final int id;
    private final String serializedName;

    InspectAnimation(int id, String serializedName) {
        this.id = id;
        this.serializedName = serializedName;
    }

    public int getId() {
        return id;
    }

    public String getSerializedName() {
        return serializedName;
    }

    public String getTranslationKey() {
        return InspectAnimations.MOD_ID + ".animation." + serializedName;
    }

    public boolean isTwoHanded() {
        return this == FLOURISH;
    }

    public static InspectAnimation byId(int id) {
        for (InspectAnimation animation : values()) {
            if (animation.id == id) {
                return animation;
            }
        }
        return NONE;
    }

    public static InspectAnimation[] playableValues() {
        return PLAYABLE.clone();
    }
}
