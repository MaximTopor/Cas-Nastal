package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementSetter;
import sk.upjs.paz.app.SceneManager;
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
    public String getRoleName(long userId) {

        String sql = """
        SELECT r.name_of_role
        FROM cn.users u
        JOIN cn.roles r ON u.role_id = r.id_roles
        WHERE u.id_user = ?
    """;

        return jdbc.queryForObject(
                sql,
                String.class,
                userId
        );
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
        String sql = """
        SELECT *
        FROM cn.users
        ORDER BY surname, name
    """;

        return jdbc.query(sql, userRowMapper);
    }

    @Override
    public List<User> getByDistrict(long districtId) {
        return List.of();
    }
}
