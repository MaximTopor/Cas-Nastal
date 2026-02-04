package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.StatusHistoryDao;
import sk.upjs.paz.model.Status;
import sk.upjs.paz.model.StatusHistory;

import java.util.List;

public class PostgresStatusHistoryDao implements StatusHistoryDao {

    private final JdbcOperations jdbc;

    public PostgresStatusHistoryDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Status> getAllStatuses() {

        return jdbc.query(
                """
                SELECT id_status,
                       name,
                       description
                FROM cn.status
                ORDER BY name
                """,
                (rs, rowNum) -> {
                    Status status = new Status();
                    status.setIdStatus(rs.getLong("id_status"));
                    status.setName(rs.getString("name"));
                    status.setDescription(rs.getString("description"));
                    return status;
                }
        );
    }

    @Override
    public void changeStatusAtomic(long userId, long statusId, long changedBy, String reason) {
        jdbc.update(
                """
                WITH deactivated AS (
                    UPDATE cn.status_history
                       SET is_current = false
                     WHERE user_id = ? AND is_current = true
                )
                INSERT INTO cn.status_history
                    (user_id, status_id, changed_by, reason, is_current)
                VALUES (?, ?, ?, ?, true)
                """,
                userId, userId, statusId, changedBy, reason
        );
    }


    @Override
    public void insert(long userId, long statusId, long changedBy, String reason) {
        jdbc.update(
                """
                INSERT INTO cn.status_history
                (user_id, status_id, changed_by, reason, is_current)
                VALUES (?, ?, ?, ?, true)
                """,
                userId, statusId, changedBy, reason
        );
    }

    @Override
    public void deactivateCurrent(long id) {
        jdbc.update(
                """
                UPDATE cn.status_history
                SET is_current = false
                WHERE user_id = ? AND is_current = true
                """,
                id
        );
    }

    @Override
    public long getCurrentStatusId(long userId) {
        return jdbc.queryForObject(
                """
                SELECT status_id
                FROM cn.status_history
                WHERE user_id = ? AND is_current = true
                """,
                Long.class,
                userId
        );
    }

    @Override
    public Status getCurrentStatus(long userId) {

        return jdbc.queryForObject(
                """
                SELECT s.id_status,
                       s.name,
                       s.description
                FROM cn.status_history sh
                JOIN cn.status s
                  ON s.id_status = sh.status_id
                WHERE sh.user_id = ?
                  AND sh.is_current = true
                """,
                (rs, rowNum) -> {
                    Status status = new Status();
                    status.setIdStatus(rs.getLong("id_status"));
                    status.setName(rs.getString("name"));
                    status.setDescription(rs.getString("description"));
                    return status;
                },
                userId
        );
    }
}
