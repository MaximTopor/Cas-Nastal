package sk.upjs.paz.dao.psdao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.upjs.paz.model.Status;
import sk.upjs.paz.testcontainer.TestContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresStatusHistoryTest extends TestContainer {

    private PostgresStatusHistoryDao dao;

    @BeforeEach
    void setUp() {
        dao = new PostgresStatusHistoryDao(jdbcTemplate);

        // =========================
        // SCHEMA
        // =========================
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS cn");
        jdbcTemplate.execute("SET search_path TO cn");

        // =========================
        // TABLES (ORDER MATTERS)
        // =========================

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

        jdbcTemplate.execute("""
    CREATE TABLE IF NOT EXISTS cn.status (
        id_status BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        description TEXT,
        created_at TIMESTAMP NOT NULL
    )
""");

        jdbcTemplate.execute("""
    CREATE TABLE IF NOT EXISTS cn.status_history (
        id_history INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        user_id BIGINT NOT NULL,
        status_id BIGINT NOT NULL,
        changed_by BIGINT NOT NULL,
        changet_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
        reason TEXT NOT NULL,
        is_current BOOLEAN NOT NULL,

        CONSTRAINT fk_history_status
            FOREIGN KEY (status_id)
            REFERENCES cn.status(id_status)
            ON DELETE CASCADE,

        CONSTRAINT fk_history_changed_by
            FOREIGN KEY (changed_by)
            REFERENCES cn.users(id_user),

        CONSTRAINT fk_history_user
            FOREIGN KEY (user_id)
            REFERENCES cn.users(id_user)
    )
""");


        jdbcTemplate.execute("TRUNCATE TABLE cn.status_history RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.status RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.users RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.roles RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE cn.districts RESTART IDENTITY CASCADE");

        jdbcTemplate.update("""
            INSERT INTO cn.roles (name_of_role, craeted_at)
            VALUES ('ADMIN', NOW()), ('USER', NOW())
        """);

        jdbcTemplate.update("""
    INSERT INTO cn.districts
    (name, address_of_center, kontakt, psc, created_at, region)
    VALUES
    ('Košice I', 'Main 1', 'k1@cn.sk', 40101, NOW(), 'KE')
""");

        jdbcTemplate.update("""
            INSERT INTO cn.users
            (name, surname, email, password_hash, role_id,
             rodne_cislo, date_of_birth, adresa, district_id,
             created_at, update_at)
            VALUES
            ('Admin','Sys','admin@test.sk','x',1,'000000/0000',
             '1990-01-01','Addr',1,NOW(),NOW()),
            ('Peter','Novak','p@test.sk','x',2,'900101/1234',
             '1990-01-01','Addr',1,NOW(),NOW())
        """);

        jdbcTemplate.update("""
            INSERT INTO cn.status (name, description, created_at)
            VALUES
            ('FREE','Free',NOW()),
            ('PREPARING','Preparing',NOW()),
            ('MOBILIZED','Mobilized',NOW())
        """);
    }

    @Test
    void getAllStatuses_shouldReturnAll() {
        List<Status> statuses = dao.getAllStatuses();

        assertEquals(3, statuses.size());
        assertEquals("FREE", statuses.get(0).getName());
    }

    @Test
    void insert_shouldCreateCurrentStatus() {
        Long userId = 2L;    // Peter
        Long statusId = 1L;  // FREE
        Long adminId = 1L;   // Admin

        dao.insert(userId, statusId, adminId, "Initial");

        Long current = dao.getCurrentStatusId(userId);
        assertEquals(statusId, current);
    }

    @Test
    void getCurrentStatus_shouldReturnStatusObject() {
        dao.insert(2L, 3L, 1L, "Mobilized");

        Status status = dao.getCurrentStatus(2L);

        assertNotNull(status);
        assertEquals("MOBILIZED", status.getName());
    }
}
