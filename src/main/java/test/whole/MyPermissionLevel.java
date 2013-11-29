/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.whole;

import net.wirex.interfaces.Model;

/**
 *
 * @author ritchie
 */
public class MyPermissionLevel extends Model {
    private boolean username;
    private boolean password;
    private boolean retype;
    private boolean email;
    private boolean phoneNumber;
    private boolean reset;

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public MyPermissionLevel() {
        username = false;
        password = true;
        retype = true;
        email = true;
        phoneNumber = true;
    }

    public boolean isUsername() {
        return username;
    }

    public void setUsername(boolean newValue) {
        boolean oldValue = username;
        this.username = newValue;
        changeSupport.firePropertyChange("username", oldValue, newValue);
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean newValue) {
        boolean oldValue = password;
        this.password = newValue;
        changeSupport.firePropertyChange("password", oldValue, newValue);
    }

    public boolean isRetype() {
        return retype;
    }

    public void setRetype(boolean newValue) {
        boolean oldValue = retype;
        this.retype = newValue;
        changeSupport.firePropertyChange("retype", oldValue, newValue);
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean newValue) {
        boolean oldValue = email;
        this.email = newValue;
        changeSupport.firePropertyChange("email", oldValue, newValue);
    }

    public boolean isPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(boolean newValue) {
        boolean oldValue = phoneNumber;
        this.phoneNumber = newValue;
        changeSupport.firePropertyChange("phoneNumber", oldValue, newValue);
    }
    
    
}
