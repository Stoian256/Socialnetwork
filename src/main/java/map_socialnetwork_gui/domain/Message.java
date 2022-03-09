package com.lab6.map_socialnetwork_gui.domain;

import java.time.LocalDateTime;
import java.util.List;

import static com.lab6.map_socialnetwork_gui.utils.Constants.DATE_TIME_FORMATTER;


public class Message {
    private int messageID;
    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime date;
    private Message reply;

    public Message(int messageID, User from, List<User> to, String message, LocalDateTime date, Message reply) {
        this.messageID = messageID;
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.reply = reply;
    }

    public Message(int messageID, User from, List<User> to, String message, LocalDateTime date) {
        this.messageID = messageID;
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.reply = null;
    }

    public int getMessageID() {
        return messageID;
    }

    public User getFrom() {
        return from;
    }

    public List<User> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        String string = "";
        if (reply != null)
            string += "Reply=" + reply;
        string += "Message{" +
                "messageID=" + messageID +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date.format(DATE_TIME_FORMATTER) +
                '}' + "\n";
        return string;
    }
}
