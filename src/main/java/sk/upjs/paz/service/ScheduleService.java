package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.TermDao;
import sk.upjs.paz.model.Term;

import java.util.List;

public class ScheduleService {

    private final TermDao termDao = Factory.INSTANCE.getTermDao();

    public List<Term> getAllTerms() {
        return termDao.getAll();
    }
}
