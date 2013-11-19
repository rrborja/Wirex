/*
 * The MIT License
 *
 * Copyright 2013 Ritchie Borja.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.wirex.interfaces;

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
