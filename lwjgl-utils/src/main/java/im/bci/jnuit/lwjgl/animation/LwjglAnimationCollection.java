package im.bci.jnuit.lwjgl.animation;

import im.bci.jnuit.animation.IAnimationCollection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LwjglAnimationCollection implements IAnimationCollection {

    LinkedHashMap<String/*animation name*/, LwjglAnimation> animations;
    private final Map<String, LwjglAnimationImage> images;

    public LwjglAnimationCollection() {
        animations = new LinkedHashMap<>();
        images = new HashMap<>();
    }

    public void addAnimation(LwjglAnimation animation) {
        animations.put(animation.getName(), animation);
    }

    @Override
    public LwjglAnimation getFirst() {
        return animations.values().iterator().next();
    }

    @Override
    public LwjglAnimation getAnimationByName(String name) {
        LwjglAnimation nanimation = animations.get(name);
        if (null != nanimation) {
            return nanimation;
        } else {
            throw new RuntimeException("Unknown animation " + name);
        }
    }

    public Map<String, LwjglAnimationImage> getImages() {
        return images;
    }
}
