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
    public void insert(District district) {

    }

    @Override
    public void update(District district) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<District> findByRegion(String region) {
        return List.of();
    }
}
