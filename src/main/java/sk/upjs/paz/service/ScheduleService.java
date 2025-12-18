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

    public List<Term> getAllTerms() {
        System.out.println(">>> ScheduleService.getAllTerms()");
        return termDao.getAll();
    }

    public void register(long userId, long termId) {
        scheduleDao.insert(userId, termId);
    }

    public void unregister(long userId, long termId) {
        scheduleDao.delete(userId, termId);
    }

    public boolean isUserRegistered(long userId, long termId) {
        return scheduleDao.exists(userId, termId);
    }

    public void cancelTerm(long termId) {
        scheduleDao.cancelTerm(termId);
    }

    public List<Term> getVisibleTermsForUser(User user) {

    long districtId = user.getDistrictId();
    LocalDate today = LocalDate.now();

    // ADMIN / OFFICER

    if (userDao.hasRole(user.getIdUser(),"OFFICER")) {
        return termDao.getByDistrict(districtId);
    }
    if (userDao.hasRole(user.getIdUser(),"ADMIN")){
        return termDao.getAll();
    }

    // BEŽNÝ USER
    return termDao.findVisibleForUser(
            user.getIdUser(),
            districtId,
            today
    );
}
    public int getRegisteredCount(long termId) {
        return scheduleDao.getByTerm(termId).size();
    }

}
