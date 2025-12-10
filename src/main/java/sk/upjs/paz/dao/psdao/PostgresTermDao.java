package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.TermDao;
import sk.upjs.paz.model.Term;

import java.time.LocalDate;
import java.util.List;

public class PostgresTermDao implements TermDao {
    private final JdbcOperations jdbc;

    public PostgresTermDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Term> getAll() {
        return List.of();
    }

    @Override
    public Term getById(long id) {
        return null;
    }

    @Override
    public void insert(Term term) {

    }

    @Override
    public void update(Term term) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<Term> getByDistrict(long districtId) {
        return List.of();
    }

    @Override
    public List<Term> getByDate(LocalDate date) {
        return List.of();
    }
}
