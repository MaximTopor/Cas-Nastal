package sk.upjs.paz.service;

import sk.upjs.paz.dao.Factory;
import sk.upjs.paz.dao.MessageDao;
import sk.upjs.paz.model.Message;
import sk.upjs.paz.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessageService {


    public List<Message> getAllMessages() {
        return messageDao.getAll();
    }


    private final MessageDao messageDao =
            Factory.INSTANCE.getMessageDao();

    public void saveMessage(User sender, String text) {

        System.out.println("SAVE MESSAGE CALLED");

        if (sender == null) {
            System.out.println("SENDER IS NULL");
            return;
        }

        if (sender.getRoleId() != 1) {
            System.out.println("ROLE IS NOT ADMIN: " + sender.getRoleId());
            return;
        }


        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String dateSent = LocalDateTime.now().format(formatter);

        Message message = new Message(
                sender.getIdUser(),   // id_sender
                sender.getIdUser(),   // id_recipient (NOT NULL)
                "ADMIN",              // subject
                text,                 // text
                dateSent,             // date_sent
                text,                 // message (дубль у таблиці)
                "CREATE"              // last_action
        );
        System.out.println("DAO CLASS = " + messageDao.getClass().getName());
        messageDao.insert(message);
    }
}
