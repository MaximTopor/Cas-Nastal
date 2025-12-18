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
    address_of_center,
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
    district_id,
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
          'Peter','Novak','peter.novak@test.sk',
            '0900000002','test123',3,'900101/1234',DATE '1995-05-10',
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
      ),
      (
          '1',
          '1',
          '1',
          '1',
          '1',
          1,
          '1',
          DATE '1999-01-01',
          '1',
          3,
          NOW(),
          NOW()
      )
    ON CONFLICT (id_user) DO NOTHING;

INSERT INTO cn.terms (
    type,
    date,
    start_time,
    end_time,
    address,
    capacity,
    okres
) VALUES ('Lekárska prehliadka',
          '2025-12-11',
          '08:00',
          '10:00',
          'Poliklinika Košice, Trieda SNP 1',
          30,
          1),

-- 2
         ('Očkovanie',
          '2025-03-21',
          '09:00',
          '12:00',
          'Zdravotné stredisko Terasa, Košice',
          50,
          1),

-- 3
         ('Administratívne vybavenie',
          '2025-03-22',
          '13:00',
          '15:00',
          'Mestský úrad Košice, Hlavná 68',
          20,
          1),

-- 4
         ('Školenie civilnej ochrany',
          '2025-03-25',
          '10:00',
          '14:00',
          'Kultúrne centrum Nad jazerom',
          40,
          1),
('Psychologické vyšetrenie', '2025-04-02', '08:30', '11:00', 'Psychologické centrum Košice, Južná trieda 15', 15, 1),
('Krvné testy', '2025-04-05', '07:00', '09:30', 'Laboratórium MedLab, Trieda SNP 12', 25, 1),
('Zdravotná administratíva', '2025-04-10', '13:00', '16:00', 'Úrad verejného zdravotníctva Košice', 20, 2),
('Kurz prvej pomoci', '2026-05-03', '09:00', '15:00', 'Červený kríž Košice, Moyzesova 20', 30, 3),
('Bezpečnostné školenie', '2026-05-15', '10:00', '14:00', 'Civilná ochrana Košice, Magnezitárska 5', 40, 1),
('Opakovaná lekárska prehliadka', '2025-06-01', '08:00', '10:00', 'Poliklinika Košice, Trieda SNP 1', 30, 1);
INSERT INTO cn.schedule (
    id_terms,
    id_user,
    status_of_application
) VALUES
      (1, 2, 'pending'),
      (2, 2, 'end'),
      (4, 3, 'pending'),
      (3, 2, 'cancelled');

COMMIT;