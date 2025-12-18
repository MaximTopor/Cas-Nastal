package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.DistrictDao;
import sk.upjs.paz.model.District;

import java.util.List;

public class DistrictService {

    private final DistrictDao districtDao = Factory.INSTANCE.getDistrictDao();

    public List<District> getAllDistricts() {
        return districtDao.getAll();
    }

    public District getDistrictById(long districtId) {
        return districtDao.getById(districtId);
    }
}
