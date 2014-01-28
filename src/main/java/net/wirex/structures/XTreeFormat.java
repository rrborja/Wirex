package net.wirex.structures;

import ca.odell.glazedlists.TreeList;
import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Ritchie Borja
 */
public final class XTreeFormat implements TreeList.Format {

    private final Object model;
    private final Class modelClass;

    public XTreeFormat(Object model) {
        this.model = model;
        this.modelClass = model.getClass();
    }

    @Override
    public void getPath(List path, Object element) {
        XNode node = (XNode) element;
        path.addAll(node.getPath());
        path.add(node);
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
