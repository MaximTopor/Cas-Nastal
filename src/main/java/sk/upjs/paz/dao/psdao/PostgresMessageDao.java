package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.MessageDao;
import sk.upjs.paz.model.Message;
import java.sql.Timestamp;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PostgresMessageDao implements MessageDao {

    private final JdbcOperations jdbc;

    public PostgresMessageDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Message> getAll() {
        return jdbc.query("""
        SELECT id_sender, text, date_sent
        FROM cn.message
        ORDER BY date_sent DESC
    """, (rs, rowNum) -> new Message(
                rs.getLong("id_sender"),
                rs.getLong("id_sender"),
                "ADMIN",
                rs.getString("text"),
                rs.getTimestamp("date_sent").toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                rs.getString("text"),
                "CREATE"
        ));
    }

    @Override
    public void insert(Message message) {

        jdbc.update("""
        INSERT INTO cn.message
        (id_sender, id_recipient, subject, text, message, date_sent, last_action)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """,
                message.getIdSender(),
                message.getIdRecipient(),
                message.getSubject(),
                message.getText(),
                message.getMessage(),
                Timestamp.valueOf(message.getDateSent()),
                message.getLastAction()
        );
    }
}
