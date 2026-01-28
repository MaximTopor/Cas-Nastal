package sk.upjs.paz.dao.psdao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.upjs.paz.model.Message;
import sk.upjs.paz.testcontainer.TestContainer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresMessageDaoTest extends TestContainer {

    private PostgresMessageDao messageDao;

    private static final DateTimeFormatter DB_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        messageDao = new PostgresMessageDao(jdbcTemplate);

        // =========================
        // SCHEMA
        // =========================
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS cn");
        jdbcTemplate.execute("SET search_path TO cn");

        // =========================
        // TABLES (minimal FK-safe)
        // =========================

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS cn.users (
                id_user BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS cn.message (
                id_message BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                id_sender BIGINT NOT NULL,
                id_recipient BIGINT NOT NULL,
                subject VARCHAR(45) NOT NULL,
                text TEXT NOT NULL,
                message TEXT NOT NULL,
                date_sent TIMESTAMP NOT NULL,
                last_action TEXT NOT NULL,
                CONSTRAINT fk_msg_sender
                    FOREIGN KEY (id_sender) REFERENCES cn.users(id_user),
                CONSTRAINT fk_msg_recipient
                    FOREIGN KEY (id_recipient) REFERENCES cn.users(id_user)
            )
        """);

        // =========================
        // CLEAN
        // =========================
        jdbcTemplate.execute("TRUNCATE TABLE cn.message RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.users RESTART IDENTITY CASCADE");

        // =========================
        // USERS
        // =========================
        jdbcTemplate.update("INSERT INTO cn.users DEFAULT VALUES"); // id 1
        jdbcTemplate.update("INSERT INTO cn.users DEFAULT VALUES"); // id 2
    }

    // =========================
    // HELPER
    // =========================
    private Message sampleMessage(LocalDateTime time) {
        return new Message(
                1L, // sender
                2L, // recipient
                "Test subject",
                "Preview text",
                time.format(DB_FMT),
                "Full message body",
                "CREATE"
        );
    }

    // =========================
    // TESTS
    // =========================

    @Test
    void insert_shouldStoreMessage() {
        messageDao.insert(sampleMessage(LocalDateTime.now()));

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM cn.message",
                Integer.class
        );

        assertEquals(1, count);
    }

    @Test
    void getAll_shouldReturnInsertedMessage() {
        String date = LocalDateTime.now().format(DB_FMT);

        messageDao.insert(new Message(
                1L,
                2L,
                "Subject",
                "Hello",
                date,
                "Body",
                "CREATE"
        ));

        List<Message> messages = messageDao.getAll();

        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0).getText());
        assertEquals(date, messages.get(0).getDateSent());
    }

    @Test
    void getAll_shouldBeOrderedByDateDesc() {
        messageDao.insert(sampleMessage(LocalDateTime.now().minusDays(1)));
        messageDao.insert(sampleMessage(LocalDateTime.now()));

        List<Message> messages = messageDao.getAll();

        assertEquals(2, messages.size());

        assertTrue(
                messages.get(0).getDateSent()
                        .compareTo(messages.get(1).getDateSent()) >= 0
        );
    }
}
