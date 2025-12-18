package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import sk.upjs.paz.dao.TermDao;
import sk.upjs.paz.model.Term;

import java.time.LocalDate;
import java.util.List;

public class PostgresTermDao implements TermDao {
    private final JdbcOperations jdbc;

    public PostgresTermDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    private RowMapper<Term> termRowMapper() {
        return (rs, rowNum) -> new Term(
                rs.getLong("id_terms"),
                rs.getString("type"),
                rs.getDate("date").toLocalDate(),
                rs.getTime("start_time").toLocalTime(),
                rs.getTime("end_time").toLocalTime(),
                rs.getString("address"),
                rs.getInt("capacity"),
                rs.getLong("okres")
        );
    }

    @Override
    public List<Term> getAll() {
        System.out.println(">>> PostgresTermDao.getAll()");
        String sql = """
        SELECT *
        FROM cn.terms
        ORDER BY date, start_time
    """;
        var list = jdbc.query(sql, termRowMapper());
        System.out.println(">>> DAO terms count = " + list.size());
        return list;
    }



    @Override
    public Term getById(long id) {
        return null;
    }

    @Override
    public void create(Term term) {

        String sql = """
            INSERT INTO cn.terms (
                type,
                date,
                start_time,
                end_time,
                address,
                capacity,
                okres
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        jdbc.update(
                sql,
                term.getType(),
                term.getDate(),
                term.getStartTime(),
                term.getEndTime(),
                term.getAddress(),
                term.getCapacity(),
                term.getDistrictId()
        );
    }

    @Override
    public void update(Term term) {

        String sql = """
        UPDATE cn.terms
        SET type = ?,
            date = ?,
            start_time = ?,
            end_time = ?,
            address = ?,
            capacity = ?,
            okres = ?
        WHERE id_terms = ?
    """;

        jdbc.update(
                sql,
                term.getType(),
                term.getDate(),
                term.getStartTime(),
                term.getEndTime(),
                term.getAddress(),
                term.getCapacity(),
                term.getDistrictId(),
                term.getIdTerms()
        );
    }


    @Override
    public void delete(long id) {

    }

    @Override
    public List<Term> getByDistrict(long districtId) {

        String sql = """
        SELECT
            t.id_terms, t.type, t.date, t.start_time, t.end_time,
            t.address, t.capacity, t.okres
        FROM cn.terms t
        WHERE t.okres = ?
          AND t.status <> 'canceled'
        ORDER BY t.date, t.start_time
    """;

        return jdbc.query(sql, termRowMapper(), districtId);
    }


    @Override
    public List<Term> getByDate(LocalDate date) {
        return List.of();
    }

    @Override
    public List<Term> findVisibleForUser(long userId, long districtId, LocalDate today) {

        String sql = """
        SELECT
               t.id_terms,
               t.type,
               t.date,
               t.start_time,
               t.end_time,
               t.address,
               t.capacity,
               t.okres
        FROM cn.terms t
        WHERE t.okres = ?
          AND (
                t.date >= ?
                OR EXISTS (
                    SELECT 1
                    FROM cn.schedule s
                    WHERE s.id_terms = t.id_terms
                      AND s.id_user = ?
                      AND s.status_of_application <> 'cancelled'
                )
              )
        ORDER BY t.date, t.start_time
    """;

        return jdbc.query(
                sql,
                termRowMapper(),
                districtId,
                today,
                userId
        );
    }



}
