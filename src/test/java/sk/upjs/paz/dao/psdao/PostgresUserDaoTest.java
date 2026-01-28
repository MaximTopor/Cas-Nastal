package sk.upjs.paz.dao.psdao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.upjs.paz.model.User;
import sk.upjs.paz.testcontainer.TestContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresUserDaoTest extends TestContainer {

    private PostgresUserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new PostgresUserDao(jdbcTemplate);

        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS cn");
        jdbcTemplate.execute("SET search_path TO cn");

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS cn.roles (
                id_roles INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                name_of_role VARCHAR(45) NOT NULL,
                descriprion TEXT,
                craeted_at TIMESTAMP NOT NULL
            )
        """);

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
        name VARCHAR(50) NOT NULL,
        surname VARCHAR(50) NOT NULL,
        email VARCHAR(100) NOT NULL,
        phone_number VARCHAR(15),
        password_hash VARCHAR(255) NOT NULL,
        role_id INTEGER NOT NULL,
        rodne_cislo VARCHAR(12) NOT NULL,
        date_of_birth DATE NOT NULL,
        adresa VARCHAR(100) NOT NULL,
        district_id BIGINT NOT NULL,
        created_at TIMESTAMP NOT NULL,
        update_at TIMESTAMP,
        CONSTRAINT users_email_unique UNIQUE(email),
        CONSTRAINT users_rc_unique UNIQUE(rodne_cislo),
        CONSTRAINT fk_user_district
            FOREIGN KEY (district_id)
            REFERENCES cn.districts (id_district),
        CONSTRAINT fk_user_role
            FOREIGN KEY (role_id)
            REFERENCES cn.roles (id_roles)
    )
""");

        // =========================
        // CLEAN
        // =========================
        jdbcTemplate.execute("TRUNCATE TABLE cn.users RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.roles RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.districts RESTART IDENTITY CASCADE");

        // =========================
        // FK DATA
        // =========================
        jdbcTemplate.update("""
            INSERT INTO cn.roles (name_of_role, descriprion, craeted_at)
            VALUES ('ADMIN','admin',NOW()),
                   ('OFFICER','officer',NOW()),
                   ('USER','user',NOW())
        """);

        jdbcTemplate.update("""
            INSERT INTO cn.districts
            (name, address_of_center, kontakt, psc, created_at, region)
            VALUES ('Košice I','Main 1','k1@cn.sk',40101,NOW(),'KE')
        """);
    }

    // =========================
    // HELPER
    // =========================
    private User sampleUser() {
        User u = new User();
        u.setName("Peter");
        u.setSurname("Novak");
        u.setEmail("peter@test.sk");
        u.setPhoneNumber("0900000000");
        u.setPasswordHash("pass");
        u.setRoleId(3); // USER
        u.setPersonalNumber("900101/1234");
        u.setDateOfBirth(LocalDate.of(1990, 1, 1));
        u.setAddress("Main 1");
        u.setDistrictId(1L);
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());
        return u;
    }

    @Test
    void existsByEmail_shouldWork() {
        userDao.insert(sampleUser());

        assertTrue(userDao.existsByEmail("peter@test.sk"));
        assertFalse(userDao.existsByEmail("none@test.sk"));
    }

    @Test
    void existsByPersonalNumber_shouldWork() {
        userDao.insert(sampleUser());

        assertTrue(userDao.existsByPersonalNumber("900101/1234"));
        assertFalse(userDao.existsByPersonalNumber("000000/0000"));
    }

    @Test
    void getByEmail_shouldReturnUser() {
        userDao.insert(sampleUser());

        User u = userDao.getByEmail("peter@test.sk");

        assertNotNull(u);
        assertEquals("Peter", u.getName());
    }

    @Test
    void getAll_shouldReturnOnlyUsersWithRoleUSER() {
        userDao.insert(sampleUser());

        List<User> users = userDao.getAll();
        assertEquals(1, users.size());
    }

    @Test
    void getByDistrict_shouldFilterUsers() {
        userDao.insert(sampleUser());

        List<User> users = userDao.getByDistrict(1L);
        assertEquals(1, users.size());
    }

}
