/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testfast;

import net.wirex.interfaces.Model;

/**
 *
 * @author PHVISP
 */
public class CalculatorModel extends Model {
    private String first;
    private String second;

    public CalculatorModel() {
        first = "";
        second = "";
    }

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
    
    
    
}
