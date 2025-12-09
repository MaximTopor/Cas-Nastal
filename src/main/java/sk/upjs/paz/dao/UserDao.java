package sk.upjs.paz.dao;

import sk.upjs.paz.model.User;

import java.util.List;

public interface UserDao {
    User findById(long id);
    User findByEmail(String email);
    void insert(User user);
    void update(User user);
    List<User> getAll();
}
