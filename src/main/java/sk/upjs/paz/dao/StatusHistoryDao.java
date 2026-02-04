package sk.upjs.paz.dao;

import sk.upjs.paz.model.Status;
import sk.upjs.paz.model.StatusHistory;

import java.util.List;

public interface StatusHistoryDao {

    void insert(long userId, long statusId, long changedBy, String reason);
    void deactivateCurrent(long id);

    long getCurrentStatusId(long userId);
    Status getCurrentStatus(long userId);
    List<Status> getAllStatuses();
    void changeStatusAtomic(long userId, long statusId, long changedBy, String reason);
}