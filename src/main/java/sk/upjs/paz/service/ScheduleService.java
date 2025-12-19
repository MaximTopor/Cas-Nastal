package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.ScheduleDao;
import sk.upjs.paz.dao.TermDao;
import sk.upjs.paz.dao.UserDao;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.model.User;

import java.time.LocalDate;
import java.util.List;

public class ScheduleService {

    private final TermDao termDao = Factory.INSTANCE.getTermDao();
    private final ScheduleDao scheduleDao = Factory.INSTANCE.getScheduleDao();
    private final UserDao userDao = Factory.INSTANCE.getUserDao();

    /* ================= BASIC OPERATIONS ================= */

    public void register(long userId, long termId) {
        scheduleDao.insert(userId, termId);
    }

    public void unregister(long userId, long termId) {
        scheduleDao.delete(userId, termId);
    }

    public boolean isUserRegistered(long userId, long termId) {
        return scheduleDao.exists(userId, termId);
    }

    public int getRegisteredCount(long termId) {
        return scheduleDao.getByTerm(termId).size();
    }

    public boolean hasRole(long userId, String role) {
        return userDao.hasRole(userId, role);
    }

    public boolean isTermFull(Term term) {
        return getRegisteredCount(term.getIdTerms()) >= term.getCapacity();
    }

    public void cancelTerm(long termId) {
        scheduleDao.cancelTerm(termId);
    }

    public List<Term> getAllTerms(){
        return termDao.getAll();
    }

    /* ================= VISIBILITY ================= */

    public List<Term> getVisibleTermsForUser(User user) {

        long districtId = user.getDistrictId();
        LocalDate today = LocalDate.now();

        if (userDao.hasRole(user.getIdUser(), "ADMIN")) {
            return termDao.getAll();
        }

        if (userDao.hasRole(user.getIdUser(), "OFFICER")) {
            return termDao.getByDistrict(districtId);
        }

        return termDao.findVisibleForUser(
                user.getIdUser(),
                districtId,
                today
        );
    }

    /* ================= FILTERING ================= */

    public List<Term> filterTerms(
            User user,
            boolean onlyMy,
            boolean onlyActive
    ) {
        List<Term> terms = getVisibleTermsForUser(user);
        LocalDate today = LocalDate.now();
        long userId = user.getIdUser();

        return terms.stream()
                .filter(t -> !onlyActive || !t.getDate().isBefore(today))
                .filter(t -> !onlyMy || isUserRegistered(userId, t.getIdTerms()))
                .toList();
    }
}
