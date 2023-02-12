package software.plusminus.jpa.listeners;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.jpa.listeners.exception.JpaListenerException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@SuppressFBWarnings("SE_NO_SERIALVERSIONID")
public abstract class JpaListener {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @SuppressWarnings("PMD.CloseResource")
    @PostConstruct
    private void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        boolean implementsAtLeastOneEventListener = false;
        for (EventType<Object> eventType : EventType.values()) {
            if (eventType.baseListenerInterface().isAssignableFrom(this.getClass())) {
                implementsAtLeastOneEventListener = true;
                registry.getEventListenerGroup(eventType).appendListener(this);
            }
        }
        if (!implementsAtLeastOneEventListener) {
            throw new JpaListenerException("Class " + this.getClass().getName() + " that extends JpaListener"
                    + "must implement at least one EventListener");
        }
    }

}
