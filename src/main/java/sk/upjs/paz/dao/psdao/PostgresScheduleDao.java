package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.ScheduleDao;
import sk.upjs.paz.model.Schedule;

import java.util.List;

public class PostgresScheduleDao implements ScheduleDao {
    private final JdbcOperations jdbc;

    public PostgresScheduleDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Schedule> getAll() {
        return List.of();
    }

    @Override
    public Schedule getById(long id) {
        return null;
    }

    @Override
    public List<Schedule> getByUser(long userId) {
        return List.of();
    }

    @Override
    public List<Schedule> getByTerm(long termId) {
        return List.of();
    }

    @Override
    public void update(Schedule schedule) {

    }

    @Override
    public void insert(long userId, long termId) {
        String sql = """
            INSERT INTO cn.schedule (status_of_application, id_user, id_terms)
            VALUES ('pending', ?, ?)
        """;
        jdbc.update(sql, userId, termId);
    }

    @Override
    public void delete(long userId, long termId) {
        String sql = """
            DELETE FROM cn.schedule
            WHERE id_user = ? AND id_terms = ?
        """;
        jdbc.update(sql, userId, termId);
    }
}
