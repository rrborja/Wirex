/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.datepicker;

import java.util.Date;
import net.wirex.interfaces.Model;

/**
 *
 * @author ritchie
 */
public class DatePickerModel extends Model {
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date newValue) {
        Date oldValue = date;
        this.date = newValue;
        fireChanges("date", oldValue, newValue);
    }
    
}
