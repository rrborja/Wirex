/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.wirex.enums;

/**
 *
 * @author RBORJA
 */
public enum Media {
    XML("application/xml"), JSON("application/json"), URLENCODED("application/x-www-form-urlencoded");
    
    private String value;
    
    Media(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
    
    
}
