/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wirex.annotations;

import java.awt.Window;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.swing.JPanel;

/**
 *
 * @author RBORJA
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Fire {
    Class<? extends JPanel> view();
    Class<? extends Window> type();
}
