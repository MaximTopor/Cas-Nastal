package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.RoleDao;
import sk.upjs.paz.model.Role;

import java.util.List;

public class PostgresRoleDao implements RoleDao {

    private final JdbcOperations jdbc;

    public PostgresRoleDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Role> getAll() {
        return List.of();
    }

    @Override
    public Role getById(int id) {
        return null;
    }

    @Override
    public void insert(Role role) {

    }

    @Override
    public void update(Role role) {

    }

    @Override
    public void delete(int id) {

    }
}
