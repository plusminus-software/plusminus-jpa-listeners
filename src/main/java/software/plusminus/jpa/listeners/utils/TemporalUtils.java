package software.plusminus.jpa.listeners.utils;

import lombok.experimental.UtilityClass;
import software.plusminus.jpa.listeners.exception.JpaListenerException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

@UtilityClass
public class TemporalUtils {

    private Map<Class<?>, Supplier<?>> currentTimeSuppliers;

    static {
        currentTimeSuppliers = new IdentityHashMap<>();
        currentTimeSuppliers.put(Instant.class, Instant::now);
        currentTimeSuppliers.put(ZonedDateTime.class, ZonedDateTime::now);
        currentTimeSuppliers.put(OffsetDateTime.class, OffsetDateTime::now);
        currentTimeSuppliers.put(OffsetTime.class, OffsetTime::now);
        currentTimeSuppliers.put(LocalDateTime.class, LocalDateTime::now);
        currentTimeSuppliers.put(LocalDate.class, LocalDate::now);
        currentTimeSuppliers.put(LocalTime.class, LocalTime::now);
        currentTimeSuppliers.put(HijrahDate.class, HijrahDate::now);
        currentTimeSuppliers.put(JapaneseDate.class, JapaneseDate::now);
        currentTimeSuppliers.put(MinguoDate.class, MinguoDate::now);
        currentTimeSuppliers.put(ThaiBuddhistDate.class, ThaiBuddhistDate::now);
        currentTimeSuppliers.put(Year.class, Year::now);
        currentTimeSuppliers.put(YearMonth.class, YearMonth::now);

        currentTimeSuppliers.put(Date.class, Date::new);
        currentTimeSuppliers.put(Long.class, System::currentTimeMillis);
        currentTimeSuppliers.put(String.class, () -> ZonedDateTime.now().toString());
    }

    public boolean isTemporalType(Class<?> type) {
        return currentTimeSuppliers.containsKey(type);
    }

    public <T> T now(Class<T> type) {
        Supplier<?> currentTimeSupplier = currentTimeSuppliers.get(type);
        if (currentTimeSupplier == null) {
            throw new JpaListenerException("Unknown type to represent current time: " + type.getName());
        }
        return type.cast(currentTimeSupplier.get());
    }

}
