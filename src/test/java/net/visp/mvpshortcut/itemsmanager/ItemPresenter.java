/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut.itemsmanager;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.Invoker;
import net.wirex.interfaces.Model;
import net.wirex.interfaces.Presenter;
import net.wirex.structures.XList;

/**
 *
 * @author jvallar
 */
public class ItemPresenter extends Presenter {

    public ItemPresenter(Model model, JPanel panel) {
        super(model, panel);       
    }
    
    public void update(){
        System.err.println("testasdasdasd");
        ItemModel model = (ItemModel) super.getModel();
        
        XList<Item> arrayListModel = new XList();
        
        arrayListModel.add(new Item("Swing", "Swing","Swing","Swing", "Swing","Swing","Swing",new Date(),"Swing"));
        arrayListModel.add(new Item("Swing", "Swing","Swing","Swing", "Swing","Swing","Swing",new Date(),"Swing"));
        arrayListModel.add(new Item("Swing", "Swing","Swing","Swing", "Swing","Swing","Swing",new Date(),"Swing"));
        arrayListModel.add(new Item("Swing", "Swing","Swing","Swing", "Swing","Swing","Swing",new Date(),"Swing"));

//        model.getTable().addAll(arrayListModel);
        model.setTable(arrayListModel);
//        model.getTable().clear();
//        model.getTable().addAll(arrayListModel);
        model.setFind("test");
    }

    @Override
    public void run(ConcurrentHashMap<String, Invoker> methods) {
        
        
        
    }
}
