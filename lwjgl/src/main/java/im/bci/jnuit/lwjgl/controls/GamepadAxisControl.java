package im.bci.jnuit.lwjgl.controls;

import org.lwjgl.glfw.GLFW;

import im.bci.jnuit.controls.Control;

public class GamepadAxisControl extends AbstractGamepadControl implements Control {

    private final int axis;
    private final float scale;
    
    public GamepadAxisControl(int pad, int axis, String name, boolean positive) {
        super(pad, name);
        this.axis = axis;
        this.scale = positive ? 1.0f : -1.0f;
    }

    @Override
    public float getDeadZone() {
        return 0.25f;
    }

    @Override
    public float getValue() {
        GLFW.glfwGetGamepadState(pad, state);
        return Math.max(0.0f, state.axes(axis) * scale);
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
