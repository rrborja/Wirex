/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.wirex.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author RBORJA
 */
public class JButtonListener extends ListenerFactory {

    private JButtonListener() {}
    
    public static void addActionListener(JPanel view, Field field, Object presenter, Method listener) throws IllegalArgumentException, IllegalAccessException {
        JButton button = (JButton) field.get(view);
        button.addActionListener(actionListener(presenter, listener));
        field.set(view, button);
    }

//    public static void addCaretListener
    
    public static void addKeyListener(JPanel view, Field field, Object presenter, Method... listener) throws IllegalArgumentException, IllegalAccessException {
        JButton button = (JButton) field.get(view);
        button.addKeyListener(keyListener(presenter, listener));
        field.set(view, button);
    }
    
    
}
