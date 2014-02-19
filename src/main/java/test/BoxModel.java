/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import net.wirex.interfaces.Model;

/**
 *
 * @author ritchie
 */
public class BoxModel extends Model {
    private String first;
    private String second;
    private Boolean third;

    public String getFirst() {
        return first;
    }

    public void setFirst(String newValue) {
        String oldValue = first;
        this.first = newValue;
        fireChanges("first", oldValue, newValue);
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String newValue) {
        String oldValue = second;
        this.second = newValue;
        fireChanges("second", oldValue, newValue);
    }

    public Boolean isThird() {
        return third;
    }

    public void setThird(Boolean newValue) {
        Boolean oldValue = third;
        this.third = newValue;
        fireChanges("third", oldValue, newValue);
    }
    
}
