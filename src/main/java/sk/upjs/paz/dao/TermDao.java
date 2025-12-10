package sk.upjs.paz.dao;

import sk.upjs.paz.model.Term;

import java.util.List;

public interface TermDao {
    List<Term> getAll();
    Term getById(long id);

    void insert(Term term);
    void update(Term term);
    void delete(long id);

    List<Term> getByDistrict(long districtId);
    List<Term> getByDate(java.time.LocalDate date);
}
