/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.wirex.structures;

import ca.odell.glazedlists.AbstractEventList;
import ca.odell.glazedlists.event.ListEventAssembler;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.event.ListEventPublisher;
import ca.odell.glazedlists.util.concurrent.LockFactory;
import ca.odell.glazedlists.util.concurrent.ReadWriteLock;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author RBORJA
 */
public final class XList<T> extends AbstractEventList<T> {

    private List<T> list;

    public XList() {
        this(LockFactory.DEFAULT.createReadWriteLock());
    }

    public XList(ReadWriteLock readWriteLock) {
        this(null, readWriteLock);
    }

    public XList(int initialCapacity) {
        this(initialCapacity, null, LockFactory.DEFAULT.createReadWriteLock());
    }

    public XList(ListEventPublisher publisher, ReadWriteLock readWriteLock) {
        this(10, publisher, readWriteLock);
    }

    public XList(int initialCapacity, ListEventPublisher publisher, ReadWriteLock readWriteLock) {
        super(publisher);
        this.list = new ArrayList<>(initialCapacity);
        this.readWriteLock = readWriteLock;
    }

    public XList(List<T> list) {
        super(null);
        this.list = list;
        this.readWriteLock = LockFactory.DEFAULT.createReadWriteLock();
    }

    public List getList() {
        return list;
    }
    
    public void setList(List list) {
        this.list = list;
    }

    @Override
    public boolean add(T value) {
        updates.beginEvent();
        updates.elementInserted(size(), value);
        boolean result = list.add(value);
        updates.commitEvent();
        return result;
    }

    @Override
    public void add(int index, T value) {
        updates.beginEvent();
        updates.elementInserted(index, value);
        list.add(index, value);
        updates.commitEvent();
    }

    @Override
    public boolean addAll(Collection<? extends T> values) {
        return addAll(size(), values);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> values) {
        if (values.isEmpty()) {
            return false;
        }
        updates.beginEvent();
        for (Iterator<? extends T> i = values.iterator(); i.hasNext();) {
            T value = i.next();
            updates.elementInserted(index, value);
            list.add(index, value);
            index++;
        }
        updates.commitEvent();
        return !values.isEmpty();
    }

    @Override
    public boolean remove(Object toRemove) {
        int index = list.indexOf(toRemove);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    public void clear() {
        if (isEmpty()) {
            return;
        }
        updates.beginEvent();
        for (int i = 0, size = size(); i < size; i++) {
            updates.elementDeleted(0, get(i));
        }
        list.clear();
        updates.commitEvent();
    }

    @Override
    public T remove(int index) {
        updates.beginEvent();
        T removed = list.remove(index);
        updates.elementDeleted(index, removed);
        updates.commitEvent();
        return removed;
    }

    @Override
    public T set(int index, T value) {
        updates.beginEvent();
        T previous = list.set(index, value);
        updates.elementUpdated(index, value);
        updates.commitEvent();
        return previous;
    }

    @Override
    public boolean removeAll(Collection<?> values) {
        boolean changed = false;
        updates.beginEvent();
        for (Iterator i = values.iterator(); i.hasNext();) {
            Object value = i.next();
            int index;
            while ((index = indexOf(value)) != -1) {
                T removed = list.remove(index);
                updates.elementDeleted(index, removed);
                changed = true;
            }
        }
        updates.commitEvent();
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> values) {
        boolean changed = false;
        updates.beginEvent();
        int index = 0;
        while (index < list.size()) {
            if (values.contains(list.get(index))) {
                index++;
            } else {
                T removed = list.remove(index);
                updates.elementDeleted(index, removed);
                changed = true;
            }
        }
        updates.commitEvent();
        return changed;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public void dispose() {
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        T[] elements = (T[]) list.toArray(new Object[list.size()]);

        List<ListEventListener<T>> serializableListeners = new ArrayList<ListEventListener<T>>(1);
        for (Iterator<ListEventListener<T>> i = updates.getListEventListeners().iterator(); i.hasNext();) {
            ListEventListener<T> listener = i.next();
            if (!(listener instanceof Serializable)) {
                continue;
            }
            serializableListeners.add(listener);
        }
        ListEventListener[] listeners = serializableListeners.toArray(new ListEventListener[serializableListeners.size()]);

        out.writeObject(elements);
        out.writeObject(listeners);
        out.writeObject(getPublisher());
        out.writeObject(getReadWriteLock());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        final T[] elements = (T[]) in.readObject();
        final ListEventListener<T>[] listeners = (ListEventListener<T>[]) in.readObject();

        try {
            this.publisher = (ListEventPublisher) in.readObject();
            this.updates = new ListEventAssembler<>(this, publisher);
            this.readWriteLock = (ReadWriteLock) in.readObject();
        } catch (OptionalDataException e) {
            if (e.eof)
            {
                this.readWriteLock = LockFactory.DEFAULT.createReadWriteLock();
            } else {
                throw e;
            }
        }
        this.list = new ArrayList<>(elements.length);
        this.list.addAll(Arrays.asList(elements));

        for (int i = 0; i < listeners.length; i++) {
            this.updates.addListEventListener(listeners[i]);
        }
    }

    @Override
    public String toString() {
        return list.toString();
    }
    
    
}
