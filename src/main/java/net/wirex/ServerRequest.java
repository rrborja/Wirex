package net.wirex;

import com.google.gson.GsonBuilder;
import java.awt.Window;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.slf4j.LoggerFactory;
import net.wirex.enums.Media;
import net.wirex.enums.REST;
import net.wirex.interfaces.Model;
import net.wirex.structures.XAttachment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 *
 * @author Ritchie Borja
 * @param <T> Your model
 */
public final class ServerRequest<T extends Model> {

    private final REST rest;
    private final String path;
    private final Media media;
    private final Map<String, String> variables;
    private final Model body;
    private final Class<? extends Model> model;
    private final Window parent;

    public ServerRequest(String rest, String path, Media media, Map<String, String> variables, Model body, JPanel panel) {
        this.parent = panel != null ? SwingUtilities.getWindowAncestor(panel) : null;
        this.rest = rest != null ? REST.valueOf(rest) : null;
        this.path = path;
        this.media = media;
        this.variables = variables != null ? variables : new HashMap<>(5);
        this.model = body != null ? body.getClass() : Model.class;
        this.body = body != null ? body : new Model() {
        };
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
    
    public Window getParent() {
        return parent;
    }

    public String getBody() {
        switch (media) {
            case URLENCODED:
                return AppEngine.encodeToUrl(body);
            default:
                if (model == PresenterModel.class) {
                    PresenterModel presenterBody = (PresenterModel)body;
                    return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, new DateJsonDeserializer()).create().toJson(presenterBody.getForm());
                } else {
                    return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, new DateJsonDeserializer()).create().toJson(body);
                }
        }
    }

    public String getRequestBody() {
        try {
            return AppEngine.snip(body);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            LoggerFactory.getLogger(ServerRequest.class.getName()).error("Can't snip field(s) in " + model, ex);
            return "";
        }
    }
    
    public Map getAttachments() {
        if (!(body instanceof XAttachment)) {
            return new HashMap(0);
        }
        XAttachment attachmentModel = (XAttachment) body;
        String[] urls = attachmentModel.attachURLs();
        String[] contentTypes = attachmentModel.getContentTypes();
        int filesLength = urls.length;
        Map<String, String> map = new HashMap<>(filesLength);
        for (int i = 0; i < filesLength; i++) {
            String url = urls[i];
            String contentType = contentTypes[i];
            map.put(url, contentType);
        }
        return map;
    }

    @Override
    public String toString() {
        return "ServerRequest{" + "rest=" + rest + ", path=" + path + ", media=" + media + ", variables=" + variables + ", body=" + body + ", model=" + model + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.rest);
        hash = 97 * hash + Objects.hashCode(this.path);
        hash = 97 * hash + Objects.hashCode(this.media);
        hash = 97 * hash + Objects.hashCode(this.variables);
        hash = 97 * hash + Objects.hashCode(this.body);
        hash = 97 * hash + Objects.hashCode(this.model);
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
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        if (!Objects.equals(this.model, other.model)) {
            return false;
        }
        return true;
    }

}
