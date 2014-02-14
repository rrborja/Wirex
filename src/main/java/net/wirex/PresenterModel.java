package net.wirex;

import java.util.HashMap;
import java.util.Map;
import net.wirex.interfaces.Model;

/**
 *
 * @author Ritchie Borja
 */
public class PresenterModel extends Model {

    private final Map<String, Model> form;

    public PresenterModel() {
        form = new HashMap<>(5);
    }

    public PresenterModel(Map form) {
        if (form == null) {
            this.form = new HashMap();
        } else {
            this.form = form;
        }
    }

    public boolean isEmpty() {
        return form.isEmpty();
    }

    public void put(String name, Model src) {
        form.put(name, src);
    }

    public Map getForm() {
        return form;
    }

    @Override
    public String toString() {
        return form.toString();
    }

}
