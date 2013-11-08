/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.listeners;

import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import static net.wirex.listeners.ListenerFactory.keyListener;

/**
 *
 * @author RBORJA
 */
public class ComponentListenerInjector extends ListenerFactory {

    private ComponentListenerInjector() {
    }

    public static void addListener(Class listenerType, Class<? extends JComponent> component, JPanel view, Field field, Object presenter, Method listener) {
        try {
            JComponent button = (JComponent) field.get(view);
            Method addActionListener = button.getClass().getMethod("add" + listenerType.getSimpleName(), ActionListener.class);
            addActionListener.invoke(button, actionListener(presenter, listener));
            field.set(view, button);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            Logger.getLogger(ComponentListenerInjector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static void addCaretListener
    public static void addKeyListener(JPanel view, Field field, Object presenter, Method... listener) {
        try {
            JButton button = (JButton) field.get(view);
            button.addKeyListener(keyListener(presenter, listener));
            field.set(view, button);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ComponentListenerInjector.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
