package xy177.inspectanimationsantique.animation;

public final class AnimationState {
    private InspectAnimation animation = InspectAnimation.NONE;
    private long playtime;
    private int duration;
    private int loops;
    private int maxLoops;
    private long lastUpdateTime;

    public void start(InspectAnimation animation, int duration, int maxLoops) {
        this.animation = animation;
        this.playtime = 0;
        this.duration = Math.max(1, duration);
        this.loops = 0;
        this.maxLoops = Math.max(0, maxLoops);
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public boolean update(long now, int maxDelta) {
        if (animation == InspectAnimation.NONE) {
            return false;
        }

        long delta = lastUpdateTime == 0 ? 1 : Math.max(0, now - lastUpdateTime);
        lastUpdateTime = now;

        if (playtime >= duration) {
            playtime = 0;
            loops++;
            if (loops > maxLoops) {
                clear();
                return true;
            }
        } else {
            playtime += Math.min(delta, Math.max(1, maxDelta));
        }
        return false;
    }

    public void clear() {
        animation = InspectAnimation.NONE;
        playtime = 0;
        duration = 0;
        loops = 0;
        maxLoops = 0;
        lastUpdateTime = 0;
    }

    public InspectAnimation getAnimation() {
        return animation;
    }

    public long getPlaytime() {
        return playtime;
    }

    public int getDuration() {
        return duration;
    }

    public int getLoops() {
        return loops;
    }

    public int getMaxLoops() {
        return maxLoops;
    }
}
