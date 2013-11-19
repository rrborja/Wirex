/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import com.google.gson.GsonBuilder;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.wirex.enums.Media;
import net.wirex.enums.REST;
import net.wirex.interfaces.Model;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author Ritchie Borja
 * @param <T> Your model
 */
public class ServerRequest<T extends Model> {

    private final REST rest;
    private final String path;
    private final Media media;
    private final Map<String, String> variables;
    private final MultiValueMap headerMap;
    private final Model body;
    private final Class<? extends Model> model;

    public ServerRequest(String rest, String path, Media media, Map<String, String> variables, Model body) {
        this.rest = REST.valueOf(rest);
        this.path = path;
        this.media = media;
        this.variables = variables;
        this.headerMap = null;
        this.model = body.getClass();
        this.body = body;
    }

    public ServerRequest(String rest, String path, Media media, MultiValueMap headerMap, Map<String, String> variables) {
        this.rest = REST.valueOf(rest);
        this.path = path;
        this.media = media;
        this.headerMap = headerMap;
        this.variables = variables;
        this.body = null;
        this.model = null;
    }

    public Class getModel() {
        return model;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public HttpMethod getRest() {
        return HttpMethod.valueOf(rest.toString());
    }

    public MediaType getMedia() {
        return media.value();
    }

    public MultiValueMap getHeaderMap() {
        return headerMap;
    }

    public String getBody() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(body);
    }

    public String getRequestBody() {
        try {
            return AppEngine.snip(body);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            LoggerFactory.getLogger(ServerRequest.class.getName()).error("Can't snip field(s) in " + model, ex);
            return "";
        }
    }

    @Override
    public String toString() {
        return "ServerRequest{" + "rest=" + rest + ", path=" + path + ", media=" + media + ", variables=" + variables + ", headerMap=" + headerMap + ", body=" + body + ", model=" + model + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.rest);
        hash = 53 * hash + Objects.hashCode(this.path);
        hash = 53 * hash + Objects.hashCode(this.media);
        hash = 53 * hash + Objects.hashCode(this.variables);
        hash = 53 * hash + Objects.hashCode(this.headerMap);
        hash = 53 * hash + Objects.hashCode(this.body);
        hash = 53 * hash + Objects.hashCode(this.model);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServerRequest<?> other = (ServerRequest<?>) obj;
        if (this.rest != other.rest) {
            return false;
        }
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        if (this.media != other.media) {
            return false;
        }
        if (!Objects.equals(this.variables, other.variables)) {
            return false;
        }
        if (!Objects.equals(this.headerMap, other.headerMap)) {
            return false;
        }
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        if (!Objects.equals(this.model, other.model)) {
            return false;
        }
        return true;
    }

}
