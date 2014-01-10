package net.wirex.structures;

/**
 *
 * @author Ritchie Borja
 */
public interface XComponent {

    /**
     * Sets the value for that custom component
     * 
     * @param value the value for that custom component
     */
    public void setValue(Object value);

    /**
     * Sets the custom component to be enabled or disabled
     * 
     * @param enabled True if enabled, otherwise, disabled
     */
    public void setEnabled(boolean enabled);

    /**
     * Checks if the custom component is enabled or not
     * 
     * @return True if enabled, otherwise, disabled
     */
    public boolean isEnabled();

    /**
     * Adds listener for custom component state changes
     * 
     * @param listener listens for custom component state changes
     */
    public void addValueListener(XValueListener listener);
}
