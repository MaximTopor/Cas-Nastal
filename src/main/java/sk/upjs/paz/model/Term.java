package sk.upjs.paz.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class Term {

    private long idTerms;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String address;
    private int capacity;
    private long districtId;

    public Term(
            long idTerms,
            java.time.LocalDate date,
            java.time.LocalTime startTime,
            java.time.LocalTime endTime,
            String address,
            int capacity,
            long districtId
    ) {
        this.idTerms = idTerms;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.capacity = capacity;
        this.districtId = districtId;
    }
}
