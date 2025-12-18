package sk.upjs.paz.dao;

import sk.upjs.paz.model.Term;

import java.time.LocalDate;
import java.util.List;

public interface TermDao {
    List<Term> getAll();
    Term getById(long id);

    void create(Term term);
    void update(Term term);
    void delete(long id);

    List<Term> getByDistrict(long districtId);
    List<Term> getByDate(java.time.LocalDate date);

    List<Term> findVisibleForUser(long userId, long districtId, LocalDate today);
}
