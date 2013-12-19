package net.wirex.interfaces;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.PresenterModel;
import net.wirex.ServerRequest;
import net.wirex.ServerResponse;
import net.wirex.enums.Media;
import net.wirex.exceptions.EventInterruptionException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Ritchie Borja
 */
public abstract class Presenter {
    
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Presenter.class.getSimpleName());

    private Model model;
    private JPanel view;
    private String path;
    private Media media;
    private Model form;
    private Model domain;
    private String rest;

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
        return request(path, new HashMap());
    }

    public ServerResponse call(Map<String, String> args) {
        return request(path, args);
    }

    public void interrupt(String msg) throws EventInterruptionException {
        throw new EventInterruptionException(msg);
    }

    public ServerResponse submit(Map form) {
        return submit(form, new HashMap());
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
