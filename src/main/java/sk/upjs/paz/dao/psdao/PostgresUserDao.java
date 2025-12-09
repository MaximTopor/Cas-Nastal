package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.UserDao;
import sk.upjs.paz.model.User;

import java.util.List;

public class PostgresUserDao implements UserDao {
    private final JdbcOperations jdbc;

    public PostgresUserDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public User findById(long id) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public void insert(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public List<User> getAll() {
        return List.of();
    }
}
