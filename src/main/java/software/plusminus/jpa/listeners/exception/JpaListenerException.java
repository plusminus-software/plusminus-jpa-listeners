package software.plusminus.jpa.listeners.exception;

public class JpaListenerException extends RuntimeException {

    public JpaListenerException(Throwable cause) {
        super(cause);
    }

    public JpaListenerException(String message) {
        super(message);
    }
}
