package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.StatusDao;
import sk.upjs.paz.model.Status;

import java.util.List;

public class PostgresStatusDao implements StatusDao {
    private final JdbcOperations jdbc;

    public PostgresStatusDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Status> getAll() {
        return List.of();
    }

    @Override
    public Status getById(long id) {
        return null;
    }

    @Override
    public void insert(Status status) {

    }

    @Override
    public void update(Status status) {

    }

    @Override
    public void delete(long id) {

    }
}
