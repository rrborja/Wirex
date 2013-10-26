/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.listeners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import static net.wirex.listeners.ListenerFactory.keyListener;

/**
 *
 * @author RBORJA
 */
public class JToggleButtonListener extends ListenerFactory {

    private JToggleButtonListener() {
    }

    public static void addActionListener(JPanel view, Field field, Object presenter, Method listener) {
        try {
            JToggleButton button = (JToggleButton) field.get(view);
            button.addActionListener(actionListener(presenter, listener));
            field.set(view, button);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(JToggleButtonListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static void addCaretListener
    public static void addKeyListener(JPanel view, Field field, Object presenter, Method... listener) {
        try {
            JToggleButton button = (JToggleButton) field.get(view);
            button.addKeyListener(keyListener(presenter, listener));
            field.set(view, button);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(JToggleButtonListener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
