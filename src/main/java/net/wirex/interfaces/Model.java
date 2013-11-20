package net.wirex.interfaces;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Ritchie Borja
 */
public abstract class Model {

    protected transient PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }
    
    public void fireChanges(String fieldName, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(fieldName, oldValue, newValue);
    }
    
}
