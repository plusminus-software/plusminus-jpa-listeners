package software.plusminus.jpa.listeners;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.plusminus.test.IntegrationTest;

import java.time.ZonedDateTime;
import java.util.Optional;

import static software.plusminus.check.Checks.check;

public class ModificationTimeListenerIntegrationTest extends IntegrationTest {

    public static final String SOME_STRING = "Some string";

    @Autowired
    private TransactionalService transactionalService;
    @Autowired
    private TestRepository repository;

    @After
    public void after() {
        repository.deleteAll();
    }

    @Test
    public void modifiedTimeIsWrittenOnCreateIfFieldIsNull() {
        TestEntity entity = getEntity();
        TestEntity saved = repository.save(entity);
        checkEntityHasRecentModifiedTime(saved, SOME_STRING);
    }

    @Test
    public void modifiedTimeIsNotWrittenOnCreateIfFieldIsNotNull() {
        ZonedDateTime modifiedTime = ZonedDateTime.now().minusMonths(1);
        TestEntity entity = getEntity();
        entity.setModifiedTime(modifiedTime);

        TestEntity saved = repository.save(entity);

        checkEntityHasSpecificModifiedTime(saved, SOME_STRING, modifiedTime);
    }

    @Test
    public void modifiedTimeIsWrittenOnUpdateIfFieldIsNotUpdated() {
        ZonedDateTime modifiedTime = ZonedDateTime.now().minusMonths(1);
        TestEntity entity = getEntity();
        entity.setModifiedTime(modifiedTime);
        repository.save(entity);

        TestEntity update = getEntity();
        update.setId(entity.getId());
        update.setMyField("Updated");
        update.setModifiedTime(modifiedTime);

        TestEntity saved = repository.save(update);

        checkEntityHasRecentModifiedTime(saved, update.getMyField());
    }

    @Test
    public void modifiedTimeIsWrittenOnUpdateIfFieldIsNull() {
        ZonedDateTime modifiedTime = ZonedDateTime.now().minusMonths(1);
        TestEntity entity = getEntity();
        entity.setModifiedTime(modifiedTime);
        repository.save(entity);

        TestEntity update = getEntity();
        update.setId(entity.getId());
        update.setMyField("Updated");
        update.setModifiedTime(null);

        TestEntity saved = repository.save(update);

        checkEntityHasRecentModifiedTime(saved, update.getMyField());
    }

    @Test
    public void modifiedTimeIsNotWrittenOnUpdateIfFieldIsUpdated() {
        ZonedDateTime modifiedTime = ZonedDateTime.now().minusMonths(1);
        TestEntity entity = getEntity();
        entity.setModifiedTime(modifiedTime);
        repository.save(entity);

        ZonedDateTime updatedTime = ZonedDateTime.now().minusDays(1);
        TestEntity update = getEntity();
        update.setId(entity.getId());
        update.setMyField("Updated");
        update.setModifiedTime(updatedTime);

        TestEntity saved = repository.save(update);

        checkEntityHasSpecificModifiedTime(saved, update.getMyField(), updatedTime);
    }

    private TestEntity getEntity() {
        TestEntity entity = new TestEntity();
        entity.setMyField(SOME_STRING);
        return entity;
    }

    private void checkEntityHasRecentModifiedTime(TestEntity entity, String myField) {
        check(entity.getModifiedTime()).recent();
        check(entity.getMyField()).is(myField);
        Optional<TestEntity> fromDb = repository.findById(entity.getId());
        check(fromDb).isPresent();
        check(fromDb.get().getModifiedTime()).recent();
        check(fromDb.get().getMyField()).is(myField);
    }

    private void checkEntityHasSpecificModifiedTime(TestEntity entity, String myField, ZonedDateTime modifiedTime) {
        check(entity.getModifiedTime()).is(modifiedTime);
        check(entity.getMyField()).is(myField);
        Optional<TestEntity> fromDb = repository.findById(entity.getId());
        check(fromDb).isPresent();
        check(fromDb.get().getModifiedTime()).is(modifiedTime);
        check(fromDb.get().getMyField()).is(myField);
    }

}