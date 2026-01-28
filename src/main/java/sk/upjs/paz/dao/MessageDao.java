package sk.upjs.paz.dao;

import sk.upjs.paz.model.Message;

import java.util.List;

public interface MessageDao {
    List<Message> getAll();
    void insert(Message message);
}
