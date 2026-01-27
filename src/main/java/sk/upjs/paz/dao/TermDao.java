package sk.upjs.paz.dao;

import sk.upjs.paz.model.Term;

import java.time.LocalDate;
import java.util.List;

public interface TermDao {
    List<Term> getAll();

    void create(Term term);
    void update(Term term);

    List<Term> getByDistrict(long districtId);

    List<Term> findVisibleForUser(long userId, long districtId, LocalDate today);
}