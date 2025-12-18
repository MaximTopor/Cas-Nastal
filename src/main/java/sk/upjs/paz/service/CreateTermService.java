package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.TermDao;
import sk.upjs.paz.model.Term;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateTermService {

    private final TermDao termDao = Factory.INSTANCE.getTermDao();

    /* ================= CREATE ================= */

    public void createTerm(
            String type,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            String address,
            int capacity,
            long districtId
    ) {

        validate(type, date, startTime, endTime, address, capacity);

        Term term = new Term(
                0,
                type,
                date,
                startTime,
                endTime,
                address,
                capacity,
                districtId
        );

        termDao.create(term);
    }

    /* ================= EDIT ================= */

    public void updateTerm(
            long termId,
            String type,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            String address,
            int capacity,
            long districtId
    ) {
        validate(type, date, startTime, endTime, address, capacity);

        Term term = new Term(
                termId,
                type,
                date,
                startTime,
                endTime,
                address,
                capacity,
                districtId
        );

        termDao.update(term);
    }

    /* ================= VALIDATION ================= */

    private void validate(
            String type,
            LocalDate date,
            LocalTime start,
            LocalTime end,
            String address,
            int capacity
    ) {

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Type is required");
        }

        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date must be today or in future");
        }

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end time are required");
        }

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
    }
}
