package net.wirex.enums;

import org.springframework.http.MediaType;

/**
 *
 * @author Ritchie Borja
 */
public enum Media {
    XML(MediaType.APPLICATION_XML), JSON(MediaType.APPLICATION_JSON), URLENCODED(MediaType.APPLICATION_FORM_URLENCODED);
    
    private final MediaType value;
    
    Media(MediaType value) {
        this.value = value;
    }

    public MediaType value() {
        return value;
    }
    
    
}
