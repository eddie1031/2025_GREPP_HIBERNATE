package eg3;

import io.eddie.domain.eg3._1.Player;
import io.eddie.domain.eg3._1.Team;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.*;
import util.TestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static util.TestUtils.*;

@Slf4j
public class RelationTests {

    static EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

    @BeforeAll
    static void init() {
        entityManagerFactory =
                Persistence.createEntityManagerFactory("grepp-hibernate-exp1");
    }

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    void close() {
        entityManager.close();
    }

    @AfterAll
    static void tearDown() {
        entityManagerFactory.close();
    }

    @Test
    @DisplayName("TABLE STRATEGY TEST")
    void table_stg_test() throws Exception {

        List<Player> bus = new ArrayList<>();

        executeCommit(entityManager, () -> {

            Player player = entityManager.find(Player.class, 1);
//            Team team = player.getTeam();

//            log.info("team.getName() = {}", team.getName());

            bus.add(player);

        });

        Player findPlayer = bus.get(0);

        log.info("findPlayer.getName() = {}", findPlayer.getName());
//        log.info("findPlayer.getTeam().getName() = {}", findPlayer.getTeam().getName());


    }

    @Test
    @DisplayName("proxy obj")
    void proxy_obj_test() throws Exception {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Player player = null;

        try {

            player = entityManager.find(Player.class, 1);
            log.info("player.getName() = {}", player.getName());

            entityManager.detach(player);

            transaction.commit();

        } catch ( Exception e ) {
            transaction.rollback();
        }

        entityManager.close();

        Team team = player.getTeam();
        log.info("team = {}", team);
        assertThatThrownBy(
                () -> {
                    team.getName();
                }
        ).isInstanceOf(LazyInitializationException.class);

    }

    @Test
    @DisplayName("getReference()")
    void get_reference_exception_test() throws Exception {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Player player = null;

        try {
            player = entityManager.getReference(Player.class, 1);
        } catch ( Exception e ) {
            transaction.rollback();
        }

        transaction.commit();
//        entityManager.close();

        log.info("player.getName() = {}", player.getName());

    }

    @Test
    @DisplayName("proxy check")
    void proxy_check() throws Exception {

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Player player = null;

        try {
            player = entityManager.getReference(Player.class, 1);
        } catch ( Exception e ) {
            transaction.rollback();
        }

        transaction.commit();
        entityManager.close();

        boolean result = entityManagerFactory.getPersistenceUnitUtil().isLoaded(player);
        log.info("result = {}", result);

    }



}
