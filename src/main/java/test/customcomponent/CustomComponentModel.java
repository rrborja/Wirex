/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.customcomponent;

import net.wirex.interfaces.Model;

/**
 *
 * @author ritchie
 */
public class CustomComponentModel extends Model {
    private String ipAddress;
    private String text33;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String newValue) {
        String oldValue = ipAddress;
        this.ipAddress = newValue;
        fireChanges("ipAddress", oldValue, newValue);
    }

    public String getText33() {
        return text33;
    }

    public void setText33(String newValue) {
        String oldValue = text33;
        this.text33 = newValue;
        fireChanges("text", oldValue, newValue);
    }
    
}
