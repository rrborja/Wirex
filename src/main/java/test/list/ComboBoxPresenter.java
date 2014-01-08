/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package test.list;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.annotations.Retrieve;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author ritchie
 */
public class ComboBoxPresenter extends Presenter {

    public ComboBoxPresenter(Model model, JPanel panel) {
        super(model, panel);
    }

    public void go() {
        System.out.println(((ComboBoxModel)getModel()).getSelected());
//        System.out.println(touch(JComboBox.class, "jComboBox1").getSelectedItem());
    }
    
    @Override
    public void run(@Retrieve({}) ConcurrentHashMap<String, Invoker> methods) {

    }
    
}
