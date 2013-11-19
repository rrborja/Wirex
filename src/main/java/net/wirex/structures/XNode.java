/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.structures;

import com.google.common.collect.Lists;

/**
 *
 * @author Ritchie Borja
 */
public class XNode<T> {

    private final T[] path;
    private final T leaf;

    public XNode(T[] path, T leaf) {
        this.path = path;
        this.leaf = leaf;
    }

    public T[] getPath() {
        return path;
    }

    public T getLeaf() {
        return leaf;
    }

    @Override
    public String toString() {
        return leaf.toString();
    }
    
    
}
