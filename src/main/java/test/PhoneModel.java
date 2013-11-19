/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.gson.annotations.Expose;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import net.wirex.interfaces.Model;
import net.wirex.structures.XList;

/**
 *
 * @author Ritchie Borja
 */
public class PhoneModel extends Model implements Serializable {
    
    private String id;
    private @Expose String label;
    private @Expose XList<Integer> phoneNumbers;

    public PhoneModel() {
        phoneNumbers = new XList();
        phoneNumbers.add(4);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String newValue) {
        String oldValue = id;
        this.id = newValue;
        changeSupport.firePropertyChange("id", oldValue, newValue);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String newValue) {
        String oldValue = label;
        this.label = newValue;
        changeSupport.firePropertyChange("label", oldValue, newValue);
    }

    public XList getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(XList newValue) {
        XList oldValue = (XList)phoneNumbers;
        this.phoneNumbers.setList(newValue.getList());
        changeSupport.firePropertyChange("phoneNumbers", oldValue, newValue);
    }

    @Override
    public String toString() {
        return "PhoneModel{" + "id=" + id + ", label=" + label + ", phoneNumbers=" + phoneNumbers + '}';
    }
    
    
    
}
