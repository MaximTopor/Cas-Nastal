package sk.upjs.paz.dao;

import sk.upjs.paz.model.District;

import java.util.List;

public interface DistrictDao {
    List<District> getAll();
    District getById(long id);

    void insert(District district);
    void update(District district);
    void delete(long id);

    List<District> findByRegion(String region);
}
