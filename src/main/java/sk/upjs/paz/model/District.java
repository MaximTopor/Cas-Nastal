package sk.upjs.paz.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class District {
    private long idDistrict;
    private String name;
    private String addressOfCenter;
    private String contact;
    private int postalCode;
    private LocalDateTime createdAt;
    private String region;

    public District(
            long idDistrict,
            String name,
            String addressOfCenter,
            String contact,
            int postalCode,
            LocalDateTime createdAt,
            String region
    ) {
        this.idDistrict = idDistrict;
        this.name = name;
        this.addressOfCenter = addressOfCenter;
        this.contact = contact;
        this.postalCode = postalCode;
        this.createdAt = createdAt;
        this.region = region;
    }

    @Override
    public String toString() {
        return name;
    }
}
