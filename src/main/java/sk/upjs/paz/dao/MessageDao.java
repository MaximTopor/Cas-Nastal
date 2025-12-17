package sk.upjs.paz.dao;

import sk.upjs.paz.model.Message;

import java.util.List;

public interface MessageDao {
    List<Message> getAll();
    Message getById(long id);

    List<Message> getBySender(long senderId);
    List<Message> getByRecipient(long recipientId);

    void insert(Message message);
    void update(Message message);
    void delete(long id);

}
