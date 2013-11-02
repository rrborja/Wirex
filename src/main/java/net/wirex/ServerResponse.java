/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import java.util.Objects;

/**
 *
 * @author RBORJA
 * @param <T>
 */
public class ServerResponse<T> {

    private final String status;
    private final T message;

    public ServerResponse(String status, T message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }


    public T getMessage() {
        return message;
    }
    
    public boolean isSerializable() {
        return status.equals("SUCCESS");
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.status);
        hash = 41 * hash + Objects.hashCode(this.message);
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
        final ServerResponse other = (ServerResponse) obj;
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ServerResponse{" + "status=" + status + ", data=" + message + '}';
    }

}
