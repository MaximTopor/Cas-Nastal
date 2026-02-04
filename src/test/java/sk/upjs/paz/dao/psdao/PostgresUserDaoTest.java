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

        jdbcTemplate.execute("TRUNCATE TABLE cn.users RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.roles RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.districts RESTART IDENTITY CASCADE");

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

    private long getInsertedId() {
        return jdbcTemplate.queryForObject("SELECT id_user FROM cn.users", Long.class);
    }

    // =========================
    // exists
    // =========================

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

    // =========================
    // getBy...
    // =========================

    @Test
    void getByEmail_shouldReturnUser() {
        userDao.insert(sampleUser());

        User u = userDao.getByEmail("peter@test.sk");

        assertNotNull(u);
        assertEquals("Peter", u.getName());
    }

    @Test
    void getByEmail_shouldReturnNull_whenNotFound() {
        assertNull(userDao.getByEmail("missing@test.sk"));
    }

    @Test
    void getById_shouldReturnUser() {
        userDao.insert(sampleUser());
        long id = getInsertedId();

        User u = userDao.getById(id);

        assertNotNull(u);
        assertEquals(id, u.getIdUser());
        assertEquals("peter@test.sk", u.getEmail());
    }

    @Test
    void getById_shouldReturnNull_whenNotFound() {
        assertNull(userDao.getById(999L));
    }

    @Test
    void getByPhone_shouldReturnUser() {
        userDao.insert(sampleUser());

        User u = userDao.getByPhone("0900000000");

        assertNotNull(u);
        assertEquals("peter@test.sk", u.getEmail());
    }

    @Test
    void getByPhone_shouldReturnNull_whenNotFound() {
        assertNull(userDao.getByPhone("0999999999"));
    }

    @Test
    void getByPersonalNumber_shouldReturnUser_whenNotAdmin() {
        userDao.insert(sampleUser());

        User u = userDao.getByPersonalNumber("900101/1234");

        assertNotNull(u);
        assertEquals("Peter", u.getName());
    }

    @Test
    void getByPersonalNumber_shouldReturnNull_whenAdminRole() {
        User admin = sampleUser();
        admin.setEmail("admin2@test.sk");
        admin.setPersonalNumber("111111/1111");
        admin.setRoleId(1); // ADMIN

        userDao.insert(admin);

        assertNull(userDao.getByPersonalNumber("111111/1111"));
    }

    // =========================
    // getAll / getByDistrict
    // =========================

    @Test
    void getAll_shouldReturnOnlyUsersWithRoleUSER() {
        userDao.insert(sampleUser());

        User officer = sampleUser();
        officer.setEmail("off@test.sk");
        officer.setPersonalNumber("800101/1111");
        officer.setRoleId(2); // OFFICER
        userDao.insert(officer);

        List<User> users = userDao.getAll();
        assertEquals(1, users.size());
        assertEquals(3, users.get(0).getRoleId());
    }

    @Test
    void getByDistrict_shouldFilterUsers() {
        userDao.insert(sampleUser());

        // інший district
        jdbcTemplate.update("""
            INSERT INTO cn.districts
            (name, address_of_center, kontakt, psc, created_at, region)
            VALUES ('Košice II','Main 2','k2@cn.sk',40102,NOW(),'KE')
        """);

        User otherDistrict = sampleUser();
        otherDistrict.setEmail("other@test.sk");
        otherDistrict.setPersonalNumber("700101/2222");
        otherDistrict.setDistrictId(2L);
        userDao.insert(otherDistrict);

        List<User> users = userDao.getByDistrict(1L);
        assertEquals(1, users.size());
        assertEquals(1L, users.get(0).getDistrictId());
    }

    // =========================
    // update
    // =========================

    @Test
    void update_shouldModifyEditableFields() {
        userDao.insert(sampleUser());
        long id = getInsertedId();

        User u = userDao.getById(id);
        u.setName("Janko");
        u.setSurname("Mrkvicka");
        u.setEmail("new@test.sk");
        u.setPhoneNumber("0911111111");
        u.setAddress("New Addr");
        u.setRoleId(2);      // OFFICER
        u.setDistrictId(1L);

        userDao.update(u);

        User fromDb = userDao.getById(id);

        assertNotNull(fromDb);
        assertEquals("Janko", fromDb.getName());
        assertEquals("Mrkvicka", fromDb.getSurname());
        assertEquals("new@test.sk", fromDb.getEmail());
        assertEquals("0911111111", fromDb.getPhoneNumber());
        assertEquals("New Addr", fromDb.getAddress());
        assertEquals(2, fromDb.getRoleId());
    }

    @Test
    void update_shouldNotThrow_whenIdDoesNotExist() {
        User u = sampleUser();
        u.setIdUser(999L);
        assertDoesNotThrow(() -> userDao.update(u));
    }

    // =========================
    // roles
    // =========================

    @Test
    void getRoleName_shouldReturnRoleName() {
        userDao.insert(sampleUser());
        long id = getInsertedId();

        assertEquals("USER", userDao.getRoleName(id));
    }

    @Test
    void hasRole_shouldWork() {
        userDao.insert(sampleUser());
        long id = getInsertedId();

        assertTrue(userDao.hasRole(id, "USER"));
        assertFalse(userDao.hasRole(id, "ADMIN"));
    }
}
