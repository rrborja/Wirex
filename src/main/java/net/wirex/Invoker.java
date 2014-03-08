package net.wirex;

import java.awt.event.ActionListener;
import java.util.Objects;

/**
 *
 * @author Ritchie Borja
 */
public final class Invoker<T> {

    private final ActionTrigger invokeCode;

    public Invoker(ActionTrigger invokeCode) {
        this.invokeCode = invokeCode;
    }

    public void run() {
        invokeCode.run();
    }
    
    public void run(T... args) {
        invokeCode.run(args);
    }

    @Override
    public String toString() {
        return "Invoker{" + "invokeCode=" + invokeCode + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.invokeCode);
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
        final Invoker other = (Invoker) obj;
        if (!Objects.equals(this.invokeCode, other.invokeCode)) {
            return false;
        }
        return true;
    }

    
}
