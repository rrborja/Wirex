/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.structures;

/**
 * Data structure for Wirex simple object. Useful when a value's type is a
 * primary data type.
 * 
 * @author Ritchie Borja
 */
public class XObject {

    private Object value;

    public XObject(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
