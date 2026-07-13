package xy177.inspectanimationsantique.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import xy177.inspectanimationsantique.animation.AnimationState;
import xy177.inspectanimationsantique.animation.InspectAnimation;

public final class AnimationTransforms {
    public static final int TURN_LOOPS = 6;
    public static final int TOSS_LOOPS = 1;

    private static final float X1 = -5.0F;
    private static final float X2 = 0.0F;
    private static final float Y1 = -25.0F;
    private static final float Y2 = 55.0F;
    private static final float Z1 = 75.0F;
    private static final float Z2 = -35.0F;

    private AnimationTransforms() {
    }

    public static void applyFirstPerson(AnimationState state, boolean rightHand) {
        switch (state.getAnimation()) {
            case FLOURISH:
                flourishFirstPerson(state, rightHand);
                break;
            case FLIP:
                flipFirstPerson(state);
                break;
            case TURN:
                turnFirstPerson(state, rightHand);
                break;
            case TOSS:
                tossFirstPerson(state, rightHand);
                break;
            default:
                break;
        }
    }

    public static void applyThirdPerson(AnimationState state, boolean rightHand, ItemStack stack,
                                        EntityLivingBase entity) {
        switch (state.getAnimation()) {
            case FLOURISH:
                flourishThirdPerson(state, rightHand);
                break;
            case FLIP:
                RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
                IBakedModel model = renderItem.getItemModelWithOverrides(stack, entity.world, entity);
                boolean standardItem = model.getItemCameraTransforms().thirdperson_right.rotation.z == 0.0F;
                flipThirdPerson(state, standardItem, rightHand, stack.getItem() instanceof ItemBlock);
                break;
            case TURN:
                turnThirdPerson(state, rightHand);
                break;
            case TOSS:
                tossThirdPerson(state, rightHand);
                break;
            default:
                break;
        }
    }

    public static void applyArmPose(ModelBiped model, AbstractClientPlayer player, AnimationState state) {
        boolean rightHanded = player.getPrimaryHand() == EnumHandSide.RIGHT;
        switch (state.getAnimation()) {
            case TURN:
                float turnProgress = percent(state);
                float turnAngle = state.getLoops() > 0 ? 0.4F : lerp(0, 0.4F, turnProgress);
                if (rightHanded) {
                    model.bipedRightArm.rotateAngleX = (float) (-Math.PI * turnAngle) + model.bipedHead.rotateAngleX;
                    model.bipedRightArm.rotateAngleY = model.bipedHead.rotateAngleY;
                } else {
                    model.bipedLeftArm.rotateAngleX = (float) (-Math.PI * turnAngle) + model.bipedHead.rotateAngleX;
                    model.bipedLeftArm.rotateAngleY = model.bipedHead.rotateAngleY;
                }
                break;
            case FLIP:
                if (rightHanded) {
                    model.bipedRightArm.rotateAngleX = (float) (-Math.PI * 0.4F);
                    model.bipedRightArm.rotateAngleY = model.bipedHead.rotateAngleY;
                } else {
                    model.bipedLeftArm.rotateAngleX = (float) (-Math.PI * 0.4F);
                    model.bipedLeftArm.rotateAngleY = model.bipedHead.rotateAngleY;
                }
                break;
            case TOSS:
                float tossProgress = percent(state);
                if (state.getLoops() < state.getMaxLoops()) {
                    tossProgress = 1 - tossProgress;
                }
                float tossAngle = (float) (-Math.PI * (tossProgress > 0.75F ? 0.3F : 0.5F));
                if (rightHanded) {
                    model.bipedRightArm.rotateAngleX = tossAngle;
                    model.bipedRightArm.rotateAngleY = model.bipedHead.rotateAngleY;
                } else {
                    model.bipedLeftArm.rotateAngleX = tossAngle;
                    model.bipedLeftArm.rotateAngleY = model.bipedHead.rotateAngleY;
                }
                break;
            case FLOURISH:
                flourishArmPose(model, rightHanded, state);
                break;
            default:
                break;
        }
    }

    private static void flourishFirstPerson(AnimationState state, boolean rightHand) {
        float progress = percent(state);
        float angle = lerp(0, 360, progress);
        GlStateManager.translate(ease(state.getLoops() % 2 == 0 ? progress : 1 - progress)
                * (rightHand ? -1.25F : 1.25F), 0, 0);
        GlStateManager.rotate(angle, 1, 0, 0);
    }

    private static void flourishThirdPerson(AnimationState state, boolean rightHand) {
        float progress = percent(state);
        float angle = ease(progress) * 360.0F;
        if (state.getLoops() % 2 != 0) {
            progress = 1 - progress;
        }

        if (rightHand) {
            GlStateManager.translate(0.33, 0.40, 0.5);
            GlStateManager.rotate(70, 1, 0, 0);
            GlStateManager.translate(ease(progress) * -0.79F, 0, 0);
            GlStateManager.rotate(angle, 1, 0, 0);
            GlStateManager.rotate(-21.0F + ease(progress) * 42.0F, 0, 1, 0);
        } else {
            GlStateManager.translate(-0.33, 0.40, 0.5);
            GlStateManager.rotate(70, 1, 0, 0);
            GlStateManager.translate(ease(progress) * 0.79F, 0, 0);
            GlStateManager.rotate(angle, 1, 0, 0);
            GlStateManager.rotate(21.0F - ease(progress) * 42.0F, 0, 1, 0);
        }
    }

    private static void flipFirstPerson(AnimationState state) {
        GlStateManager.rotate(lerp(0, 360, percent(state)), 1, 0, 0);
    }

