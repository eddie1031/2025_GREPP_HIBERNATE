package eg1;

import io.eddie.domain.eg1.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.*;
import util.TestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static util.TestUtils.*;

@Slf4j
public class EntityManagerTests {

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
    @DisplayName("")
    void test1() throws Exception {

        Map<String, Object> properties = entityManagerFactory.getProperties();

        String url = properties.get("jakarta.persistence.jdbc.url").toString();
        String driver = properties.get("jakarta.persistence.jdbc.driver").toString();

        log.info("url = {}", url);
        log.info("driver = {}", driver);

        assertThat(url).isEqualTo("jdbc:mysql://localhost:3306/grepp_hibernate_test");
        assertThat(driver).isEqualTo("com.mysql.cj.jdbc.Driver");

    }

    @Test
    @DisplayName("Save test")
    void save_test() throws Exception {

//        EntityTransaction transaction = entityManager.getTransaction();
//        transaction.begin();
//        try {
//
//            // Transient
//            Member member = genMember(genMemberName());
//
//            // Managed
//            entityManager.persist(member);
//
//        } catch ( Exception e ) {
//            transaction.rollback();
//        } finally {
//            transaction.commit();
//        }

        executeCommit(entityManager, () -> {
            Member member = genMember(genMemberName());
            entityManager.persist(member);
        });

    }

    private Member genMember(String memberName) {
        return Member.builder()
                .id(memberName)
                .name(memberName)
                .build();
    }

    private static String genMemberName() {
        return "member" + genNumStr();
    }

    @Test
    @DisplayName("Select Test")
    void select_test() throws Exception {

        Member member = genMember(genMemberName());

        executeCommit(entityManager, () -> {

            entityManager.persist(member);
            Member findMember = entityManager.find(Member.class, member.getId());

            assertThat(findMember).isEqualTo(member);

            log.info("member = {}", member);
            log.info("findMember = {}", findMember);

        });

        executeCommit(entityManager, () -> {

            Member findMember = entityManager.find(Member.class, member.getId());
            assertThat(findMember.getId()).isEqualTo(member.getId());

            findMember.setName("ADMIN");
        });
//
        executeCommit(entityManager, () -> {
            Member findMember = entityManager.find(Member.class, member.getId());
            assertThat(findMember.getName()).isEqualTo("ADMIN");

            entityManager.detach(findMember);
            findMember.setName("MEMBER");
        });

        executeCommit(entityManager, () -> {
            Member findMember = entityManager.merge(member);
            assertThat(findMember.getName()).isNotEqualTo("ADMIN");
            assertThat(findMember.getName()).isEqualTo("MEMBER");
        });

    }

    @Test
    @DisplayName("Write-behind Test")
    void write_behind_test() throws Exception {

        executeCommit(entityManager, () -> {

            Member member1 = genMember(genMemberName());
            Member member2 = genMember(genMemberName());

            entityManager.persist(member1);
            entityManager.persist(member2);

            log.info("아직 쿼리가 실행되지 않았습니다!");

        });

    }


}
