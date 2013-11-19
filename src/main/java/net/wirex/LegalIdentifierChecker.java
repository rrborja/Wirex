/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import java.util.Arrays;
import net.wirex.exceptions.InvalidKeywordFromBindingNameException;
import net.wirex.exceptions.ReservedKeywordFromBindingNameException;

/**
 *
 * @author Ritchie Borja
 */
public class LegalIdentifierChecker {

    private final static String[] keywords = new String[]{"abstract", ""};

    private static void isReservedKeyword(String token)
            throws ReservedKeywordFromBindingNameException {
        if (Arrays.asList(keywords).contains(token)) {
            throw new ReservedKeywordFromBindingNameException(token);
        }
    }

    private static void isValidIdentifier(String token)
            throws InvalidKeywordFromBindingNameException {
        if (!Character.isJavaIdentifierStart(token.charAt(0))) {
            throw new InvalidKeywordFromBindingNameException(token);
        }
        for (int i = 1; i < token.length(); i++) {
            char tokenPointer = token.charAt(i);
            if (!Character.isJavaIdentifierPart(tokenPointer)) {
                throw new InvalidKeywordFromBindingNameException(token);
            }
        }
    }

    public static String check(String token)
            throws InvalidKeywordFromBindingNameException,
            ReservedKeywordFromBindingNameException {
        isValidIdentifier(token);
        isReservedKeyword(token);
        return token;
    }

}
