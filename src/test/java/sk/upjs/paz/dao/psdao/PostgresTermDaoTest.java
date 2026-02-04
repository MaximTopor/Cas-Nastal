package sk.upjs.paz.dao.psdao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.upjs.paz.model.Term;
import sk.upjs.paz.testcontainer.TestContainer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresTermDaoTest extends TestContainer {

    private PostgresTermDao termDao;

    @BeforeEach
    void setup() {
        termDao = new PostgresTermDao(jdbcTemplate);

        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS cn");
        jdbcTemplate.execute("SET search_path TO cn");

        // потрібні таблиці для FK/EXISTS у findVisibleForUser
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS cn.districts (
                id_district BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                address_of_center VARCHAR(150) NOT NULL,
                kontakt VARCHAR(100) NOT NULL,
                psc INTEGER NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                region VARCHAR(100) NOT NULL
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS cn.users (
                id_user BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                name VARCHAR(50),
                surname VARCHAR(50),
                email VARCHAR(100),
                phone_number VARCHAR(15),
                password_hash VARCHAR(255),
                role_id INTEGER,
                rodne_cislo VARCHAR(12),
                date_of_birth DATE,
                adresa VARCHAR(100),
                district_id BIGINT NOT NULL,
                created_at TIMESTAMP DEFAULT NOW(),
                update_at TIMESTAMP DEFAULT NOW(),
                CONSTRAINT fk_user_district
                    FOREIGN KEY (district_id)
                    REFERENCES cn.districts(id_district)
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS cn.terms (
                id_terms BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                type VARCHAR(100) NOT NULL,
                date DATE NOT NULL,
                start_time TIME NOT NULL,
                end_time TIME NOT NULL,
                address VARCHAR(150) NOT NULL,
                capacity INTEGER NOT NULL,
                okres BIGINT NOT NULL,
                CONSTRAINT fk_terms_district
                    FOREIGN KEY (okres)
                    REFERENCES cn.districts(id_district)
            )
        """);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS cn.schedule (
                id_schedule BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                status_of_application VARCHAR(30) NOT NULL DEFAULT 'pending',
                id_user BIGINT NOT NULL,
                id_terms BIGINT NOT NULL,
                CONSTRAINT fk_schedule_user
                    FOREIGN KEY (id_user) REFERENCES cn.users(id_user)
                    ON DELETE CASCADE,
                CONSTRAINT fk_schedule_terms
                    FOREIGN KEY (id_terms) REFERENCES cn.terms(id_terms)
                    ON DELETE CASCADE
            )
        """);

        // clean
        jdbcTemplate.execute("TRUNCATE TABLE cn.schedule RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.terms RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.users RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.districts RESTART IDENTITY CASCADE");

        // base data
        jdbcTemplate.update("""
            INSERT INTO cn.districts (name, address_of_center, kontakt, psc, created_at, region)
            VALUES ('Košice I','Main 1','k1@cn.sk',40101,NOW(),'KE')
        """);

        jdbcTemplate.update("""
            INSERT INTO cn.users
            (name, surname, email, phone_number, password_hash,
             role_id, rodne_cislo, date_of_birth, adresa,
             district_id, created_at, update_at)
            VALUES
            ('Peter','Novak','p@test.sk','0900','x',3,'900101/1234',
             '1990-01-01','Addr',1,NOW(),NOW())
        """);
    }

    private Term sampleTerm(LocalDate date, String type) {
        return new Term(
                0,
                type,
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
        termDao.create(sampleTerm(LocalDate.now().plusDays(1), "Lekárska prehliadka"));

        List<Term> terms = termDao.getAll();
        assertEquals(1, terms.size());
        assertEquals("Lekárska prehliadka", terms.get(0).getType());
    }

    @Test
    void update_shouldModifyExistingTerm() {
        termDao.create(sampleTerm(LocalDate.now().plusDays(1), "Old"));

        Long id = jdbcTemplate.queryForObject("SELECT id_terms FROM cn.terms", Long.class);

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

        Term fromDb = jdbcTemplate.queryForObject(
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

        assertNotNull(fromDb);
        assertEquals("Updated type", fromDb.getType());
        assertEquals(50, fromDb.getCapacity());
        assertEquals("New address", fromDb.getAddress());
    }

    @Test
    void getByDistrict_shouldFilterByDistrictAndNotCanceled() {
        termDao.create(sampleTerm(LocalDate.now().plusDays(1), "Lekárska"));
        termDao.create(sampleTerm(LocalDate.now().plusDays(2), "canceled")); // має відфільтруватися

        List<Term> terms = termDao.getByDistrict(1L);
        assertEquals(1, terms.size());
        assertEquals("Lekárska", terms.get(0).getType());
    }

    @Test
    void findVisibleForUser_shouldReturnFutureTerms() {
        termDao.create(sampleTerm(LocalDate.now().plusDays(5), "Lekárska"));

        List<Term> terms = termDao.findVisibleForUser(1L, 1L, LocalDate.now());

        assertEquals(1, terms.size());
    }

    @Test
    void findVisibleForUser_shouldReturnPastTermIfUserRegistered() {
        termDao.create(sampleTerm(LocalDate.now().minusDays(3), "Lekárska"));

        Long termId = jdbcTemplate.queryForObject("SELECT id_terms FROM cn.terms", Long.class);

        jdbcTemplate.update("""
            INSERT INTO cn.schedule (id_terms, id_user, status_of_application)
            VALUES (?, ?, 'pending')
        """, termId, 1L);

        List<Term> terms = termDao.findVisibleForUser(1L, 1L, LocalDate.now());

        assertEquals(1, terms.size());
        assertEquals(termId, terms.get(0).getIdTerms());
    }

    @Test
    void findVisibleForUser_shouldNotReturnPastTermIfUserNotRegistered() {
        termDao.create(sampleTerm(LocalDate.now().minusDays(3), "Lekárska"));

        List<Term> terms = termDao.findVisibleForUser(1L, 1L, LocalDate.now());

        assertTrue(terms.isEmpty());
    }

    @Test
    void findVisibleForUser_shouldNotReturnPastTermIfRegistrationCancelled() {
        termDao.create(sampleTerm(LocalDate.now().minusDays(3), "Lekárska"));

        Long termId = jdbcTemplate.queryForObject("SELECT id_terms FROM cn.terms", Long.class);

        // ВАЖЛИВО: у твоєму SQL фільтр: status_of_application <> 'cancelled'
        jdbcTemplate.update("""
            INSERT INTO cn.schedule (id_terms, id_user, status_of_application)
            VALUES (?, ?, 'cancelled')
        """, termId, 1L);

        List<Term> terms = termDao.findVisibleForUser(1L, 1L, LocalDate.now());

        assertTrue(terms.isEmpty());
    }
}
