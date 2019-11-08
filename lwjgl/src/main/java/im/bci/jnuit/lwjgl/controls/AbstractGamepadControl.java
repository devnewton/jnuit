package im.bci.jnuit.lwjgl.controls;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWGamepadState;

import im.bci.jnuit.controls.Control;

public abstract class AbstractGamepadControl implements Control {

    protected final int pad;
    private final String name;
    protected static GLFWGamepadState[] states;
    static {
    	states = new GLFWGamepadState[GLFW.GLFW_JOYSTICK_LAST+1];
    	for(int i=0; i<states.length; ++i) {
    		states[i] = GLFWGamepadState.create();
    	}
    }

    public AbstractGamepadControl(int pad, String name) {
        this.pad = pad;
        this.name = name;
    }

    @Override
    public String getControllerName() {
        return GLFW.glfwGetGamepadName(pad);
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + pad;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractGamepadControl other = (AbstractGamepadControl) obj;
        if (pad != other.pad)
            return false;
        return true;
    }

}