    private static void flipThirdPerson(AnimationState state, boolean standardItem, boolean rightHand, boolean block) {
        GlStateManager.rotate(lerp(0, 360, percent(state)), 1, 0, 0);
        if (standardItem && !block) {
            GlStateManager.translate(rightHand ? 0.06F : -0.1825F, 0.0625, -0.125);
            GlStateManager.rotate(90, 0, 1, 0);
        } else {
            GlStateManager.translate(block ? 0 : rightHand ? 0.125F : -0.125F,
                    block ? -0.125F : 0.0625F, block ? 0 : -0.125F);
        }
    }

    private static void turnFirstPerson(AnimationState state, boolean rightHand) {
        float progress = percent(state);
        int loop = state.getLoops();
        if (loop < 1) {
            rotate(rightHand, lerp(0, X1, progress), lerp(0, Y1, progress), lerp(0, Z1, progress));
            GlStateManager.translate(0, lerp(0, 0.1F, progress), 0);
        } else if (loop < 3) {
            rotate(rightHand, X1, Y1, Z1);
            GlStateManager.translate(0, 0.1F, 0);
        } else if (loop < 4) {
            rotate(rightHand, lerp(X1, X2, progress), lerp(Y1, Y2, progress), lerp(Z1, Z2, progress));
            GlStateManager.translate(0, 0.1F, 0);
        } else if (loop < 6) {
            rotate(rightHand, X2, Y2, Z2);
            GlStateManager.translate(0, 0.1F, 0);
        } else {
            rotate(rightHand, X2, lerp(Y2, 0, progress), lerp(Z2, 0, progress));
            GlStateManager.translate(0, lerp(0.1F, 0, progress), 0);
        }
    }

    private static void turnThirdPerson(AnimationState state, boolean rightHand) {
        float progress = percent(state);
        int loop = state.getLoops();
        if (loop < 1) {
            GlStateManager.translate(0, lerp(0, -0.1F, progress), 0);
            rotate(rightHand, lerp(0, X1, progress), lerp(0, Y1, progress), lerp(0, Z1, progress));
        } else if (loop < 3) {
            GlStateManager.translate(0, -0.1F, 0);
            rotate(rightHand, X1, Y1, Z1);
        } else if (loop < 4) {
            GlStateManager.translate(0, lerp(-0.1F, 0, progress), 0);
            rotate(rightHand, lerp(X1, X2, progress), lerp(Y1, Y2, progress), lerp(Z1, Z2, progress));
        } else if (loop < 6) {
            rotate(rightHand, X2, Y2, Z2);
        } else {
            rotate(rightHand, X2, lerp(Y2, 0, progress), lerp(Z2, 0, progress));
        }
    }

    private static void tossFirstPerson(AnimationState state, boolean rightHand) {
        float progress = percent(state);
        float height = ease(progress);
        GlStateManager.translate(0, state.getLoops() < TOSS_LOOPS ? height : 1 - height, 0);
        float angle = lerp(0, 360, progress) * (rightHand ? 1 : -1);
        GlStateManager.rotate(angle, 1, 0, 0);
        GlStateManager.rotate(angle, 0, 1, 0);
    }

    private static void tossThirdPerson(AnimationState state, boolean rightHand) {
        float progress = percent(state);
        float height = ease(progress);
        GlStateManager.translate(0, state.getLoops() < TOSS_LOOPS ? height : 1 - height, 0);
        float angle = lerp(0, 360, progress) * (rightHand ? -1 : 1);
        GlStateManager.rotate(angle, 1, 0, 0);
        GlStateManager.rotate(angle, 0, 1, 0);
    }

    private static void flourishArmPose(ModelBiped model, boolean rightHanded, AnimationState state) {
        float progress = percent(state);
        if (state.getLoops() % 2 == 0) {
            if (progress < 0.25F) {
                setFlourishArm(model, rightHanded);
            } else if (progress > 0.65F) {
                setFlourishArm(model, !rightHanded);
            }
        } else if (progress < 0.25F) {
            setFlourishArm(model, !rightHanded);
        } else if (progress > 0.65F) {
            setFlourishArm(model, rightHanded);
        }
    }

    private static void setFlourishArm(ModelBiped model, boolean rightArm) {
        if (rightArm) {
            model.bipedRightArm.rotateAngleX = (float) (-Math.PI * 0.4F);
            model.bipedRightArm.rotateAngleY = (float) (Math.PI * 0.1F);
        } else {
            model.bipedLeftArm.rotateAngleX = (float) (-Math.PI * 0.4F);
            model.bipedLeftArm.rotateAngleY = (float) (Math.PI * -0.1F);
        }
    }

    private static void rotate(boolean rightHand, float x, float y, float z) {
        float sign = rightHand ? 1 : -1;
        GlStateManager.rotate(x * sign, 1, 0, 0);
        GlStateManager.rotate(y * sign, 0, 1, 0);
        GlStateManager.rotate(z * sign, 0, 0, 1);
    }

    public static float lerp(float start, float end, float fraction) {
        return start + fraction * (end - start);
    }

    private static float ease(float value) {
        return value < 0.5F
                ? 8.0F * value * value * value * value
                : 1.0F - (float) Math.pow(-2.0F * value + 2.0F, 4) / 2.0F;
    }

    private static float percent(AnimationState state) {
        return state.getDuration() <= 0 ? 0 : Math.min(1.0F, (float) state.getPlaytime() / state.getDuration());
    }
}
