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
        System.out.println(">>> PostgresTermDao.getAll()");
        String sql = """
        SELECT
            id_terms,
            type,
            date,
            start_time,
            end_time,
            address,
            capacity,
            okres
        FROM cn.terms
        ORDER BY date, start_time
    """;
        var list = jdbc.query(sql, (rs, rowNum) ->
                new Term(
                        rs.getLong("id_terms"),
                        rs.getString("type"),
                        rs.getObject("date", java.time.LocalDate.class),
                        rs.getObject("start_time", java.time.LocalTime.class),
                        rs.getObject("end_time", java.time.LocalTime.class),
                        rs.getString("address"),
                        rs.getInt("capacity"),
                        rs.getLong("okres")
                )
        );
        System.out.println(">>> DAO terms count = " + list.size());
        return list;
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
