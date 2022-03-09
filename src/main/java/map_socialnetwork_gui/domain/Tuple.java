package com.lab6.map_socialnetwork_gui.domain;

import java.util.Objects;


/**
 * Define a Tuple o generic type entities
 * @param <E1> - tuple first entity type
 * @param <E2> - tuple second entity type
 */
public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    /**
     * Constructorul pentru tuplu
     *
     * @param e1 primul element din tuplu
     * @param e2 al doilea element din tuplu
     */
    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * Metoda ce returneaza primul element din tuplu
     *
     * @return primul element din tuplu
     */
    public E1 getLeft() {
        return e1;
    }

    /**
     * Metoda de setter a primul element din tuplu
     *
     * @param e1 -valoarea de dat pentru primul element din tuplu
     */
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    /**
     * Metoda ce returneaza al doilea element din tuplu
     *
     * @return al doilea element din tuplu
     */
    public E2 getRight() {
        return e2;
    }

    /**
     * Metoda de setter a celui de-al doilea element din tuplu
     *
     * @param e2 -valoarea de dat pentru al doilea element din tuplu
     */
    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    /**
     * Metoda ce returneaa un string cu elementele din tuplu
     *
     * @return elementele din tuplu despartite prin virgula
     */
    @Override
    public String toString() {
        return "" + e1 + "," + e2;

    }

    /**
     * Metoda de verificare a egalitatii a doua tupluri
     *
     * @param obj obiectul de verificat a egalitatii cu tuplul this
     * @return true daca cele doua tupluri au aceleasi elemente indiferent de pozitia lor in tuplu
     */
    @Override
    public boolean equals(Object obj) {
        return (this.e1.equals(((Tuple) obj).e1) && this.e2.equals(((Tuple) obj).e2)) || (this.e1.equals(((Tuple) obj).e2) && this.e2.equals(((Tuple) obj).e1));
    }

    /**
     * @return codul hash pe baza celor doua elemente
     */
    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}
