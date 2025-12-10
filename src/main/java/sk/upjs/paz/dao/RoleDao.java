package sk.upjs.paz.dao;

import sk.upjs.paz.model.Role;

import java.util.List;

public interface RoleDao {
    List<Role> getAll();
    Role getById(int id);

    void insert(Role role);
    void update(Role role);
    void delete(int id);
}
