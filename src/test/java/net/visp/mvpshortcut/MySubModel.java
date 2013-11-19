/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut;

import net.wirex.interfaces.Model;

/**
 *
 * @author Ritchie Borja
 */
public class MySubModel extends Model {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String newValue) {
        String oldValue = text;
        this.text = newValue;
        changeSupport.firePropertyChange("text", oldValue, newValue);
    }
    
}
