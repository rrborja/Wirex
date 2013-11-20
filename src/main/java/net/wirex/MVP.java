package net.wirex;

import java.awt.Window;
import javax.swing.JPanel;

/**
 *
 * @author Ritchie Borja
 */
public interface MVP {
    JPanel getView();
    void setTitle(String title);
    void display(Class<? extends Window> window, Boolean isVisible);
}
