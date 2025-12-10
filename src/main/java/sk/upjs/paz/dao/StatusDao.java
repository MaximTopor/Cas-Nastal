package sk.upjs.paz.dao;

import sk.upjs.paz.model.Status;

import java.util.List;

public interface StatusDao {
    List<Status> getAll();
    Status getById(long id);

    void insert(Status status);
    void update(Status status);
    void delete(long id);
}
