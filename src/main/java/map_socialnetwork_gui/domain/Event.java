package com.lab6.map_socialnetwork_gui.domain;

import java.time.LocalDateTime;
import java.util.List;

import static com.lab6.map_socialnetwork_gui.utils.Constants.DATE_TIME_FORMATTER;

public class Event {
        private int eventID;
        private int creatorID;
        private List<Integer> participants;
        private String eventName;
        private String photoURL;
        private LocalDateTime dateTime;

    public Event(int eventID, int creatorID, List<Integer> participants,String eventName, String photoURL, LocalDateTime dateTime) {
        this.eventID = eventID;
        this.creatorID = creatorID;
        this.participants=participants;
        this.eventName = eventName;
        this.photoURL = photoURL;
        this.dateTime = dateTime;
    }


    public List<Integer> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Integer> participants) {
        this.participants = participants;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", creatorID=" + creatorID +
                ", dateTime=" + dateTime +
                '}';
    }
}
