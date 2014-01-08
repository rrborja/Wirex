/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.list;

import net.wirex.interfaces.Model;
import net.wirex.structures.XList;

/**
 *
 * @author ritchie
 */
public class ComboBoxModel extends Model {

//    @Path("components/combobox")
    private XList combo;
    private String selected;

    public ComboBoxModel() {
        combo = new XList();
        combo.add("haha");
        combo.add("huhu");
        combo.add("hehe");
        selected = "hi";
    }

    public XList getCombo() {
        return combo;
    }

    public void setCombo(XList newValue) {
        XList oldValue = combo;
        this.combo = newValue;
        fireChanges("combo", oldValue, newValue);
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String newValue) {
        String oldValue = selected;
        this.selected = newValue;
        fireChanges("selected", oldValue, newValue);
    }

}
