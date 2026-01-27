package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.DistrictDao;
import sk.upjs.paz.model.District;
import sk.upjs.paz.model.User;

import java.util.List;

public class DistrictService {

    private final DistrictDao districtDao = Factory.INSTANCE.getDistrictDao();

    public List<District> getAllDistricts() {
        return districtDao.getAll();
    }

    public District getDistrictById(long districtId) {
        return districtDao.getById(districtId);
    }

    public void updateDistrict(User editor, District district) {

        if (editor == null) {
            throw new SecurityException("Not authenticated");
        }

        if (editor.getRoleId() != 1) {
            throw new SecurityException("No permission");
        }

        if (districtDao.existsByName(district.getName(), district.getIdDistrict())) {
            throw new IllegalArgumentException("NAME_EXISTS");
        }

        if (districtDao.existsByAddress(district.getAddressOfCenter(), district.getIdDistrict())) {
            throw new IllegalArgumentException("ADDRESS_EXISTS");
        }

        if (districtDao.existsByPostalCode(district.getPostalCode(), district.getIdDistrict())) {
            throw new IllegalArgumentException("PSC_EXISTS");
        }

        districtDao.update(district);
    }




}
