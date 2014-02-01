/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.datepicker;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author ritchie
 */
public class DatePickerPresenter extends Presenter {

    public DatePickerPresenter(Model model, JPanel panel) {
        super(model, panel);
    }
    
    public void go() {
        DatePickerModel model = (DatePickerModel) getModel();
        model.setDate(new Date(2011,01,20));
    }
    
    public void display() {
        DatePickerModel model = (DatePickerModel) getModel();
        System.out.println(model.getDate());
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        
    }
    
}
