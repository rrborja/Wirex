package net.wirex;

import java.awt.Window;
import javax.swing.JPanel;

/**
 *
 * @author Ritchie Borja
 */
public interface MVP {

    /**
     * Gets the panel of the binded panel.
     *
     * @return The panel itself
     */
    JPanel getView();

    /**
     * Sets the title text of the window to be fired.
     *
     * @param title The string to display the title text
     */
    void setTitle(String title);

    /**
     * Specifies what type of window and automatically displays it
     *
     * @param window Window type (JFrame or JDialog)
     */
    void display(Class<? extends Window> window);

    /**
     * Specifies what type of window either JFrame or JDisplay.
     *
     * @param window Window type (JFrame or JDialog)
     * @param isVisible Option to display or not to display
     */
    void display(Class<? extends Window> window, Boolean isVisible);

    /**
     * Sets the ability of the window to resize, preventing the user from
     * resizing it.
     *
     * @param resizable value to allow user resizing the window
     */
    void setResizable(boolean resizable);

}
