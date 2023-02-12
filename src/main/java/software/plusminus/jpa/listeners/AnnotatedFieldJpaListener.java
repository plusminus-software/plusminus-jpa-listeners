package software.plusminus.jpa.listeners;

import org.springframework.lang.Nullable;
import software.plusminus.jpa.listeners.model.JpaListenerContext;
import software.plusminus.jpa.listeners.utils.JpaListenerUtils;
import software.plusminus.util.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

public abstract class AnnotatedFieldJpaListener extends JpaListener {

    protected void process(JpaListenerContext context, Class<? extends Annotation> type) {
        Object entity = context.getEntity();
        Optional<Field> field = FieldUtils.findFirstWithAnnotation(entity.getClass(), type);
        if (!field.isPresent()) {
            return;
        }
        Object currentValue = FieldUtils.read(entity, field.get());
        newValue(context, field.get(), currentValue)
                .ifPresent(value -> JpaListenerUtils.writeField(value, entity, field.get(), context.getPropertyNames(),
                        context.getCurrentState()));
    }

    protected abstract Optional<Object> newValue(JpaListenerContext context, Field field,
                                                 @Nullable Object currentValue);

}
