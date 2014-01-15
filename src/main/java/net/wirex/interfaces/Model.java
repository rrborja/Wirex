package net.wirex.interfaces;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import net.wirex.AppEngine;
import net.wirex.structures.XModelListener;

/**
 *
 * @author Ritchie Borja
 */
public abstract class Model {

    protected transient PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private transient String hashValue = "";

    private transient XModelListener listener = new XModelListener() {
        @Override
        public void modelChanged() {
        }

        @Override
        public void modelUnchanged() {
        }
    };

    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void fireChanges(String fieldName, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(fieldName, oldValue, newValue);
        if (hashValue.equals(AppEngine.hash(this))) {
            listener.modelUnchanged();
        } else {
            listener.modelChanged();
        }
    }

    public void store() {
        hashValue = AppEngine.hash(this);
    }

    public void addModelListener(XModelListener listener) {
        this.listener = listener;
    }

    public Object streamData() {
        throw new UnsupportedOperationException("You need to override this method in " + this.getClass().getSimpleName());
    }

    public Class streamType() {
        throw new UnsupportedOperationException("You need to override this method in " + this.getClass().getSimpleName());
    }
}
