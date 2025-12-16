package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.UserDao;
import sk.upjs.paz.model.User;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        this.userDao = Factory.INSTANCE.getUserDao();
    }

    /**
     * Overí prihlasovacie údaje používateľa.
     *
     * @param email email zadaný používateľom
     * @param rawPassword heslo zadané používateľom (zatiaľ plain-text)
     * @return prihlásený User alebo null, ak autentifikácia zlyhá
     */
    public User authenticate(String email, String rawPassword) {
        System.out.println("AUTH: email=" + email);
        if (email == null || email.isBlank()) {
            return null;
        }

        if (rawPassword == null || rawPassword.isBlank()) {
            return null;
        }

        User user = userDao.getByEmail(email);
        System.out.println("USER FROM DB = " + user);

        if (user == null) {
            return null;
        }

        // ZATIAĽ jednoduché porovnanie (neskôr hash)
        if (!(user.getPasswordHash().equals(rawPassword))) {
            return null;
        }

        return user;
    }

    public User getUserById(long userId) {
        return userDao.getById(userId);
    }
}
