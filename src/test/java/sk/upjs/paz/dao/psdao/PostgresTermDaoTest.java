package sk.upjs.paz.dao.psdao;

import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import sk.upjs.paz.model.Term;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresTermDaoTest {

    private static JdbcTemplate jdbc;
    private PostgresTermDao termDao;

    @BeforeAll
    static void setupDb() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5433/cn");
        ds.setUsername("casnast");
        ds.setPassword("casnast");

        jdbc = new JdbcTemplate(ds);
    }

    @BeforeEach
    void setup() {
        termDao = new PostgresTermDao(jdbc);

        // FK-safe cleanup
        jdbc.execute("TRUNCATE TABLE cn.schedule RESTART IDENTITY CASCADE");
        jdbc.execute("TRUNCATE TABLE cn.terms RESTART IDENTITY CASCADE");
        jdbc.execute("TRUNCATE TABLE cn.users RESTART IDENTITY CASCADE");
        jdbc.execute("TRUNCATE TABLE cn.districts RESTART IDENTITY CASCADE");

        // district
        jdbc.update("""
            INSERT INTO cn.districts
            (name, address_of_center, kontakt, psc, created_at, region)
            VALUES ('Košice I','Main 1','k1@cn.sk',40101,NOW(),'KE')
        """);

        // user
        jdbc.update("""
            INSERT INTO cn.users
            (name, surname, email, phone_number, password_hash,
             role_id, rodne_cislo, date_of_birth, adresa,
             district_id, created_at, update_at)
            VALUES
            ('Peter','Novak','p@test.sk','0900','x',3,'900101/1234',
             '1990-01-01','Addr',1,NOW(),NOW())
        """);
    }

    private Term sampleTerm(LocalDate date) {
        return new Term(
                0,
                "Lekárska prehliadka",
                date,
                LocalTime.of(8, 0),
                LocalTime.of(10, 0),
                "Poliklinika",
                30,
                1L
        );
    }

    @Test
    void create_and_getAll_shouldReturnInsertedTerm() {
        termDao.create(sampleTerm(LocalDate.now().plusDays(1)));

        List<Term> terms = termDao.getAll();
        assertEquals(1, terms.size());
        assertEquals("Lekárska prehliadka", terms.get(0).getType());
    }

    @Test
    void update_shouldModifyExistingTerm() {
        termDao.create(sampleTerm(LocalDate.now().plusDays(1)));

        Long id = jdbc.queryForObject(
                "SELECT id_terms FROM cn.terms",
                Long.class
        );

        Term t = new Term(
                id,
                "Updated type",
                LocalDate.now().plusDays(2),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                "New address",
                50,
                1L
        );

        termDao.update(t);

        Term fromDb = jdbc.queryForObject(
                "SELECT * FROM cn.terms WHERE id_terms = ?",
                (rs, rn) -> new Term(
                        rs.getLong("id_terms"),
                        rs.getString("type"),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime(),
                        rs.getString("address"),
                        rs.getInt("capacity"),
                        rs.getLong("okres")
                ),
                id
        );

        assertEquals("Updated type", fromDb.getType());
        assertEquals(50, fromDb.getCapacity());
    }

    @Test
    void getByDistrict_shouldFilterByDistrictAndNotCanceled() {
        termDao.create(sampleTerm(LocalDate.now().plusDays(1)));
        termDao.create(sampleTerm(LocalDate.now().plusDays(2)));

        List<Term> terms = termDao.getByDistrict(1L);
        assertEquals(2, terms.size());
    }

    @Test
    void findVisibleForUser_shouldReturnFutureTerms() {
        termDao.create(sampleTerm(LocalDate.now().plusDays(5)));

        List<Term> terms = termDao.findVisibleForUser(
                1L,
                1L,
                LocalDate.now()
        );

        assertEquals(1, terms.size());
    }

    @Test
    void findVisibleForUser_shouldReturnPastTermIfUserRegistered() {
        // past term
        termDao.create(sampleTerm(LocalDate.now().minusDays(3)));

        Long termId = jdbc.queryForObject(
                "SELECT id_terms FROM cn.terms",
                Long.class
        );

        // user is registered
        jdbc.update("""
            INSERT INTO cn.schedule
            (id_terms, id_user, status_of_application)
            VALUES (?, ?, 'pending')
        """, termId, 1L);

        List<Term> terms = termDao.findVisibleForUser(
                1L,
                1L,
                LocalDate.now()
        );

        assertEquals(1, terms.size());
    }
}
