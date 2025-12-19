package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.UserDao;
import sk.upjs.paz.model.User;

public class RegisterService {
    private final UserDao userDao = Factory.INSTANCE.getUserDao();

    public void register(User user) {
        userDao.insert(user);
    }

    public boolean emailExists(String email) {
        return userDao.existsByEmail(email);
    }

    public String hashPassword(String password) {
        return password; // тимчасово
    }

    public boolean personalNumberExists(String rc) {return userDao.existsByPersonalNumber(rc);
    }


}
