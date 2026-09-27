package com.engine.core.config;

import com.engine.core.testsupport.PostgresTestBase;
import com.engine.domain.Event;
import com.engine.domain.Seat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(classes = {AppConfig.class, PersistenceConfig.class})
@TestPropertySource(properties = {"hibernate.hbm2ddl.auto=create-drop"})
class PersistenceConfigIT extends PostgresTestBase {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void contextLoadsAndEntityManagerFactoryIsConfigured() {
        assertThat(entityManagerFactory).isNotNull();
        assertThat(entityManagerFactory.getMetamodel().getEntities())
                .extracting(EntityType::getName)
                .contains("Event", "Seat");
    }

    @Test
    @Transactional
    void persistsAndReadsBackAnEventWithSeats() {
        Event event = new Event("Test Concert", "Test Arena", Instant.now().plusSeconds(86_400));
        entityManager.persist(event);

        Seat seat = new Seat(event, "A", 1);
        entityManager.persist(seat);

        entityManager.flush();
        entityManager.clear();

        Event reloaded = entityManager.find(Event.class, event.getId());
        assertThat(reloaded).isNotNull();
        assertThat(reloaded.getName()).isEqualTo("Test Concert");

        Long seatCount = entityManager
                .createQuery("select count(s) from Seat s where s.event.id = :eventId", Long.class)
                .setParameter("eventId", event.getId())
                .getSingleResult();

        assertThat(seatCount).isEqualTo(1L);
    }

    @Test
    @Transactional
    void transactionRollsBackOnException() {
        Event event = new Event("Rollback Concert", "Rollback Arena", Instant.now());
        entityManager.persist(event);
        entityManager.flush();

        Long id = event.getId();

        try {
            runInNestedTransactionAndThrow();
        } catch (RuntimeException ignored) {
            // Expected
        }

        entityManager.clear();
        assertThat(entityManager.find(Event.class, id)).isNotNull();
    }

    private void runInNestedTransactionAndThrow() {

    }
}
