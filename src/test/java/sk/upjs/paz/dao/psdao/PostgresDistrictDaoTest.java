package sk.upjs.paz.dao.psdao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.upjs.paz.model.District;
import sk.upjs.paz.testcontainer.TestContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PostgresDistrictDaoTest extends TestContainer {

    private PostgresDistrictDao districtDao;

    @BeforeEach
    void setUp() {
        districtDao = new PostgresDistrictDao(jdbcTemplate);

        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS cn.districts (
                id_district BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                address_of_center VARCHAR(150) NOT NULL,
                kontakt VARCHAR(100) NOT NULL,
                psc INTEGER NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                region VARCHAR(100) NOT NULL
            )
        """);

        jdbcTemplate.execute("TRUNCATE TABLE cn.districts RESTART IDENTITY");
    }

    // =========================
    // GET ALL
    // =========================

    @Test
    void getAll_shouldReturnEmptyList_whenNoData() {
        List<District> list = districtDao.getAll();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void getAll_shouldReturnAllDistrictsSortedByName() {
        insertDistrict("Košice II", "Addr 2", "b@b.sk", 40102, "Košice");
        insertDistrict("Košice I", "Addr 1", "a@a.sk", 40101, "Košice");

        List<District> list = districtDao.getAll();

        assertEquals(2, list.size());
        assertEquals("Košice I", list.get(0).getName());
        assertEquals("Košice II", list.get(1).getName());
    }

    // =========================
    // GET BY ID
    // =========================

    @Test
    void getById_shouldReturnDistrict_whenExists() {
        Long id = insertDistrict("Košice I", "Addr", "a@a.sk", 40101, "Košice");

        District d = districtDao.getById(id);

        assertNotNull(d);
        assertEquals(id, d.getIdDistrict());
        assertEquals("Košice I", d.getName());
    }

    // =========================
    // UPDATE
    // =========================

    @Test
    void update_shouldUpdateAllEditableFields() {
        Long id = insertDistrict("Old", "Old Addr", "old@cn.sk", 11111, "OldReg");

        District d = districtDao.getById(id);
        d.setName("New");
        d.setAddressOfCenter("New Addr");
        d.setContact("new@cn.sk");
        d.setPostalCode(22222);
        d.setRegion("NewReg");

        districtDao.update(d);

        District updated = districtDao.getById(id);

        assertEquals("New", updated.getName());
        assertEquals("New Addr", updated.getAddressOfCenter());
        assertEquals("new@cn.sk", updated.getContact());
        assertEquals(22222, updated.getPostalCode());
        assertEquals("NewReg", updated.getRegion());
    }

    // =========================
    // EXISTS BY NAME
    // =========================

    @Test
    void existsByName_shouldReturnTrue_whenNameExists() {
        insertDistrict("Košice I", "Addr", "a@a.sk", 40101, "Košice");

        assertTrue(districtDao.existsByName("Košice I", -1));
        assertFalse(districtDao.existsByName("Nonexistent", -1));
    }

    @Test
    void existsByName_shouldRespectExcludeId() {
        Long id = insertDistrict("Košice I", "Addr", "a@a.sk", 40101, "Košice");

        assertFalse(districtDao.existsByName("Košice I", id));
        assertTrue(districtDao.existsByName("Košice I", -1));
    }

    // =========================
    // EXISTS BY ADDRESS
    // =========================

    @Test
    void existsByAddress_shouldWorkCorrectly() {
        Long id = insertDistrict("Košice I", "Main 1", "a@a.sk", 40101, "Košice");

        assertTrue(districtDao.existsByAddress("Main 1", -1));
        assertFalse(districtDao.existsByAddress("Main 1", id));
        assertFalse(districtDao.existsByAddress("Unknown", -1));
    }

    // =========================
    // EXISTS BY POSTAL CODE
    // =========================

    @Test
    void existsByPostalCode_shouldWorkCorrectly() {
        Long id = insertDistrict("Košice I", "Addr", "a@a.sk", 40101, "Košice");

        assertTrue(districtDao.existsByPostalCode(40101, -1));
        assertFalse(districtDao.existsByPostalCode(40101, id));
        assertFalse(districtDao.existsByPostalCode(99999, -1));
    }

    // =========================
    // HELPER
    // =========================

    private Long insertDistrict(
            String name,
            String address,
            String contact,
            int psc,
            String region
    ) {
        jdbcTemplate.update("""
            INSERT INTO cn.districts
            (name, address_of_center, kontakt, psc, region)
            VALUES (?, ?, ?, ?, ?)
        """, name, address, contact, psc, region);

        return jdbcTemplate.queryForObject(
                "SELECT id_district FROM cn.districts WHERE name = ?",
                Long.class,
                name
        );
    }
}
