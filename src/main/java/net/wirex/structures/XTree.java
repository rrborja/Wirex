/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.structures;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Borja
 */
public class XTree {

    private XTree() {
    }

    public static XList build(Object model) {
        XList list = new XList();
        Class modelClass = model.getClass();
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object element = field.get(model);
                list.add(element);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(XTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }
}
