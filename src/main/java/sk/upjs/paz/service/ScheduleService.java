package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.ScheduleDao;
import sk.upjs.paz.dao.TermDao;
import sk.upjs.paz.model.Term;

import java.util.List;

public class ScheduleService {

    private final TermDao termDao = Factory.INSTANCE.getTermDao();
    private final ScheduleDao scheduleDao = Factory.INSTANCE.getScheduleDao();

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

    public void deleteTerm(long termId) {
        scheduleDao.deleteTerm(termId);
    }
}
