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

package net.wirex;

import com.google.gson.GsonBuilder;
import java.util.Map;
import java.util.Objects;
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
