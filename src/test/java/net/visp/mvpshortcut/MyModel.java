/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import net.visp.wirex.interfaces.Model;

/**
 *
 * @author RBORJA
 */
public class MyModel extends Model {

    transient private String name;
    transient private Boolean check;
    transient private String newText;
    private String lastname;
    private String phoneLabel;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String newValue) {
        String oldValue = lastname;
        this.lastname = newValue;
        changeSupport.firePropertyChange("lastname", oldValue, newValue);
    }

    public String getPhoneLabel() {
        return phoneLabel;
    }

    public void setPhoneLabel(String phoneLabel) {
        this.phoneLabel = phoneLabel;
    }

    public String getName() {
        return name;
    }

    public String getNewText() {
        return newText;
    }

    public void setNewText(String newValue) {
        String oldValue = this.newText;
        this.newText = newValue;
        changeSupport.firePropertyChange("newText", oldValue, newValue);
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean newValue) {
        Boolean oldValue = check;
        this.check = newValue;
        changeSupport.firePropertyChange("check", oldValue, newValue);
    }

    public void setName(String newValue) {
        String oldValue = name;
        this.name = newValue;
        changeSupport.firePropertyChange("name", oldValue, newValue);
    }
}
