package net.wirex;

/**
 *
 * @author Ritchie Borja
 */
public class ResponseStructure {

    private String feature;
    private Integer type;
    private Object body;

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

}
