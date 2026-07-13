package xy177.inspectanimationsantique.config;

import net.minecraftforge.common.config.Config;
import xy177.inspectanimationsantique.InspectAnimationsAntique;
import xy177.inspectanimationsantique.animation.InspectAnimation;

@Config(modid = InspectAnimationsAntique.MOD_ID, name = InspectAnimationsAntique.MOD_ID)
public final class InspectConfig {
    @Config.Comment({
            "General animation selection and cycling options.",
            "常规动画选择和循环切换选项。"
    })
    @Config.LangKey("config.inspectanimationsantique.category.general")
    public static final General GENERAL = new General();

    @Config.Comment({
            "Animation durations and loop counts.",
            "动画时长和循环次数设置。"
    })
    @Config.LangKey("config.inspectanimationsantique.category.animations")
    public static final Animations ANIMATIONS = new Animations();

    @Config.Comment({
            "Experimental block, item registry, and Ore Dictionary animation rules.",
            "实验性方块、物品注册名和矿物词典动画规则。"
    })
    @Config.LangKey("config.inspectanimationsantique.category.custom")
    public static final CustomAnimations CUSTOM_ANIMATIONS = new CustomAnimations();

    @Config.Comment({
            "Animation timing safeguards.",
            "动画计时保护设置。"
    })
    @Config.LangKey("config.inspectanimationsantique.category.performance")
    public static final Performance PERFORMANCE = new Performance();

    private InspectConfig() {
    }

    public static boolean isEnabled(InspectAnimation animation) {
        switch (animation) {
            case RANDOM:
                return GENERAL.enableRandom;
            case NONE:
                return GENERAL.enableNone;
            case TURN:
                return GENERAL.enableTurn;
            case TOSS:
                return GENERAL.enableToss;
            case FLIP:
                return GENERAL.enableFlip;
            case FLOURISH:
                return GENERAL.enableFlourish;
            default:
                return false;
        }
    }

    public static String[] getSelectors(InspectAnimation animation) {
        switch (animation) {
            case TURN:
                return CUSTOM_ANIMATIONS.turnItems;
            case TOSS:
                return CUSTOM_ANIMATIONS.tossItems;
            case FLIP:
                return CUSTOM_ANIMATIONS.flipItems;
            case FLOURISH:
                return CUSTOM_ANIMATIONS.flourishItems;
            case NONE:
                return CUSTOM_ANIMATIONS.noneItems;
            default:
                return new String[0];
        }
    }

    public static final class General {
        @Config.Comment({
                "Animation selected when the client starts.",
                "客户端启动时选择的动画。"
        })
        @Config.LangKey("config.inspectanimationsantique.default_animation")
        public InspectAnimation defaultAnimation = InspectAnimation.TURN;

        @Config.LangKey("config.inspectanimationsantique.enable_random")
        public boolean enableRandom = true;
        @Config.LangKey("config.inspectanimationsantique.enable_none")
        public boolean enableNone = false;
        @Config.LangKey("config.inspectanimationsantique.enable_turn")
        public boolean enableTurn = true;
        @Config.LangKey("config.inspectanimationsantique.enable_toss")
        public boolean enableToss = true;
        @Config.LangKey("config.inspectanimationsantique.enable_flip")
        public boolean enableFlip = true;
        @Config.LangKey("config.inspectanimationsantique.enable_flourish")
        public boolean enableFlourish = true;
    }

    public static final class Animations {
        @Config.RangeInt(min = 20, max = 1800)
        @Config.LangKey("config.inspectanimationsantique.turn_duration")
        public int turnDuration = 600;

        @Config.RangeInt(min = 20, max = 1800)
        @Config.LangKey("config.inspectanimationsantique.toss_duration")
        public int tossDuration = 600;

        @Config.RangeInt(min = 20, max = 1800)
        @Config.LangKey("config.inspectanimationsantique.flip_duration")
        public int flipDuration = 600;

        @Config.RangeInt(min = 20, max = 2400)
        @Config.LangKey("config.inspectanimationsantique.flourish_duration")
        public int flourishDuration = 800;

        @Config.RangeInt(min = 1, max = 20)
        @Config.LangKey("config.inspectanimationsantique.flip_loops")
        public int flipLoops = 2;

        @Config.RangeInt(min = 1, max = 20)
        @Config.LangKey("config.inspectanimationsantique.flourish_loops")
        public int flourishLoops = 5;
    }

    public static final class CustomAnimations {
        @Config.LangKey("config.inspectanimationsantique.enable_block_animation")
        public boolean enableBlockAnimation = false;
        @Config.LangKey("config.inspectanimationsantique.block_animation")
        public InspectAnimation blockAnimation = InspectAnimation.NONE;

        @Config.Comment({
                "Enable per-item animation rules.",
                "Use item registry names such as minecraft:diamond_sword.",
                "Prefix an Ore Dictionary name with #, such as #toolSword.",
                "启用逐物品动画规则。",
                "使用物品注册名，例如 minecraft:diamond_sword。",
                "矿物词典名称需添加 # 前缀，例如 #toolSword。"
        })
        @Config.LangKey("config.inspectanimationsantique.enable_item_animations")
        public boolean enableItemAnimations = false;

        @Config.LangKey("config.inspectanimationsantique.turn_items")
        public String[] turnItems = new String[0];
        @Config.LangKey("config.inspectanimationsantique.toss_items")
        public String[] tossItems = new String[0];
        @Config.LangKey("config.inspectanimationsantique.flip_items")
        public String[] flipItems = new String[0];
        @Config.LangKey("config.inspectanimationsantique.flourish_items")
        public String[] flourishItems = new String[0];
        @Config.LangKey("config.inspectanimationsantique.none_items")
        public String[] noneItems = new String[0];
    }

    public static final class Performance {
        @Config.Comment({
                "Maximum animation time advance per rendered frame, in milliseconds.",
                "每个渲染帧允许动画推进的最大时间，单位为毫秒。"
        })
        @Config.RangeInt(min = 1, max = 80)
        @Config.LangKey("config.inspectanimationsantique.max_delta")
        public int maxDelta = 40;
    }
}
