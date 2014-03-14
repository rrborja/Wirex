package net.wirex.structures;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
public class XMap<K, V> implements Map<K, V> {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(XMap.class.getSimpleName());
    ConcurrentMap<K, V> map;

    public XMap() {
        this(10);
    }

    public XMap(int capacity) {
        map = new ConcurrentHashMap<K, V>(capacity);
    }

    public V put(K key, V value) {
        try {
            return map.put(key, value);
        } finally {
            synchronized (this) {
                notify();
            }
        }
    }

    public boolean containsKey(Object key) {
        V v = get(key);
        return v != null;
    }

    public V get(Object key) {
        while (!map.containsKey(key)) {
            try {
                synchronized (this) {
                    wait(5000);
                }
                if (map.containsKey(key)) {
                    return map.get(key);
                } else {
                    return null;
                }
            } catch (InterruptedException ex) {
                LOG.warn("Attempting to get {} but interrupted", key);
                return null;
            }
        }
        return map.get(key);
    }

    public V remove(Object key) {
        return map.remove(key);
    }

    public Set keySet() {
        return map.keySet();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
