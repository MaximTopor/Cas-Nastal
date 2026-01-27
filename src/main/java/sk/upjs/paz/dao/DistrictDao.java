package sk.upjs.paz.dao;

import sk.upjs.paz.model.District;

import java.util.List;

public interface DistrictDao {
    List<District> getAll();
    District getById(long id);
    void update(District district);

    boolean existsByName(String name, long excludeId);
    boolean existsByAddress(String address, long excludeId);
    boolean existsByPostalCode(int psc, long excludeId);
}
