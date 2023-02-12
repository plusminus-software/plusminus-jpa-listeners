package software.plusminus.jpa.listeners.utils;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;
import software.plusminus.jpa.listeners.exception.JpaListenerException;
import software.plusminus.util.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

@UtilityClass
public class JpaListenerUtils {

    public void writeField(Object value, Object entity, Field field,
                           String[] propertyNames, Object[] state) {

        FieldUtils.write(entity, value, field);
        JpaListenerUtils.updateState(state, propertyNames, field.getName(), value);
    }

    public void updateState(Object[] currentState, String[] propertyNames, String propertyToUpdate, Object value) {
        int index = getPropertyIndex(propertyNames, propertyToUpdate);
        currentState[index] = value;
    }

    public boolean checkStateIsChanged(@Nullable Object[] oldState, String[] propertyNames,
                                       String checkingProperty, Object value) {
        if (oldState == null) {
            return true;
        }
        int index = getPropertyIndex(propertyNames, checkingProperty);
        Object oldValue = oldState[index];
        return !Objects.equals(oldValue, value);
    }

    private int getPropertyIndex(String[] propertyNames, String propertyToFind) {
        int index = Arrays.asList(propertyNames).indexOf(propertyToFind);
        if (index < 0) {
            throw new JpaListenerException("Can't find '" + propertyToFind + "' property");
        }
        return index;
    }

}
