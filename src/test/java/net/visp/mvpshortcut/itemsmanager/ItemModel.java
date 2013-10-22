/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut.itemsmanager;

import java.util.Date;
import net.wirex.interfaces.Model;
import net.wirex.structures.XList;

/**
 *
 * @author jvallar
 */
public class ItemModel extends Model {
    XList<Item> table;
    String find;
    
    public ItemModel() {
        table = new XList();
        table.add(new Item("Swing", "Swing","Swing","Swing", "Swing","Swing","Swing",new Date(),"Swing"));
    }

    public String getFind() {
        return find;
    }

    public void setFind(String newValue) {
        String oldValue = find;
        this.find = newValue;
        changeSupport.firePropertyChange("find", oldValue, newValue);
    }

    public XList<Item> getTable() {
        return table;
    }

    public void setTable(XList<Item> newTable) {
        XList<Item> oldValue = (XList) table;
        this.table.setList(newTable.getList());
        changeSupport.firePropertyChange("table", oldValue, newTable);
    }
}
