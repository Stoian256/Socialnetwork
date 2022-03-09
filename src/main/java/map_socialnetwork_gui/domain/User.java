package com.lab6.map_socialnetwork_gui.domain;

import java.util.Objects;


public class User {
    private int userID;
    private String userFirstName;
    private String userLastName;
    private String userUsername;
    private String userPassword;

    /**
     * Constructorul clasei utilizator
     *
     * @param userId        id-ul utilizatorului
     * @param userFirstName prenumele utilizatorului
     * @param userLastName  numele utilizatorului
     * @param userUsername  username-ul utilizatorului
     * @param userPassword  parola utilizatorului
     */
    public User(int userId, String userFirstName, String userLastName, String userUsername, String userPassword) {
        this.userID = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userUsername = userUsername;
        this.userPassword = userPassword;
    }

    /**
     * Metoda ce returneaza id-ul utilizatorului
     *
     * @return id-ul utilizatorului
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Metoda de setter a id-ului utilizatorului
     *
     * @param userID valoarea id-ului de dat utilizatorului
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Metoda ce returneaza prenumele utilizatorului
     *
     * @return prenumele utilizatorului
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * Metoda de setter a prenumelui
     *
     * @param userFirstName valoarea prenumelui de dat utilizatorului
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /**
     * Metoda ce returneaza numele utilizatorului
     *
     * @return numele utilizatorului
     */
    public String getUserLastName() {
        return userLastName;
    }

    /**
     * Metoda de setter a numelui utilizatorului
     *
     * @param userLastName valoarea numelui de dat utilizatorului
     */
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    /**
     * Metoda ce returneaza username-ul utilizatorului
     *
     * @return username-ul utilizatorului
     */
    public String getUserUsername() {
        return userUsername;
    }

    /**
     * Metoda ce seteaza username-ul utilizatorului
     *
     * @param userUsername valoarea username-ului de setat utilizatorului
     */
    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    /**
     * Metoda ce returneaza parola utilizatorului
     *
     * @return parola utilizatorului
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * Metoda ce seteaza parola utilizatorului
     *
     * @param userPassword valoarea parolei de setat utilizatorului
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * Metoda ce returneaa un string cu datele utilizatorului
     *
     * @return un string cu id-ul utilizatorului numele si prenumele lui
     */
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userFirstName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                '}';
    }

    /**
     * Metoda de verificare a egalitatii a doi utilizatori
     *
     * @param o obiectul de verificat a egalitatii cu utilizatorul this
     * @return true daca cei doi utilizatori au aceleasi id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.userID == user.userID || Objects.equals(this.userUsername, user.userUsername);
    }

    /**
     * @return codul hash pe baza id-ului
     */
    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }
}
