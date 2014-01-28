package net.wirex.structures;

import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.Icon;
import net.wirex.AppEngine;

/**
 *
 * @author Ritchie Borja
 */
public final class XNode<T> {

    private final T[] path;
    private final T leaf;

    public XNode(T[] path, T leaf) {
        this.path = path;
        this.leaf = leaf;
    }

    public List getPath() {
        return Lists.newArrayList(path);
    }

    public T getLeaf() {
        return leaf;
    }

    @Override
    public String toString() {
        return String.valueOf(leaf);
    }
    
    
}
