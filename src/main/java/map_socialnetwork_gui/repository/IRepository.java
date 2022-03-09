package com.lab6.map_socialnetwork_gui.repository;

import com.lab6.map_socialnetwork_gui.exceptions.RepoException;

import java.sql.SQLException;
import java.util.Collection;

/**
 * Interfata pentru com.lab6.map_socialnetwork_gui.repository cu operatii CRUD
 *
 * @param <T> tipul entitatilor salvate in com.lab6.map_socialnetwork_gui.repository
 */
public interface IRepository<T> {
    /**
     * Metoda ce adauga o entitatea data ca parametru
     *
     * @param t entitatea ce trebuie adaugata
     * @throws RepoException daca exista deja o entitate cu acelasi id
     */
    void add(T t) throws RepoException;

    /**
     * Metoda ce sterge o entidate pe baza id-ului dat ca argument
     *
     * @param ID id-ul entitatii de sters
     * @throws RepoException daca nu exista o entitate cu id-ul dat
     */
    void remove(int ID) throws RepoException;

    /**
     * Metoda ce returneaza numarul de entitati
     *
     * @return numarul de entitati
     */
    int size();

    /**
     * Metoda ce returneaza o entitate pe paza id-ului
     *
     * @param id id-ul entitatii de gasit
     * @return entitate cu id-ul dat
     * @throws RepoException daca nu exista o entitate cu id-ul dat
     */
    T getOne(int id) throws RepoException;

    /**
     * Metoda ce returneaza toate entitatiile
     *
     * @return entitatiile
     */
    Collection<T> getAll();

    /**
     * Modifica entitatea
     *
     * @param t
     */
    void update(T t) throws RepoException;
}
