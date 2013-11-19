/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wirex.exceptions;

/**
 *
 * @author Ritchie Borja
 */
public class InvalidKeywordFromBindingNameException extends Exception {
    
    private final String token;
    
    public InvalidKeywordFromBindingNameException(String token) {
        super();
        this.token = token;
    }
    
    public String getInvalidToken() {
        return token;
    }
    
}
