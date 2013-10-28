/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.structures;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TreeList;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.wirex.annotations.PathNode;

/**
 *
 * @author RBORJA
 */
public class XTreeFormat implements TreeList.Format {

    private Object model;
    private Class modelClass;

    public XTreeFormat(Object model) {
        this.model = model;
        this.modelClass = model.getClass();
    }

    @Override
    public void getPath(List path, Object element) {
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(model).equals(element)) {
                    PathNode pathNode = field.getAnnotation(PathNode.class);
                    if (pathNode != null) {
                        String[] pathToNode = pathNode.value();
                        path.addAll(Lists.newArrayList(pathToNode));
                        path.add(element);
                    } else {
                        path.add(element);
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(XTreeFormat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean allowsChildren(Object element) {
        return true;
    }

    @Override
    public Comparator getComparator(int depth) {
        return null;
    }
}
