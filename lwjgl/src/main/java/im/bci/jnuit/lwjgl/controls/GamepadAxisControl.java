package im.bci.jnuit.lwjgl.controls;

import org.lwjgl.glfw.GLFW;

import im.bci.jnuit.controls.Control;

public class GamepadAxisControl extends AbstractGamepadControl implements Control {

    private final int axis;
    private final float scale;
    
    public static final float SCALE_UP=-1.0f;
    public static final float SCALE_DOWN=1.0f;
    public static final float SCALE_LEFT=-1.0f;
    public static final float SCALE_RIGHT=1.0f;
    public static final float SCALE_OTHER=1.0f;
    
    public GamepadAxisControl(int pad, int axis, String name, float scale) {
        super(pad, name);
        this.axis = axis;
        this.scale = scale;
    }

    @Override
    public float getDeadZone() {
        return 0.25f;
    }

    @Override
    public float getValue() {
        GLFW.glfwGetGamepadState(pad, states[pad]);
        return Math.max(0.0f, states[pad].axes(axis) * scale);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + axis;
        result = prime * result + Float.floatToIntBits(scale);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GamepadAxisControl other = (GamepadAxisControl) obj;
        if (axis != other.axis)
            return false;
        if (Float.floatToIntBits(scale) != Float.floatToIntBits(other.scale))
            return false;
        return true;
    }

}
