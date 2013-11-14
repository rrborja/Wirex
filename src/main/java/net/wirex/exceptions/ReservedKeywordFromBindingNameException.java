/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wirex.exceptions;

/**
 *
 * @author RBORJA
 */
public class ReservedKeywordFromBindingNameException extends Exception {
    
    private final String token;
    
    public ReservedKeywordFromBindingNameException(String token) {
        super();
        this.token = token;
    }
    
    public String getInvalidToken() {
        return token;
    }
    
}
