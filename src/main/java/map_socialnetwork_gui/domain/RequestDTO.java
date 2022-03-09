package com.lab6.map_socialnetwork_gui.domain;

import com.lab6.map_socialnetwork_gui.model.FriendStatus;

import java.time.LocalDateTime;

public class RequestDTO {

    private int requestID;
    private int userID;
    private String firstName;
    private String lastName;
    private LocalDateTime date;

    private FriendStatus status;

    public RequestDTO(int requestID, int userID, String firstName, String lastName, LocalDateTime date, FriendStatus status) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.status = status;
        this.requestID = requestID;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }
}
