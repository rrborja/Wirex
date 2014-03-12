package net.wirex.exceptions;

/**
 *
 * @author Ritchie Borja
 */
public class InvalidKeywordFromBindingNameException extends RuntimeException {
    
    private final String token;
    
    public InvalidKeywordFromBindingNameException(String token) {
        super();
        this.token = token;
    }
    
    public String getInvalidToken() {
        return token;
    }
    
}
