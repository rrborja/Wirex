/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.listeners;

import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            LoggerFactory.getLogger(ComponentListenerInjector.class.getName()).error("Unable to add " + listenerType.getSimpleName() + " to " + field.getName(), ex);
        }
    }

//    public static void addCaretListener
    public static void addKeyListener(JPanel view, Field field, Object presenter, Method... listener) {
        try {
            JButton button = (JButton) field.get(view);
            button.addKeyListener(keyListener(presenter, listener));
            field.set(view, button);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            LoggerFactory.getLogger(ComponentListenerInjector.class.getName()).error("Unable to load listener.", ex);
        }

    }
}
