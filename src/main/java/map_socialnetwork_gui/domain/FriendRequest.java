package com.lab6.map_socialnetwork_gui.domain;

import com.lab6.map_socialnetwork_gui.model.FriendStatus;

import java.time.LocalDateTime;

public class FriendRequest {
    private int requestID;
    private User firstUser;
    private User secondUser;
    private FriendStatus status;
    private LocalDateTime dateTime;

    public FriendRequest(int requestID, User firstUser, User secondUser, FriendStatus friendStatus, LocalDateTime dateTime) {
        this.requestID = requestID;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.status = friendStatus;
        this.dateTime = dateTime;
    }

    public FriendRequest(int requestID, User firstUser, User secondUser, LocalDateTime dateTime) {
        this.requestID = requestID;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.status = FriendStatus.PENDING;
        this.dateTime = dateTime;
    }

    public int getRequestID() {
        return requestID;
    }

    public User getFirstUser() {
        return firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "requestID=" + requestID +
                ", from=" + firstUser +
                ", to=" + secondUser +
                ", status=" + status +
                '}';
    }
}
