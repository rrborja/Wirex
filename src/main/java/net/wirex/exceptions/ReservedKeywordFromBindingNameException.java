package net.wirex.exceptions;

/**
 *
 * @author Ritchie Borja
 */
public class ReservedKeywordFromBindingNameException extends RuntimeException {
    
    private final String token;
    
    public ReservedKeywordFromBindingNameException(String token) {
        super();
        this.token = token;
    }
    
    public String getInvalidToken() {
        return token;
    }
    
}
