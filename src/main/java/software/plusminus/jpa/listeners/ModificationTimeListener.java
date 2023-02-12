package software.plusminus.jpa.listeners;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import software.plusminus.jpa.listeners.annotation.ModificationTime;
import software.plusminus.jpa.listeners.exception.JpaListenerException;
import software.plusminus.jpa.listeners.model.JpaListenerContext;
import software.plusminus.jpa.listeners.utils.JpaListenerUtils;
import software.plusminus.jpa.listeners.utils.TemporalUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@SuppressFBWarnings("SE_NO_SERIALVERSIONID")
@Component
public class ModificationTimeListener extends AnnotatedFieldJpaListener implements PreInsertEventListener,
        PreUpdateEventListener {

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        JpaListenerContext context = JpaListenerContext.builder()
                .entity(event.getEntity())
                .currentState(event.getState())
                .oldState(null)
                .propertyNames(event.getPersister().getPropertyNames())
                .build();
        process(context, ModificationTime.class);
        return false;
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        JpaListenerContext context = JpaListenerContext.builder()
                .entity(event.getEntity())
                .currentState(event.getState())
                .oldState(event.getOldState())
                .propertyNames(event.getPersister().getPropertyNames())
                .build();
        process(context, ModificationTime.class);
        return false;
    }


    @Override
    protected Optional<Object> newValue(JpaListenerContext context, Field field, @Nullable Object currentValue) {
        if (!TemporalUtils.isTemporalType(field.getType())) {
            throw new JpaListenerException("Field " + field.getName() + " annotated with @ModificationTime"
                    + " annotation (in " + field.getType().getName() + " class) must have temporal type,"
                    + " but has " + field.getType().getName());
        }

        if (currentValue != null && JpaListenerUtils.checkStateIsChanged(
                context.getOldState(), context.getPropertyNames(), field.getName(), currentValue)) {
            return Optional.empty();
        }
        return Optional.of(TemporalUtils.now(field.getType()));
    }
}
