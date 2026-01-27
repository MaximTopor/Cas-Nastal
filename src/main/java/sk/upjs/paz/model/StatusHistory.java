package sk.upjs.paz.model;

import lombok.Getter;

@Getter
public class StatusHistory {
    private int idHistory;
    private long userId;
    private long statusId;
    private long changedBy;
    private String changedAt;
    private String reason;
    private boolean isCurrent;
}

