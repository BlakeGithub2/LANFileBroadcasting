package main.utils;

import javafx.scene.input.KeyCode;
import main.exceptions.InvalidKeyCodeException;
import org.testfx.framework.junit.ApplicationTest;

import java.util.HashMap;

public class TestFXUtils extends ApplicationTest {
    private final HashMap<Character, KeyCode> specialKeyCodeMap = initializeKeyCodeMap();

    // NOTE: press method is not static

    public void inputText(String text) {
        text = text.toUpperCase();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            KeyCode specialKeyCode = specialKeyCodeMap.get(c);

            KeyCode code = null;
            if (specialKeyCode == null) {
                code = KeyCode.getKeyCode(c + "");
            } else {
                code = specialKeyCode;
            }

            if (specialKeyCode == KeyCode.COLON) {
                typeColon();
                continue;
            }

            if (code != null) {
                tap(code);
            } else {
                // Could not find key code
                String message = "Could not find key code represented with character" + c + ". ";
                message = message.concat("Try adding to the TestFXUtils HashMap?");
                throw new InvalidKeyCodeException(message);
            }
        }
    }

    public void typeColon() {
        // Colon KeyCode is glitchy.
        // Copied from https://github.com/TestFX/TestFX/issues/74
        press(KeyCode.SHIFT);
        tap(KeyCode.SEMICOLON);
        release(KeyCode.SHIFT);
    }

    public HashMap<Character, KeyCode> initializeKeyCodeMap() {
        HashMap<Character, KeyCode> map = new HashMap<>();

        map = new HashMap<>();
        map.put(':', KeyCode.COLON);
        map.put('\\', KeyCode.BACK_SLASH);
        map.put('/', KeyCode.SLASH);

        return map;
    }

    public void tap(KeyCode key) {
        press(key).release(key);
    }
}
