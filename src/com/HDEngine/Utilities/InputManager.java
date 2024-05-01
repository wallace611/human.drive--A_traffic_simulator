package com.HDEngine.Utilities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener {
    private boolean[] pressedKey;

    public InputManager() {
        this.pressedKey = new boolean[256];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKey[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKey[e.getKeyCode()] = false;
    }

    public boolean[] getPressedKey() {
        boolean[] res = new boolean[256];
        System.arraycopy(pressedKey, 0, res, 0, 256);
        return res;
    }
}
