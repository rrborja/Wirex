/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.ApplicationControllerFactory;
import net.wirex.Invoker;
import net.wirex.ServerResponse;
import net.wirex.enums.Media;
import net.wirex.exceptions.EventInterruptionException;

/**
 *
 * @author RBORJA
 */
public abstract class Presenter {

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

    public Model getModel() {
        return model;
    }

    /**
     * *
     *
     * @param model
     * @deprecated Replacing the model will lose all bindings to all components
     */
    @Deprecated
    public void setModel(Model model) {
        this.model = model;
    }

    public JPanel getPanel() {
        return view;
    }

    public void setPanel(JPanel panel) {
        this.view = panel;
    }

    public Object call() {
        return request(path);
    }

    public Object call(Map<String, String> args) {
        Iterator it = args.entrySet().iterator();
        String parsedPath = path;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String identifier = pairs.getKey().toString();
            parsedPath = parsedPath.replace("{" + identifier + "}", pairs.getValue().toString());
        }
        return request(parsedPath);
    }

    public void interrupt(String msg) throws EventInterruptionException {
        throw new EventInterruptionException(msg);
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

    private void init(String path, Media media, String rest, Model form, Model domain) {
        this.path = path;
        this.media = media;
        this.rest = rest;
        this.form = form;
        this.domain = domain;
    }

    private synchronized Object request(String parsedPath) throws UniformInterfaceException {
        Client client = Client.create();
        WebResource resource = client.resource(parsedPath);
        Gson gson1 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Gson gson2 = new Gson();
        if (form != null) {
            if (rest.equals("post")) {
                ClientResponse response = resource.type(media.value()).post(ClientResponse.class, gson1.toJson(model));
                ApplicationControllerFactory.deserialize(model.getClass(), model, gson2.fromJson(response.getEntity(String.class), model.getClass()));
                return response;
            } else {
                ClientResponse response = resource.accept(media.value()).get(ClientResponse.class);
                ApplicationControllerFactory.deserialize(model.getClass(), model, gson2.fromJson(response.getEntity(String.class), model.getClass()));
                return response;
            }
        } else {
            if (rest.equals("post")) {
                ClientResponse response = resource.type(media.value()).post(ClientResponse.class, gson1.toJson(model));
                String jsonresponse = response.getEntity(String.class);
                return gson2.fromJson(jsonresponse, ServerResponse.class);
            } else {
                ClientResponse response = resource.accept(media.value()).get(ClientResponse.class);
                return gson2.fromJson(response.getEntity(String.class), ServerResponse.class);
            }
        }
    }

    public abstract void run(ConcurrentHashMap<String, Invoker> methods);
}
