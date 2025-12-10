package sk.upjs.paz.dao;

import sk.upjs.paz.model.Schedule;

import java.util.List;

public interface ScheduleDao {
    List<Schedule> getAll();
    Schedule getById(long id);

    List<Schedule> getByUser(long userId);
    List<Schedule> getByTerm(long termId);

    void insert(Schedule schedule);
    void update(Schedule schedule);
    void delete(long id);
}
