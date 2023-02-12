package software.plusminus.jpa.listeners;

import lombok.Data;
import software.plusminus.jpa.listeners.annotation.ModificationTime;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class TestEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ModificationTime
    private ZonedDateTime modifiedTime;

    private String myField;

}