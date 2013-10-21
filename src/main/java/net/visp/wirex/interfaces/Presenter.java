/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.visp.wirex.interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JPanel;
import net.visp.wirex.ApplicationControllerFactory;
import net.visp.wirex.enums.Media;
import test.PhoneModel;

/**
 *
 * @author RBORJA
 */
public class Presenter {

    private Model model;
    private JPanel view;
    private String path;
    private Media media;
    private String rest;

    public Presenter(Model model, JPanel panel) {
        this.model = model;
        this.view = panel;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public JPanel getPanel() {
        return view;
    }

    public void setPanel(JPanel panel) {
        this.view = panel;
    }

    public ClientResponse call() {
        return request(path);
    }

    public ClientResponse call(Map<String, String> args) {
        Iterator it = args.entrySet().iterator();
        String parsedPath = path;
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String identifier = pairs.getKey().toString();
            parsedPath = parsedPath.replace("{" + identifier + "}", pairs.getValue().toString());
        }
        return request(parsedPath);
    }

    private void init(String path, Media media, String rest) {
        this.path = path;
        this.media = media;
        this.rest = rest;
    }

    private ClientResponse request(String parsedPath) throws UniformInterfaceException {
        Client client = Client.create();
        WebResource resource = client.resource(parsedPath);
        Gson gson1 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Gson gson2 = new Gson();
        if (rest.equals("post")) {
            ClientResponse response = resource.type(media.value()).post(ClientResponse.class, gson1.toJson(model));
            ApplicationControllerFactory.deserialize(model.getClass(), model, gson2.fromJson(response.getEntity(String.class), model.getClass()));
            return response;
        } else {
            ClientResponse response = resource.accept(media.value()).get(ClientResponse.class);
            ApplicationControllerFactory.deserialize(model.getClass(), model, gson2.fromJson(response.getEntity(String.class), model.getClass()));
            return response;
        }
    }
}
