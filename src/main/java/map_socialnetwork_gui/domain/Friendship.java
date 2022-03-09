package com.lab6.map_socialnetwork_gui.domain;

import java.time.LocalDateTime;

import static com.lab6.map_socialnetwork_gui.utils.Constants.DATE_TIME_FORMATTER;

public class Friendship {
    private int friendshipID;
    private int first;
    private int second;
    private LocalDateTime date;

    /**
     * Constructorul clasei prietenie
     *
     * @param friendshipID id-ul prietenie
     * @param first        id-ul primului utilizator din relatia de prietenie
     * @param second       id-ul celui de al doilea utilizator din relatia de prietenie
     */
    public Friendship(int friendshipID, int first, int second, LocalDateTime date) {
        this.friendshipID = friendshipID;
        this.first = first;
        this.second = second;
        this.date = date;
    }

    /**
     * Metoda ce defineste egalitatea dintre 2 prietenii
     *
     * @param o un obiect pentru care se face verificarea de egalitate cu o prietenie
     * @return true - daca o se poate aduce la o forma de prietenie si are id-ul primului utilizator egal cu id-ul primului utilizator
     * al prieteniei si id-ul celor de ai doilea utilizatori  egali sau id-ul primului egal cu cel de al doilea si al doilea cu primul
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return (first == that.first && second == that.second) || (first == that.second && second == that.first);
    }


    /**
     * Metoda ce returneaza id-ul unei prietenii
     *
     * @return id-ul prieteniei
     */
    public int getFriendshipID() {
        return friendshipID;
    }

    /**
     * Metoda ce returneaza id-ul primului utilizator din relatia de prietenie
     *
     * @return id-ul primului utilizator din relatia de prietenie
     */
    public int getFirst() {
        return first;
    }

    /**
     * Metoda ce returneaza id-ul celui de al doilea utilizator din relatia de prietenie
     *
     * @return id-ul celui de al doilea utilizator din relatia de prietenie
     */
    public int getSecond() {
        return second;
    }

    /**
     * Metoda ce returneaza data in care s-a format relatia de prietenie
     *
     * @return data in care s-a format relatia de prietenie
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Metoda ce returneaza prietenie sub forma unui string
     *
     * @return un string ce cuprinde id-ul prieteniei, id-urile celoi doi utilizatori din prietenie si data in care s-a format prietenia
     */
    @Override
    public String toString() {
        return "Friendship{" +
                "friendshipID=" + friendshipID +
                ", firstUser=" + first +
                ", secondUser=" + second +
                ", date=" + date.format(DATE_TIME_FORMATTER) +
                '}';
    }
}
