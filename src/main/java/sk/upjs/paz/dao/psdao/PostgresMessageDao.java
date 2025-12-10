package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.MessageDao;
import sk.upjs.paz.model.Message;

import java.util.List;

public class PostgresMessageDao implements MessageDao {

    private final JdbcOperations jdbc;

    public PostgresMessageDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Message> getAll() {
        return List.of();
    }

    @Override
    public Message getById(long id) {
        return null;
    }

    @Override
    public List<Message> getBySender(long senderId) {
        return List.of();
    }

    @Override
    public List<Message> getByRecipient(long recipientId) {
        return List.of();
    }

    @Override
    public void insert(Message message) {

    }

    @Override
    public void update(Message message) {

    }

    @Override
    public void delete(long id) {

    }
}
