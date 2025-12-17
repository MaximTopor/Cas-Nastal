package sk.upjs.paz.model;

import lombok.Getter;

@Getter
public class Message {
    private long idMessage;
    private long idSender;
    private long idRecipient;
    private String subject;
    private String text;
    private String dateSent;
    private String message;
    private String updateAt;
    private String lastAction; // ENUM in DB

    public Message(long idSender,
                   long idRecipient,
                   String subject,
                   String text,
                   String dateSent,
                   String message,
                   String lastAction) {

        this.idSender = idSender;
        this.idRecipient = idRecipient;
        this.subject = subject;
        this.text = text;
        this.dateSent = dateSent;
        this.message = message;
        this.lastAction = lastAction;
    }
}


