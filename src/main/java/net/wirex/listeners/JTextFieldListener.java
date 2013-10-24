/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.listeners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            Logger.getLogger(JTextFieldListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addKeyListener(JPanel view, Field field, Object presenter, Method... listener) {
        try {
            JTextField textfield = (JTextField) field.get(view);
            textfield.addKeyListener(keyListener(presenter, listener));
            field.set(view, textfield);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(JTextFieldListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
