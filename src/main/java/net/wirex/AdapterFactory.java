/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.Method;
import java.util.Map;
import static net.wirex.ListenerFactory.invoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ritchie
 */
public final class AdapterFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ListenerFactory.class.getName());

    public static KeyAdapter KeyAdapter(final Object presenter, final Map<String, Method> listener) {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                invoke(listener.get("keyReleased"), presenter, e);
            }
            @Override
            public void keyPressed(KeyEvent e) {
                invoke(listener.get("keyPressed"), presenter, e);
            }
            @Override
            public void keyTyped(KeyEvent e) {
                invoke(listener.get("keyTyped"), presenter, e);
            }
        };
    }

    public static MouseAdapter MouseAdapter(final Object presenter, final Map<String, Method> listener) {
        return new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                invoke(listener.get("mouseMoved"), presenter, e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                invoke(listener.get("mouseDragged"), presenter, e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                invoke(listener.get("mouseWheelMoved"), presenter, e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                invoke(listener.get("mouseExited"), presenter, e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                invoke(listener.get("mouseEntered"), presenter, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                invoke(listener.get("mouseReleased"), presenter, e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                invoke(listener.get("mousePressed"), presenter, e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                invoke(listener.get("mouseClicked"), presenter, e);
            }
        };
    }

    private AdapterFactory() {
    }
}
