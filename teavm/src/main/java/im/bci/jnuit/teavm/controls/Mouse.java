package im.bci.jnuit.teavm.controls;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.MouseEvent;

public class Mouse {
    private Map<Short, MouseButtonControl> controls = new HashMap<Short, MouseButtonControl>();
    private int clientX;
    private int clientY;

    public Mouse() {
        controls.put((short) MouseEvent.LEFT_BUTTON, new MouseButtonControl((short) MouseEvent.LEFT_BUTTON));
        controls.put((short) MouseEvent.MIDDLE_BUTTON, new MouseButtonControl((short) MouseEvent.MIDDLE_BUTTON));
        controls.put((short) MouseEvent.RIGHT_BUTTON, new MouseButtonControl((short) MouseEvent.RIGHT_BUTTON));

        Window window = Window.current();
        window.listenMouseDown((MouseEvent e) -> {
            MouseButtonControl control = controls.get(e.getButton());
            if (null != control) {
                control.setPressed(true);
            }
        });
        window.listenMouseUp((MouseEvent e) -> {
            MouseButtonControl control = controls.get(e.getButton());
            if (null != control) {
                control.setPressed(false);
            }
        });
        window.addEventListener("mousemove", (MouseEvent e) -> {
            this.clientX = e.getClientX();
            this.clientY = e.getClientY();
        });
    }

    public Collection<MouseButtonControl> getPossibleControls() {
        return controls.values();
    }

    public int getClientX() {
        return clientX;
    }

    public int getClientY() {
        return clientY;
    }

    public boolean isPressed() {
        return controls.get(MouseEvent.LEFT_BUTTON).isPressed();
    }

}
