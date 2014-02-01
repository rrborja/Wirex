/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.list;

import net.wirex.annotations.Path;
import net.wirex.interfaces.Model;
import net.wirex.structures.XList;

/**
 *
 * @author ritchie
 */
public class ComboBoxModel extends Model {

    @Path("component/state/list")
    private XList combo;
    private String selected;
    private String select;
    private boolean check;
    private boolean radio;

    public String getSelect() {
        return select;
    }

    public void setSelect(String newValue) {
        String oldValue = select;
        this.select = newValue;
        fireChanges("select", oldValue, newValue);
    }

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

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean newValue) {
        Boolean oldValue = check;
        this.check = newValue;
        fireChanges("check", oldValue, newValue);
    }

    public boolean isRadio() {
        return radio;
    }

    public void setRadio(boolean newValue) {
        Boolean oldValue = radio;
        this.radio = newValue;
        fireChanges("radio", oldValue, newValue);
    }

}
