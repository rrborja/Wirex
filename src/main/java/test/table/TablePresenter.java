/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.table;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;

/**
 *
 * @author ritchie
 */
public class TablePresenter extends Presenter {

    public TablePresenter(Model model, JPanel panel) {
        super(model, panel);
    }
    
    public void read() {
        TableModel model = (TableModel)getModel();
        MyField field = model.getSelectedRow();
        model.setField1(String.valueOf(field.getField1()));
        model.setField2(String.valueOf(field.getField2()));
    }
    
    public void submit2() {
        System.out.println("hehe");
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
