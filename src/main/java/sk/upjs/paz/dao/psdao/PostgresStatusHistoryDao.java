package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.StatusHistoryDao;
import sk.upjs.paz.model.StatusHistory;

import java.util.List;

public class PostgresStatusHistoryDao implements StatusHistoryDao {

    private final JdbcOperations jdbc;

    public PostgresStatusHistoryDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<StatusHistory> getAll() {
        return List.of();
    }

    @Override
    public StatusHistory getById(long id) {
        return null;
    }

    @Override
    public List<StatusHistory> getByUser(long userId) {
        return List.of();
    }

    @Override
    public List<StatusHistory> getByStatus(long statusId) {
        return List.of();
    }

    @Override
    public void insert(StatusHistory history) {

    }

    @Override
    public void update(StatusHistory history) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public StatusHistory getCurrentStatus(long userId) {
        return null;
    }
}
