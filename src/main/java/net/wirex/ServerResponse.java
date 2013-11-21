package net.wirex;

import java.util.Objects;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Ritchie Borja
 * @param <T>
 */
public final class ServerResponse<T> {

    private final HttpStatus status;
    private final T message;

    public ServerResponse(HttpStatus status, T message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }


    public T getMessage() {
        return message;
    }
    
    public boolean isSerializable() {
        return status == HttpStatus.OK;
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
