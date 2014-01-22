package net.wirex.interfaces;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.wirex.AppEngine;
import net.wirex.structures.XModelListener;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
public abstract class Model {
    
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Model.class.getSimpleName());

    protected transient PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private transient String hashValue = AppEngine.hash(this);

    private transient Map undoObject = synchronize();

    private transient XModelListener listener = new XModelListener() {
        @Override
        public void modelChanged() {
        }

        @Override
        public void modelUnchanged() {
        }
    };

    private Map synchronize() {
        Map<String, Object> map = new HashMap<>(5);
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                map.put(fieldName, field.get(this));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                map.put(fieldName, null);
            }
            field.setAccessible(false);
        }
        return map;
    }

    public Map getUndoObject() {
        return Collections.unmodifiableMap(undoObject);
    }

    public void undo() {
        Method[] methods = getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().contains("set")) {
                String methodName = retrieveSetterProperty(method.getName());
                try {
                    method.invoke(this, getUndoObject().get(methodName));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOG.warn("Can't process field {}.{}", getClass().toString(), methodName);
                }
            }
        }
    }

    private String retrieveSetterProperty(String methodName) {
        String result = methodName.replace("set", "");
        result = Character.toLowerCase(result.charAt(0)) + result.substring(1);
        return result;
    }

    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void fireChanges(String fieldName, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(fieldName, oldValue, newValue);
        if (isChanged()) {
            listener.modelUnchanged();
        } else {
            listener.modelChanged();
        }
    }

    public boolean isChanged() {
        return hashValue.equals(AppEngine.hash(this));
    }

    public void store() {
        hashValue = AppEngine.hash(this);
        undoObject = synchronize();
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
