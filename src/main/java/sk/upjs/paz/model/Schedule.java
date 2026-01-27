package sk.upjs.paz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Schedule {
    private long idSchedule;
    private String statusOfApplication; // ENUM
    private long userId;
    private long termsId;
}
