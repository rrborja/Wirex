package net.wirex.structures;

import java.util.Map;

/**
 *
 * @author Ritchie Borja
 */
public interface XLive {
    
    void onChanges(Map<String, String> map);
    
}
