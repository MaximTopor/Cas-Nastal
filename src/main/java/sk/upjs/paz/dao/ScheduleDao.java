package sk.upjs.paz.dao;

import sk.upjs.paz.model.Schedule;

import java.util.List;

public interface ScheduleDao {

    List<Schedule> getByUser(long userId);
    List<Schedule> getByTerm(long termId);

    void insert(long userId, long termId);
    void update(Schedule schedule);
    void delete(long userId, long termId);
    boolean exists(long userId, long termId);

    void cancelTerm(long termId);

}
