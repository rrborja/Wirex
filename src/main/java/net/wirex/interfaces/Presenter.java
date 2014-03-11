package net.wirex.interfaces;

import java.awt.Cursor;
import java.awt.Window;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.PresenterModel;
import net.wirex.ServerRequest;
import net.wirex.ServerResponse;
import net.wirex.enums.Media;
import net.wirex.exceptions.EventInterruptionException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
public abstract class Presenter {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Presenter.class.getSimpleName());

    private static final Lock lock = new ReentrantLock();

    private Model model;
    private JPanel view;
    private JMenuBar menu;
    private String path;
    private Media media;
    private Model form;
    private Model domain;
    private String rest;
//    private final Map<String, Object> undoManager = new HashMap<>(5);

    public Presenter(JMenuBar menu) {
        this.menu = menu;
    }

    public Presenter(Model model, JPanel panel) {
        this.model = model;
        this.view = panel;
    }

    protected Model getModel() {
        return model;
    }

    /**
     * *
     *
     * @param model
     * @deprecated Replacing the model will lose all bindings to all components
     */
    @Deprecated
    protected void setModel(Model model) {
        this.model = model;
    }

    @Deprecated
    public JPanel getPanel() {
        return view;
    }

    public void store() {
        Map<String, Object> map = new HashMap<>(5);
        Class modelClass = model.getClass();
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String property = field.getName();
            try {
                map.put(property, field.get(model));
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                LOG.warn("Can't process field {}.{}", model.getClass().toString(), property);
                map.put(property, null);
            }
            field.setAccessible(false);
        }
        this.model.undoObject.clear();
        this.model.undoObject.putAll(map);
    }

    private String retrieveSetterProperty(String methodName) {
        String result = methodName.replace("set", "");
        result = Character.toLowerCase(result.charAt(0)) + result.substring(1);
        return result;
    }

    /**
     *
     * @deprecated Use in the model class undo()
     */
    @Deprecated
    public void undo() {
        Class modelClass = model.getClass();
        Method[] methods = modelClass.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = retrieveSetterProperty(method.getName());
            try {
                method.invoke(model, model.getUndoObject().get(methodName));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOG.warn("Can't process field {}.{}", model.getClass().toString(), methodName);
            }
        }
    }

    public void clear() {
        Class modelClass = model.getClass();
        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            String property = field.getName();
            if (!Modifier.isTransient(modifiers)) {
                try {
                    if (field.getType() == String.class) {
                        PropertyUtils.setProperty(model, property, "");
                    } else if (field.getType() == Boolean.class || field.getType().toString().equals("boolean")) {
                        PropertyUtils.setProperty(model, property, false);
                    } else if (field.getType() == Integer.class || field.getType().toString().equals("int")) {
                        PropertyUtils.setProperty(model, property, 0);
                    } else if (field.getType() == Double.class || field.getType().toString().equals("double")) {
                        PropertyUtils.setProperty(model, property, 0.0);
                    } else if (field.getType() == Float.class || field.getType().toString().equals("float")) {
                        PropertyUtils.setProperty(model, property, 0.0);
                    } else if (field.getType() == Character.class || field.getType().toString().equals("char")) {
                        PropertyUtils.setProperty(model, property, null);
                    }

                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    Logger.getLogger(Presenter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     *
     * @param <T>
     * @param componentClass
     * @param componentName
     * @return
     * @deprecated Because it's cheating and also a security problem
     */
    @Deprecated
    protected <T> T touch(Class<T> componentClass, String componentName) {
        Class viewClass = view.getClass();
        try {
            Field componentField = viewClass.getDeclaredField(componentName);
            componentField.setAccessible(true);
            return (T) componentField.get(view);
        } catch (NoSuchFieldException ex) {
            LOG.error("Field {} not found in {}", componentName, viewClass.getName());
        } catch (SecurityException ex) {
            Logger.getLogger(Presenter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Presenter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Presenter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    protected void setPanel(JPanel panel) {
        this.view = panel;
    }

    public ServerResponse call() {
        return request(path, new HashMap(0));
    }

    public ServerResponse call(Map<String, String> args) {
        return request(path, args);
    }

    public void interrupt(String msg) throws EventInterruptionException {
        throw new EventInterruptionException(msg);
    }

    public ServerResponse submit(Map form) {
        return submit(form, new HashMap(0));
    }

    public ServerResponse submit(Map form, Map<String, String> args) {
        ArrayList<Boolean> editable = busy();
        try {
            PresenterModel presenterModel = new PresenterModel(form);
            ServerRequest request = new ServerRequest(rest, path, media, args, presenterModel, view);
            ServerResponse response = AppEngine.push(request);
            return response;
        } finally {
            idle(editable);
        }
    }

    private void init(String path, Media media, String rest) {
        this.path = path;
        this.media = media;
        this.rest = rest;
    }

    private void init(String path, Media media, String rest, Model form) {
        this.path = path;
        this.media = media;
        this.rest = rest;
        this.form = form;
    }

    /**
     *
     * @param path
     * @param media
     * @param rest
     * @param form
     * @param domain
     * @deprecated Domain never used
     */
    @Deprecated
    private void init(String path, Media media, String rest, Model form, Model domain) {
        this.path = path;
        this.media = media;
        this.rest = rest;
        this.form = form;
        this.domain = domain;
    }

    private ServerResponse request(String path, Map variables) {
        ArrayList<Boolean> editable = busy();
        try {
            ServerRequest request = new ServerRequest(rest, path, media, variables, model, view);
            ServerResponse response = AppEngine.push(request);
            if (response.isSerializable()) {
                AppEngine.deserialize(model, (Model) response.getMessage());
            }
            return response;
        } finally {
            idle(editable);
        }
    }

    private static final transient Cursor WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
    private static final transient Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

    private void setBusyCursor(boolean enable) {
        Window window = SwingUtilities.getWindowAncestor(view);
        if (window == null) {
            return;
        }
        if (enable) {
            window.setCursor(WAIT_CURSOR);
        } else {
            window.setCursor(DEFAULT_CURSOR);
        }
    }

    protected synchronized ArrayList<Boolean> busy() {
        setBusyCursor(true);
        ArrayList<Boolean> editable = new ArrayList<>();
        List<JComponent> components = (List) model.getComponents();
        for (JComponent component : components) {
            editable.add(component.isEnabled());
            component.setEnabled(false);
        }
        return editable;
    }

    protected synchronized void idle(ArrayList<Boolean> editable) {
        List<JComponent> components = (List) model.getComponents();
        Iterator<Boolean> iter = editable.iterator();
        for (JComponent component : components) {
            component.setEnabled(iter.next());
        }
        setBusyCursor(false);
    }

    public void onBackground() {
        throw new UnsupportedOperationException("onBackground is not implemented in your presenter " + this.getClass().getName());
    }

    public abstract void run(ConcurrentHashMap<String, Invoker> methods);
}
