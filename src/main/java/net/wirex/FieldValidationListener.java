package net.wirex;

import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Ritchie Borja
 */
public interface FieldValidationListener extends DocumentListener, ActionListener{
    
   void validate(); 
}
