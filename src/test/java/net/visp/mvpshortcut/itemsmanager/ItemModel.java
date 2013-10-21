/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.mvpshortcut.itemsmanager;

import java.util.Date;
import net.visp.wirex.interfaces.Model;
import net.visp.wirex.structures.XList;

/**
 *
 * @author jvallar
 */
public class ItemModel extends Model {
    XList<Item> table;
    String find;
    
    public ItemModel() {
        table = new XList();
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
