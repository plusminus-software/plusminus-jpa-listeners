package software.plusminus.jpa.listeners.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@Builder
public class JpaListenerContext {

    private Object entity;

    @Nullable
    private Object[] oldState;

    @Nullable
    private Object[] currentState;

    private String[] propertyNames;

}
