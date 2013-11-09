/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import java.awt.Window;
import javax.swing.JPanel;

/**
 *
 * @author RBORJA
 */
public interface MVP {
    JPanel getView();
    void setTitle(String title);
    void display(Class<? extends Window> window, Boolean isVisible);
}
