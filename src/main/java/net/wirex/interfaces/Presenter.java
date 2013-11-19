/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex.interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import net.wirex.AppEngine;
import net.wirex.Invoker;
import net.wirex.ServerRequest;
import net.wirex.ServerResponse;
import net.wirex.enums.Media;
import net.wirex.exceptions.EventInterruptionException;

/**
 *
 * @author Ritchie Borja
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

    public ServerResponse call() {
        return request(path, new HashMap());
    }

    public ServerResponse call(Map<String, String> args) {
        return request(path, args);
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
        ServerRequest request = new ServerRequest(rest, path, media, variables, model);
        ServerResponse response = AppEngine.push(request);
        if (response.isSerializable()) {
            AppEngine.deserialize(model, (Model)response.getMessage());
        }
        return response;
    }

    public abstract void run(ConcurrentHashMap<String, Invoker> methods);
}
