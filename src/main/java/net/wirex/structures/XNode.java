package net.wirex.structures;

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
