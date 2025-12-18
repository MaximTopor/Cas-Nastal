package sk.upjs.paz.model;

import lombok.Getter;

@Getter
public class District {
    private long idDistrict;
    private String name;
    private String addressOfCenter;
    private String contact;
    private int postalCode;
    private String createdAt;
    private String region;
}
