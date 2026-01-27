package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.UserDao;
import sk.upjs.paz.model.User;

import java.util.List;

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

    public String getRoleName(long userId) {
        return userDao.getRoleName(userId);
    }

    public void update(User user) {
        if (user == null) {
            return;
        }

        userDao.update(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public boolean canManageTerms(long userId) {
        String role = getRoleName(userId);
        return "ADMIN".equals(role) || "WORKER".equals(role);
    }

    public User getUserById(long userId) {
        return userDao.getById(userId);
    }

    public List<User> getUsersByDistrict(long districtId) {
        return userDao.getByDistrict(districtId);
    }


    public User getUserByPersonalNumber(String personalNumber) {
        if (personalNumber == null || personalNumber.isBlank()) {
            return null;
        }
        return userDao.getByPersonalNumber(personalNumber);
    }
    public boolean phoneExistsForOtherUser(String phone, long userId) {
        User existing = userDao.getByPhone(phone);
        return existing != null && existing.getIdUser() != userId;
    }

    public boolean emailExistsForOtherUser(String email, long userId) {
        User existing = userDao.getByEmail(email);
        return existing != null && existing.getIdUser() != userId;
    }
}
