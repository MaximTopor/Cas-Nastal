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
      ( 'Bratislavský kraj', 'Main Street 1', 'kosice1@cn.sk', 40101, NOW(), 'Košice'),
      ( 'Košický kraj', 'Hlavná 10', 'kosice2@cn.sk', 40102, NOW(), 'Košice'),
      ( 'Prešovský kraj', 'Letná 5', 'kosice3@cn.sk', 40103, NOW(), 'Košice')
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
      ),
      (
          '1',
          'Adam',
          '1',
          '0987762343',
          '1',
          1,
          '1',
          DATE '1999-01-01',
          'Nikdy',
          3,
          NOW(),
          NOW()
      ),
      ('Ján','Kováč','jan.kovac@test.sk','0901000001','x',3,'850101/0001','1985-01-01','Hlavná 1',1,NOW(),NOW()),
      ('Martin','Novák','martin.novak@test.sk','0901000002','x',3,'860202/0002','1986-02-02','Hlavná 2',1,NOW(),NOW()),
      ('Peter','Horváth','peter.horvath@test.sk','0901000003','x',3,'870303/0003','1987-03-03','Hlavná 3',1,NOW(),NOW()),
      ('Tomáš','Szabó','tomas.szabo@test.sk','0901000004','x',3,'880404/0004','1988-04-04','Hlavná 4',1,NOW(),NOW()),
      ('Marek','Varga','marek.varga@test.sk','0901000005','x',3,'890505/0005','1989-05-05','Hlavná 5',1,NOW(),NOW()),
      ('Lukáš','Baláž','lukas.balaz@test.sk','0901000006','x',3,'900606/0006','1990-06-06','Hlavná 6',3,NOW(),NOW()),
      ('Andrej','Molnár','andrej.molnar@test.sk','0901000007','x',3,'910707/0007','1991-07-07','Hlavná 7',3,NOW(),NOW()),
      ('Michal','Urban','michal.urban@test.sk','0901000008','x',3,'920808/0008','1992-08-08','Hlavná 8',3,NOW(),NOW()),
      ('Filip','Král','filip.kral@test.sk','0901000009','x',3,'930909/0009','1993-09-09','Hlavná 9',3,NOW(),NOW()),
      ('Róbert','Hudec','robert.hudec@test.sk','0901000010','x',3,'941010/0010','1994-10-10','Hlavná 10',3,NOW(),NOW()),
      ('Ivan','Bartoš','ivan.bartos@test.sk','0901000011','x',3,'951111/0011','1995-11-11','Hlavná 11',2,NOW(),NOW()),
      ('Pavol','Mikuš','pavol.mikus@test.sk','0901000012','x',3,'961212/0012','1996-12-12','Hlavná 12',2,NOW(),NOW()),
      ('Juraj','Benko','juraj.benko@test.sk','0901000013','x',3,'970101/0013','1997-01-01','Hlavná 13',2,NOW(),NOW()),
      ('Samuel','Polák','samuel.polak@test.sk','0901000014','x',3,'980202/0014','1998-02-02','Hlavná 14',2,NOW(),NOW()),
      ('Adam','Tóth','adam.toth@test.sk','0901000015','x',3,'990303/0015','1999-03-03','Hlavná 15',2,NOW(),NOW());

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

INSERT INTO cn.status (
    name,
    description,
    created_at
) VALUES
      ('FREE', 'Can be mobilizeted, but now no', NOW()),
      ('NOT HEALTHY', 'Can not be mobiblizated because of healthy ', NOW()),
      ('PREPARING', 'He/She is receiving training on poligon', NOW()),
      ('MOBILIZED', 'User has been mobilized', NOW());

INSERT INTO cn.status_history (
    user_id,
    status_id,
    changed_by,
    reason,
    is_current
)
SELECT
    u.id_user,
    s.id_status,
    1,
    'Initial status: FREE',
    true
FROM cn.users u
         JOIN cn.status s
              ON s.name = 'FREE'
WHERE NOT EXISTS (
    SELECT 1
    FROM cn.status_history sh
    WHERE sh.user_id = u.id_user
);


COMMIT;