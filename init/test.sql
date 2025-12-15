-- =========================
-- TEST DATA (SCHEMA cn)
-- =========================

BEGIN;

-- =========================
-- ROLES
-- =========================
INSERT INTO cn.roles (
    name_of_role,
    descriprion,
    craeted_at
) VALUES
      ('ADMIN', 'Administrator role', NOW()),
      ('OFFICER', 'Officer role', NOW()),
      ('USER', 'Regular user role', NOW())
    ON CONFLICT (name_of_role) DO NOTHING;

-- =========================
-- DISTRICTS
-- =========================
INSERT INTO cn.districts (
    name,
    adres_of_centr,
    kontakt,
    psc,
    created_at,
    region
) VALUES
      ( 'Košice I', 'Main Street 1', 'kosice1@cn.sk', 40101, NOW(), 'Košice'),
      ( 'Košice II', 'Hlavná 10', 'kosice2@cn.sk', 40102, NOW(), 'Košice'),
      ( 'Košice III', 'Letná 5', 'kosice3@cn.sk', 40103, NOW(), 'Košice')
    ON CONFLICT (id_district) DO NOTHING;

-- =========================
-- USERS
-- =========================
INSERT INTO cn.users (
    name,
    surname,
    email,
    phone_number,
    password_hash,
    role_id,
    rodne_cislo,
    date_of_birth,
    adresa,
    distrikt_id,
    created_at,
    update_at
) VALUES
      (
          'Admin',
          'System',
          'admin@test.sk',
          '0900000001',
          'admin123',
          1,
          '000000/0001',
          DATE '1990-01-01',
          'Main Street 1',
          1,
          NOW(),
          NOW()
      ),
      (
          'Peter',
          'Novak',
          'peter.novak@test.sk',
          '0900000002',
          'test123',
          3,
          '900101/1234',
          DATE '1995-05-10',
          'Hlavná 10',
          2,
          NOW(),
          NOW()
      ),
      (
          'Jana',
          'Kovacova',
          'jana.kovacova@test.sk',
          '0900000003',
          'test123',
          2,
          '920202/5678',
          DATE '1992-02-02',
          'Letná 5',
          3,
          NOW(),
          NOW()
      )
    ON CONFLICT (id_user) DO NOTHING;

COMMIT;
