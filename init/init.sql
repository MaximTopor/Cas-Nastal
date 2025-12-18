-- Create schema
CREATE SCHEMA IF NOT EXISTS cn;
SET search_path TO cn;

-----------------------------------------------------
-- TABLE: Districts
-----------------------------------------------------
CREATE TABLE IF NOT EXISTS districts (
                                         id_district BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                         name VARCHAR(50) NOT NULL,
    adres_of_centr VARCHAR(150) NOT NULL,
    kontakt VARCHAR(100) NOT NULL,
    psc INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    region VARCHAR(100) NOT NULL,
    CONSTRAINT districts_name_unique UNIQUE(name),
    CONSTRAINT districts_adres_unique UNIQUE(adres_of_centr),
    CONSTRAINT districts_psc_unique UNIQUE(psc)
    );

-----------------------------------------------------
-- TABLE: Roles
-----------------------------------------------------
CREATE TABLE IF NOT EXISTS roles (
                                     id_roles INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     name_of_role VARCHAR(45) NOT NULL,
    descriprion TEXT,
    craeted_at TIMESTAMP NOT NULL,
    CONSTRAINT roles_name_unique UNIQUE(name_of_role)
    );

-----------------------------------------------------
-- TABLE: Users
-----------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
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
    distrikt_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    update_at TIMESTAMP,
    CONSTRAINT users_email_unique UNIQUE(email),
    CONSTRAINT users_rc_unique UNIQUE(rodne_cislo),
    CONSTRAINT fk_user_district
    FOREIGN KEY (distrikt_id)
    REFERENCES districts (id_district)
    ON DELETE CASCADE,
    CONSTRAINT fk_user_role
    FOREIGN KEY (role_id)
    REFERENCES roles (id_roles)
    );

CREATE INDEX idx_user_district ON users(distrikt_id);
CREATE INDEX idx_user_role ON users(role_id);

-----------------------------------------------------
-- TABLE: Status
-----------------------------------------------------
CREATE TABLE IF NOT EXISTS status (
                                      id_status BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL
    );

-----------------------------------------------------
-- TABLE: Terms
-----------------------------------------------------
CREATE TABLE IF NOT EXISTS terms (
                                     id_terms BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                     type VARCHAR(150) NOT NULL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    address VARCHAR(100) NOT NULL,
    capacity INTEGER NOT NULL,
    okres BIGINT NOT NULL,
    CONSTRAINT fk_terms_district
    FOREIGN KEY (okres)
    REFERENCES districts (id_district)
    );

CREATE INDEX idx_terms_okres ON terms(okres);

-----------------------------------------------------
-- TABLE: Schedule
-----------------------------------------------------
CREATE TABLE IF NOT EXISTS schedule (
                                        id_schedule BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                                        status_of_application TEXT NOT NULL CHECK
    (status_of_application IN ('pending', 'end', 'cancelled')),

    id_user BIGINT NOT NULL,
    id_terms BIGINT NOT NULL,

    CONSTRAINT fk_schedule_user
    FOREIGN KEY (id_user)
    REFERENCES users (id_user)
    ON DELETE CASCADE,

    CONSTRAINT fk_schedule_terms
    FOREIGN KEY (id_terms)
    REFERENCES terms (id_terms)
    ON DELETE CASCADE
    );

CREATE INDEX idx_schedule_user ON schedule(id_user);
CREATE INDEX idx_schedule_terms ON schedule(id_terms);

-----------------------------------------------------
-- TABLE: Message
-----------------------------------------------------
CREATE TABLE IF NOT EXISTS message (
                                       id_message BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                       id_sender BIGINT NOT NULL,
                                       id_recipient BIGINT NOT NULL,
                                       subject VARCHAR(45) NOT NULL,
    text TEXT NOT NULL,
    date_sent TIMESTAMP NOT NULL,
    message TEXT NOT NULL,
    update_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    last_action TEXT NOT NULL CHECK
(last_action IN ('CREATE', 'UPDATE', 'DELETE')),

    CONSTRAINT fk_message_sender
    FOREIGN KEY (id_sender)
    REFERENCES users(id_user)
    ON DELETE CASCADE,

    CONSTRAINT fk_message_recipient
    FOREIGN KEY (id_recipient)
    REFERENCES users(id_user)
    ON DELETE CASCADE
    );

CREATE INDEX idx_message_sender ON message(id_sender);
CREATE INDEX idx_message_recipient ON message(id_recipient);

-----------------------------------------------------
-- TABLE: Status_History
-----------------------------------------------------
CREATE TABLE IF NOT EXISTS status_history (
                                              id_history INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                              user_id BIGINT NOT NULL,
                                              status_id BIGINT NOT NULL,
                                              changed_by BIGINT NOT NULL,
                                              changet_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                              reason TEXT NOT NULL,
                                                is_current BOOLEAN NOT NULL,

                                              CONSTRAINT fk_history_status
                                              FOREIGN KEY (status_id)
    REFERENCES status(id_status)
    ON DELETE CASCADE,

    CONSTRAINT fk_history_changed_by
    FOREIGN KEY (changed_by)
    REFERENCES users(id_user),

    CONSTRAINT fk_history_user
    FOREIGN KEY (user_id)
    REFERENCES users(id_user)
    );

CREATE INDEX idx_history_status ON status_history(status_id);
CREATE INDEX idx_history_changed_by ON status_history(changed_by);
CREATE INDEX idx_history_user ON status_history(user_id);