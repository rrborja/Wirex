/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.listeners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static net.wirex.listeners.ListenerFactory.actionListener;

/**
 *
 * @author RBORJA
 */
public class JTextFieldListener extends ListenerFactory {

    private JTextFieldListener() {
    }

    public static void addActionListener(JPanel view, Field field, Object presenter, Method listener) {
        try {
            JTextField textfield = (JTextField) field.get(view);
            textfield.addActionListener(actionListener(presenter, listener));
            field.set(view, textfield);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            LoggerFactory.getLogger(JTextFieldListener.class.getName()).error("Cannot add ActionListener to " + field.getName(), ex);
        }
    }

    public static void addKeyListener(JPanel view, Field field, Object presenter, Method... listener) {
        try {
            JTextField textfield = (JTextField) field.get(view);
            textfield.addKeyListener(keyListener(presenter, listener));
            field.set(view, textfield);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            LoggerFactory.getLogger(JTextFieldListener.class.getName()).error("Cannot add KeyListener to " + field.getName(), ex);
        }
    }
}
