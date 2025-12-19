package sk.upjs.paz.dao;

import sk.upjs.paz.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAll();
    User getById(long id);
    User getByEmail(String email);
    List<User> getByDistrict(long districtId);

    void insert(User user);
    void update(User user);
    void delete(long id);

    boolean existsByPersonalNumber(String personalNumber);

    String getRoleName(long userId);
    User getByPersonalNumber(String personalNumber);
    boolean hasRole(long userId, String role);
    boolean existsByEmail(String email);
}
