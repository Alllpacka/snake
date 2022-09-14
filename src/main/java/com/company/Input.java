package com.company;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Input implements NativeKeyListener {

    private Snake snake;

    public Input(Snake snake) {
        this.snake = snake;
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ex) {
                throw new RuntimeException(ex);
            }
            System.exit(0);
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_W || e.getKeyCode() == NativeKeyEvent.VC_UP) {
            Direction.direction = Direction.Up;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_A || e.getKeyCode() == NativeKeyEvent.VC_LEFT) {
            Direction.direction = Direction.Left;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_S || e.getKeyCode() == NativeKeyEvent.VC_DOWN) {
            Direction.direction = Direction.Down;
        }
        if (e.getKeyCode() == NativeKeyEvent.VC_D || e.getKeyCode() == NativeKeyEvent.VC_RIGHT) {
            Direction.direction = Direction.Right;
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {

    }

    public void nativeKeyTyped(NativeKeyEvent e) {

    }

    public static void startInputListener(Snake snake) {
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new Input(snake));
    }
}