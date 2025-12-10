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
        return List.of();
    }

    @Override
    public District getById(long id) {
        return null;
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
