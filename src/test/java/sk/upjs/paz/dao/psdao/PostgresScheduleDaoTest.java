package sk.upjs.paz.dao.psdao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.upjs.paz.model.Schedule;
import sk.upjs.paz.testcontainer.TestContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresScheduleDaoTest extends TestContainer {

    private PostgresScheduleDao dao;

    @BeforeEach
    void setUp() {
        dao = new PostgresScheduleDao(jdbcTemplate);

        // ===== schema =====
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS cn");
        jdbcTemplate.execute("SET search_path TO cn");

        // ===== tables =====
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS cn.districts (
                id_district BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                address_of_center VARCHAR(150) NOT NULL,
                kontakt VARCHAR(100) NOT NULL,
                psc INTEGER NOT NULL,
                created_at TIMESTAMP NOT NULL,
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
                created_at TIMESTAMP,
                update_at TIMESTAMP,
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
                    ON DELETE CASCADE,
                CONSTRAINT uq_schedule UNIQUE (id_user, id_terms)
            )
        """);

        // ===== clean =====
        jdbcTemplate.execute("TRUNCATE TABLE cn.schedule RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.terms RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.users RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.districts RESTART IDENTITY CASCADE");

        // ===== base data =====
        jdbcTemplate.update("""
            INSERT INTO cn.districts
            (name, address_of_center, kontakt, psc, created_at, region)
            VALUES ('Košice I','Addr','a@a.sk',40101,NOW(),'KE')
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

        jdbcTemplate.update("""
            INSERT INTO cn.terms
            (type, date, start_time, end_time, address, capacity, okres)
            VALUES
            ('Lekárska', CURRENT_DATE, '08:00','10:00','Addr',30,1)
        """);
    }

    @Test
    void insert_and_exists_shouldWork() {
        dao.insert(1L, 1L);
        assertTrue(dao.exists(1L, 1L));
    }

    @Test
    void getByUser_shouldReturnSchedule() {
        dao.insert(1L, 1L);

        List<Schedule> list = dao.getByUser(1L);

        assertEquals(1, list.size());
        assertEquals("pending", list.get(0).getStatusOfApplication());
    }

    @Test
    void getByTerm_shouldReturnSchedule() {
        dao.insert(1L, 1L);

        List<Schedule> list = dao.getByTerm(1L);

        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getUserId());
    }

    @Test
    void update_shouldChangeStatus() {
        dao.insert(1L, 1L);

        Schedule s = jdbcTemplate.queryForObject(
                "SELECT * FROM cn.schedule",
                (rs, rn) -> new Schedule(
                        rs.getLong("id_schedule"),
                        rs.getString("status_of_application"),
                        rs.getLong("id_user"),
                        rs.getLong("id_terms")
                )
        );

        s.setStatusOfApplication("end");
        dao.update(s);

        String status = jdbcTemplate.queryForObject(
                "SELECT status_of_application FROM cn.schedule",
                String.class
        );

        assertEquals("end", status);
    }

    @Test
    void delete_shouldRemoveSchedule() {
        dao.insert(1L, 1L);
        dao.delete(1L, 1L);
        assertFalse(dao.exists(1L, 1L));
    }
}
