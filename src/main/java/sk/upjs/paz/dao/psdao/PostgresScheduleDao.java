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

    private org.springframework.jdbc.core.RowMapper<Schedule> scheduleRowMapper() {
        return (rs, rowNum) -> new Schedule(
                rs.getLong("id_schedule"),
                rs.getString("status_of_application"),
                rs.getLong("id_user"),
                rs.getLong("id_terms")
        );
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

        String sql = """
            SELECT *
            FROM cn.schedule
            WHERE id_user = ?
        """;

        return jdbc.query(sql, scheduleRowMapper(), userId);
    }

    @Override
    public List<Schedule> getByTerm(long termId) {

        String sql = """
        SELECT
            id_schedule,
            status_of_application,
            id_user,
            id_terms
        FROM cn.schedule
        WHERE id_terms = ?
    """;

        return jdbc.query(sql, scheduleRowMapper(), termId);
    }


    public void update(Schedule schedule) {

        String sql = """
        UPDATE schedule
        SET status_of_application = ?
        WHERE id_schedule = ?
    """;

        jdbc.update(
                sql,
                schedule.getStatusOfApplication(),
                schedule.getIdSchedule()
        );
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

    @Override
    public boolean exists(long userId, long termId) {

        String sql = """
        SELECT EXISTS (
            SELECT 1
            FROM cn.schedule
            WHERE id_user = ? AND id_terms = ?
        )
    """;

        return Boolean.TRUE.equals(
                jdbc.queryForObject(
                        sql,
                        Boolean.class,
                        userId,
                        termId
                )
        );
    }

    @Override
    public void cancelTerm(long termId) {

        String sql = """
        UPDATE cn.terms
        SET status = 'canceled'
        WHERE id_terms = ?
    """;

        jdbc.update(sql, termId);
    }


}
