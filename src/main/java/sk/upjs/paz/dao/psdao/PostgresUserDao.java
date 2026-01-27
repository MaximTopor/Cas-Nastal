package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.UserDao;
import sk.upjs.paz.dao.psdao.mapper.UserRowMapper;
import sk.upjs.paz.model.User;

import java.util.List;

public class PostgresUserDao implements UserDao {
    private final JdbcOperations jdbc;
    private final UserRowMapper userRowMapper = new UserRowMapper();

    public PostgresUserDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void insert(User user) {

        String sql = """
    INSERT INTO cn.users
    (name, surname, email, phone_number,
     password_hash, role_id, rodne_cislo,
     date_of_birth, adresa, district_id,
     created_at, update_at)
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
""";
        jdbc.update(
                sql,
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getPasswordHash(),
                user.getRoleId(),
                user.getPersonalNumber(),
                user.getDateOfBirth(),
                user.getAddress(),
                user.getDistrictId(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
    @Override
    public void update(User user) {
        String sql = """
        UPDATE cn.users
        SET name = ?,
            surname = ?,
            email = ?,
            phone_number = ?,
            adresa = ?,
             role_id = ?,
            district_id = ?,
            update_at = NOW()
        WHERE id_user = ?
    """;

        jdbc.update(
                sql,
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getRoleId(),
                user.getDistrictId(),
                user.getIdUser()
        );
    }
    @Override
    public boolean existsByPersonalNumber(String personalNumber) {

        String sql = """
        SELECT COUNT(*)
        FROM cn.users
        WHERE rodne_cislo = ?
    """;

        Integer count = jdbc.queryForObject(sql, Integer.class, personalNumber);
        return count != null && count > 0;
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
    public boolean hasRole(long userId, String role) {

        String sql = """
        SELECT COUNT(*)
        FROM cn.users u
        JOIN cn.roles r ON r.id_roles = u.role_id
        WHERE u.id_user = ?
          AND r.name_of_role = ?
    """;

        Integer count = jdbc.queryForObject(
                sql,
                Integer.class,
                userId,
                role
        );

        return count != null && count > 0;
    }
    @Override
    public boolean existsByEmail(String email) {

        String sql = """
        SELECT COUNT(*)
        FROM cn.users
        WHERE email = ?
    """;

        Integer count = jdbc.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
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
            FROM cn.users
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
        WHERE role_id = 3
        ORDER BY surname, name
    """;

        return jdbc.query(sql, userRowMapper);
    }
    @Override
    public List<User> getByDistrict(long districtId) {
        String sql = """
        SELECT *
        FROM cn.users
        WHERE district_id = ?
          AND role_id = 3
        ORDER BY surname, name
    """;

        return jdbc.query(sql, userRowMapper, districtId);
    }
    @Override
    public User getByPersonalNumber(String personalNumber) {
        String sql = """
        SELECT *
        FROM cn.users
        WHERE rodne_cislo = ?
        and role_id != 1
    """;

        return jdbc.query(sql, userRowMapper, personalNumber)
                .stream()
                .findFirst()
                .orElse(null);
    }
    @Override
    public User getByPhone(String phone) {
        String sql = """
        SELECT *
        FROM cn.users
        WHERE phone_number = ?
    """;

        return jdbc.query(sql, userRowMapper, phone)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
