/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.table;

import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import net.wirex.structures.XList;

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
        XList table = ((TableModel) getModel()).getTable();
        table.getReadWriteLock().readLock().lock();
        try {
            MyField field11 = new MyField();
            field11.setField1("hehehehehe");
            field11.setField2("james");
            MyField field22 = new MyField();
            field22.setField1("wahahaha");
            field22.setField2("jeff");
            table.add(field11);
            table.add(field22);
        } finally {
            table.getReadWriteLock().readLock().unlock();
        }
        System.out.println(AppEngine.hash(getModel()));
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
