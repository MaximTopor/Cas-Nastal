package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import sk.upjs.paz.dao.UserDao;
import sk.upjs.paz.dao.psdao.mapper.UserRowMapper;
import sk.upjs.paz.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostgresUserDao implements UserDao {
    private final JdbcOperations jdbc;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    public PostgresUserDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insert(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public User getByEmail(String email) {
        String sql = """
            SELECT *
            FROM cn.users
            WHERE email = ?
        """;

        return jdbc.query(sql, userRowMapper, email)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public User getById(long id) {
        String sql = """
            SELECT *
            FROM users
            WHERE id_user = ?
        """;

        return jdbc.query(sql, userRowMapper, id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public List<User> getByDistrict(long districtId) {
        return List.of();
    }
}
