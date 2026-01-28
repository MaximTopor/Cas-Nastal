package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.StatusHistoryDao;
import sk.upjs.paz.model.Status;
import sk.upjs.paz.model.StatusHistory;

import java.util.List;

public class StatusHistoryService {

    private final StatusHistoryDao statusHistoryDao =
            Factory.INSTANCE.getStatusHistoryDao();

    public void changeStatus(long userId,
                             long statusId,
                             long changedBy,
                             String reason) {

        statusHistoryDao.deactivateCurrent(userId);

        statusHistoryDao.insert(userId, statusId, changedBy, reason);
    }

    public long getCurrentStatusId(long userId) {
        return statusHistoryDao.getCurrentStatusId(userId);
    }

    public Status getCurrentStatus(long userId) {
        return statusHistoryDao.getCurrentStatus(userId);
    }

    public List<Status> getAllStatuses() {
        return statusHistoryDao.getAllStatuses();
    }
}
