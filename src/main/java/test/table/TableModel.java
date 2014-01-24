/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.table;

import net.wirex.interfaces.Model;
import net.wirex.structures.XList;

/**
 *
 * @author ritchie
 */
public class TableModel extends Model {

    private String field1;
    private String field2;
    private XList<MyField> table;
    private MyField selectedRow;

    public TableModel() {
        table = new XList<>();
        table.add(new MyField());
        
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String newValue) {
        String oldValue = field1;
        this.field1 = newValue;
        fireChanges("field1", oldValue, newValue);
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String newValue) {
        String oldValue = field2;
        this.field2 = newValue;
        fireChanges("field2", oldValue, newValue);
    }

    public XList<MyField> getTable() {
        return table;
    }

    public void setTable(XList<MyField> newValue) {
        this.table = newValue;
    }

    public MyField getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(MyField selectedRow) {
        this.selectedRow = selectedRow;
    }

}
