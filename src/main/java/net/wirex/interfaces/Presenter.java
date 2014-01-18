package net.wirex.interfaces;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
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

    private Model model;
    private JPanel view;
    private JMenuBar menu;
    private String path;
    private Media media;
    private Model form;
    private Model domain;
    private String rest;
    private final Map<String, Object> undoManager = new HashMap<>(5);

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
        this.undoManager.clear();
        this.undoManager.putAll(map);
    }
    
    private String retrieveSetterProperty(String methodName) {
        String result = methodName.replace("set", "");
        result = Character.toLowerCase(result.charAt(0)) + result.substring(1);
        return result;
    }

    public void undo() {
        Class modelClass = model.getClass();
        Method[] methods = modelClass.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = retrieveSetterProperty(method.getName());
            try {
                method.invoke(model, undoManager.get(methodName));
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
        PresenterModel presenterModel = new PresenterModel(form);
        ServerRequest request = new ServerRequest(rest, path, media, args, presenterModel, view);
        ServerResponse response = AppEngine.push(request);
        return response;
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
        ServerRequest request = new ServerRequest(rest, path, media, variables, model, view);
        ServerResponse response = AppEngine.push(request);
        if (response.isSerializable()) {
            AppEngine.deserialize(model, (Model) response.getMessage());
        }
        return response;
    }

    public abstract void run(ConcurrentHashMap<String, Invoker> methods);
}
