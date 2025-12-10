package sk.upjs.paz.dao;

import sk.upjs.paz.model.StatusHistory;

import java.util.List;

public interface StatusHistoryDao {
    List<StatusHistory> getAll();
    StatusHistory getById(long id);

    List<StatusHistory> getByUser(long userId);
    List<StatusHistory> getByStatus(long statusId);

    void insert(StatusHistory history);
    void update(StatusHistory history);
    void delete(long id);

    StatusHistory getCurrentStatus(long userId);
}