package im.bci.jnuit.lwjgl.controls;

import org.lwjgl.glfw.GLFW;

import im.bci.jnuit.controls.Control;

public class GamepadButtonControl extends AbstractGamepadControl implements Control {

    private final int button;
    
    public GamepadButtonControl(int pad, int button, String name) {
        super(pad, name);
        this.button = button;
    }

    @Override
    public float getDeadZone() {
        return 0.1f;
    }

    @Override
    public float getValue() {
        GLFW.glfwGetGamepadState(pad, states[pad]);
        return GLFW.GLFW_PRESS == states[pad].buttons(button) ? 1f : 0f;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + button;
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
        GamepadButtonControl other = (GamepadButtonControl) obj;
        if (button != other.button)
            return false;
        return true;
    }

}
