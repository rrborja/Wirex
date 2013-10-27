/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Borja
 */
public class XTree<T> {

    public static class Node<T> {
        private T value;
        private Node<T> parent;
        private List<Node<T>> children;
    }
    
    private Node<T> node;
    
    public XTree() {
        node.children = new ArrayList<>();
    }

    public Node<T> getNode() {
        return node;
    }

    public void setNode(Node<T> node) {
        this.node = node;
    }
    
    public T getValue() {
        return (T) node.value;
    }
    
    public Node<T> getParent() {
        return node.parent;
    }
    
    public boolean addNode(Node<T> value) {
        return node.children.add(value);
    }
    
    public boolean removeNode(Node<T> value) {
        return node.children.remove(value);
    }
    
    public Iterator iterator() {
        return node.children.iterator();
    }
    
    
}
