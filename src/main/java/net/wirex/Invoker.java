/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import java.awt.event.ActionListener;
import java.util.Objects;

/**
 *
 * @author jvallar
 */
public class Invoker {

    private final ActionListener invokeCode;

    public Invoker(ActionListener invokeCode) {
        this.invokeCode = invokeCode;
    }

    public void run() {
        invokeCode.actionPerformed(null);
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
