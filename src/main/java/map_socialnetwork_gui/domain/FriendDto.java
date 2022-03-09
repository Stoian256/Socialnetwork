package com.lab6.map_socialnetwork_gui.domain;

import java.time.LocalDateTime;

import static com.lab6.map_socialnetwork_gui.utils.Constants.DATE_TIME_FORMATTER;


public class FriendDto {
    private int friendshipID;
    private String lastName;
    private String firsName;
    private String username;
    private LocalDateTime date;

    public FriendDto(int friendshipID,String lastName, String firsName, String username,LocalDateTime date) {
        this.friendshipID=friendshipID;
        this.lastName = lastName;
        this.firsName = firsName;
        this.username = username;
        this.date = date;
    }

    public int getFriendshipID() {
        return friendshipID;
    }

    public void setFriendshipID(int friendshipID) {
        this.friendshipID = friendshipID;
    }

    public String getUsername() { return username;}
    public String getLastName() {
        return lastName;
    }

    public String getFirsName() {
        return firsName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return lastName + " | " + firsName + " | " +username+ " | "+ date.format(DATE_TIME_FORMATTER);
    }
}
