/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.enums;

import org.springframework.http.MediaType;

/**
 *
 * @author RBORJA
 */
public enum Media {
    XML(MediaType.APPLICATION_XML), JSON(MediaType.APPLICATION_JSON), URLENCODED(MediaType.APPLICATION_FORM_URLENCODED);
    
    private MediaType value;
    
    Media(MediaType value) {
        this.value = value;
    }

    public MediaType value() {
        return value;
    }
    
    
}
