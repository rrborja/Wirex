/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.listeners;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static net.wirex.listeners.ListenerFactory.actionListener;

/**
 *
 * @author RBORJA
 */
public class JTextFieldListener extends ListenerFactory {
    
    private JTextFieldListener() {}
    
    public static void addActionListener(JPanel view, Field field, Object presenter, Method listener) throws IllegalArgumentException, IllegalAccessException {
        JTextField textfield = (JTextField) field.get(view);
        textfield.addActionListener(actionListener(presenter, listener));
        field.set(view, textfield);
    }
    
    public static void addKeyListener(JPanel view, Field field, Object presenter, Method... listener) throws IllegalArgumentException, IllegalAccessException {
        JTextField textfield = (JTextField) field.get(view);
        textfield.addKeyListener(keyListener(presenter, listener));
        field.set(view, textfield);
    }
}
