/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut.itemsmanager;

import java.util.Date;
import javax.swing.JPanel;
import net.visp.wirex.interfaces.Model;
import net.visp.wirex.interfaces.Presenter;
import net.visp.wirex.structures.XList;

/**
 *
 * @author jvallar
 */
public class ItemPresenter extends Presenter {

    public ItemPresenter(Model model, JPanel panel) {
        super(model, panel);       
    }
    
    public void update(){
        System.err.println("test");
        ItemModel model = (ItemModel) super.getModel();
        
        XList<Item> arrayListModel = new XList<>();
        
        arrayListModel.add(new Item("Swing", "Swing","Swing","Swing", "Swing","Swing","Swing",new Date(),"Swing"));
        arrayListModel.add(new Item("Swing", "Swing","Swing","Swing", "Swing","Swing","Swing",new Date(),"Swing"));
        arrayListModel.add(new Item("Swing", "Swing","Swing","Swing", "Swing","Swing","Swing",new Date(),"Swing"));
        arrayListModel.add(new Item("Swing", "Swing","Swing","Swing", "Swing","Swing","Swing",new Date(),"Swing"));

        
        model.setTable(arrayListModel);
        model.setFind("test");
    }
}
