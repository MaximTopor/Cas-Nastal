package sk.upjs.paz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class District {
    private long idDistrict;
    private String name;
    private String addressOfCenter;
    private String contact;
    private int postalCode;
    private LocalDateTime createdAt;
    private String region;

    @Override
    public String toString() {
        return name;
    }
}