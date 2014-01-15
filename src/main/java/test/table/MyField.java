/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.table;

import net.wirex.annotations.Column;
import net.wirex.interfaces.Model;

/**
 *
 * @author ritchie
 */
public class MyField extends Model {

    @Column("Field 1")
    private Object field1;
    @Column("Field 2")
    private Object field2;

    public MyField() {
        field1 = "haha";
        field2 = "hehe";
    }

    public Object getField1() {
        return field1;
    }

    public void setField1(Object field1) {
        this.field1 = field1;
    }

    public Object getField2() {
        return field2;
    }

    public void setField2(Object field2) {
        this.field2 = field2;
    }

    @Override
    public String toString() {
        return "MyField{" + "field1=" + field1 + ", field2=" + field2 + '}';
    }

}
