package im.bci.jnuit.teavm.controls;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.KeyboardEvent;

public class Keyboard {
    
    private Map<String, KeyControl> controls = new HashMap<String,KeyControl>();

    public Keyboard() {
        addKey("a");
        addKey("b");
        addKey("c");
        addKey("d");
        addKey("e");
        addKey("f");
        addKey("g");
        addKey("h");
        addKey("i");
        addKey("j");
        addKey("k");
        addKey("l");
        addKey("m");
        addKey("n");
        addKey("o");
        addKey("p");
        addKey("q");
        addKey("r");
        addKey("s");
        addKey("t");
        addKey("u");
        addKey("w");
        addKey("x");
        addKey("y");
        addKey("z");
        addKey("0");
        addKey("1");
        addKey("2");
        addKey("3");
        addKey("4");
        addKey("5");
        addKey("6");
        addKey("7");
        addKey("8");
        addKey("9");
        
        addKey("Alt");
        addKey("AltGraph");
        addKey("CapsLock");
        addKey("Control");
        addKey("Meta");
        addKey("Shift");
        addKey("Enter");
        addKey("Tab");
        addKey(" ");      
        
        addKey("ArrowDown");
        addKey("ArrowLeft");
        addKey("ArrowRight");
        addKey("ArrowUp");
        
        addKey("End");
        addKey("Home");
        addKey("PageDown");
        addKey("PageUp");
        
        addKey("F1");
        addKey("F2");
        addKey("F3");
        addKey("F4");
        addKey("F5");
        addKey("F6");
        addKey("F7");
        addKey("F8");
        addKey("F9");
        addKey("F10");
        addKey("F11");
        addKey("F12");
        
        addKey("Escape");
        addKey("Pause");
        
        Window window = Window.current();
        window.listenKeyDown((KeyboardEvent e) -> {
            KeyControl control = controls.get(e.getKey());
            if(null != control) {
                control.setPressed(true);
            }
        });
        window.listenKeyUp((KeyboardEvent e) -> {
            KeyControl control = controls.get(e.getKey());
            if(null != control) {
                control.setPressed(false);
            }
        });
    }

    private void addKey(String k) {
        controls.put(k, new KeyControl(k));
    }
    
    public KeyControl getKeyControl(String k) {
        return controls.get(k);
    }

    public Collection<KeyControl> getPossibleControls() {
        return controls.values();
    }

}
