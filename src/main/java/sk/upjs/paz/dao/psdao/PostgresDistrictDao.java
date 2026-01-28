package sk.upjs.paz.dao.psdao;

import org.springframework.jdbc.core.JdbcOperations;
import sk.upjs.paz.dao.DistrictDao;
import sk.upjs.paz.model.District;

import java.util.List;

public class PostgresDistrictDao implements DistrictDao {

    private final JdbcOperations jdbc;

    public PostgresDistrictDao(JdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<District> getAll() {

        String sql = """
            SELECT *
            FROM cn.districts
            ORDER BY name
        """;

        return jdbc.query(sql, (rs, rowNum) ->
                new District(
                        rs.getLong("id_district"),
                        rs.getString("name"),
                        rs.getString("address_of_center"),
                        rs.getString("kontakt"),
                        rs.getInt("psc"),
                        rs.getObject("created_at", java.time.LocalDateTime.class),
                        rs.getString("region")
                )
        );
    }

    @Override
    public District getById(long id) {

        String sql = """
        SELECT *
        FROM cn.districts
        WHERE id_district = ?
    """;

        return jdbc.queryForObject(
                sql,
                (rs, rowNum) -> new District(
                        rs.getLong("id_district"),
                        rs.getString("name"),
                        rs.getString("address_of_center"),
                        rs.getString("kontakt"),
                        rs.getInt("psc"),
                        rs.getObject("created_at", java.time.LocalDateTime.class),
                        rs.getString("region")
                ),
                id
        );
    }

    @Override
    public void update(District district) {

        String sql = """
        UPDATE cn.districts
        SET name = ?,
            address_of_center = ?,
            kontakt = ?,
            psc = ?,
            region = ?
        WHERE id_district = ?
    """;

        jdbc.update(
                sql,
                district.getName(),
                district.getAddressOfCenter(),
                district.getContact(),
                district.getPostalCode(),
                district.getRegion(),
                district.getIdDistrict()
        );
    }

    @Override
    public boolean existsByName(String name, long excludeId) {
        String sql = """
        SELECT COUNT(*)
        FROM cn.districts
        WHERE name = ?
          AND id_district <> ?
    """;

        Integer count = jdbc.queryForObject(sql, Integer.class, name, excludeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByAddress(String address, long excludeId) {
        String sql = """
        SELECT COUNT(*)
        FROM cn.districts
        WHERE address_of_center = ?
          AND id_district <> ?
    """;

        Integer count = jdbc.queryForObject(sql, Integer.class, address, excludeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByPostalCode(int psc, long excludeId) {
        String sql = """
        SELECT COUNT(*)
        FROM cn.districts
        WHERE psc = ?
          AND id_district <> ?
    """;

        Integer count = jdbc.queryForObject(sql, Integer.class, psc, excludeId);
        return count != null && count > 0;
    }

}
