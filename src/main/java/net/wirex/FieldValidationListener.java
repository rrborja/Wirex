package net.wirex;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Ritchie Borja
 */
public interface FieldValidationListener extends DocumentListener, ActionListener, ItemListener {
    
   boolean validate(); 
}
