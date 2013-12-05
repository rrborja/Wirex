package net.wirex;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import net.wirex.interfaces.Model;

/**
 *
 * @author Ritchie Borja
 */
public class PresenterModel extends Model {
    
    private Map<String, Model> form;
    
    public PresenterModel() {
        form = new HashMap<>();
    }
    
    public PresenterModel(ImmutableMap form) {
        this.form = form;
    }
    
    public boolean isEmpty() {
        return form.isEmpty();
    }
    
    public void put(String name, Model src) {
        form.put(name, src);
    }
    
    public ImmutableMap getForm() {
        return ImmutableMap.copyOf(form);
    }
    
}
