/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.exceptions;

/**
 *
 * @author ritchie
 */
public class UnsuccessfulServerResponseException extends RuntimeException {

    public UnsuccessfulServerResponseException(String message) {
        super(message);
    }

}
