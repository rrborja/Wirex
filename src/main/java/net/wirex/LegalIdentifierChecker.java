/*
 * The MIT License
 *
 * Copyright 2013 Ritchie Borja.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
