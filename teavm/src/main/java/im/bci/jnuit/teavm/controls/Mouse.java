package im.bci.jnuit.teavm.controls;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.MouseEvent;

public class Mouse {
    private MouseButtonControl[] controls = new MouseButtonControl[3];
    private int clientX;
    private int clientY;

    public Mouse() {
        controls = new MouseButtonControl[] { new MouseButtonControl((short) 0), new MouseButtonControl((short) 1),
                new MouseButtonControl((short) 2) };
        Window window = Window.current();
        window.listenMouseDown((MouseEvent e) -> {
            final short button = e.getButton();
            if (button < controls.length) {
                MouseButtonControl control = controls[button];
                if (null != control) {
                    control.setPressed(true);
                }
            }
        });
        window.listenMouseUp((MouseEvent e) -> {
            final short button = e.getButton();
            if (button < controls.length) {
                MouseButtonControl control = controls[button];
                if (null != control) {
                    control.setPressed(false);
                }
            }
        });
        window.addEventListener("mousemove", (MouseEvent e) -> {
            this.clientX = e.getClientX();
            this.clientY = e.getClientY();
        });
    }

    public Collection<MouseButtonControl> getPossibleControls() {
        return Arrays.asList(controls);
    }

    public int getClientX() {
        return clientX;
    }

    public int getClientY() {
        return clientY;
    }

    public boolean isPressed() {
        return controls[0].isPressed();
    }

}
