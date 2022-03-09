package com.lab6.map_socialnetwork_gui.utils;

import com.lab6.map_socialnetwork_gui.domain.Tuple;

import java.util.*;


public class Graph {
    private Map<Integer, List<Integer>> adjVertices;

    public Graph() {
        this.adjVertices = new HashMap<>();
    }

    public Set<Integer> getAdj() {
        return adjVertices.keySet();
    }

    /**
     * Adauga un nod in graf
     *
     * @param vertex nodul de adaugat
     */
    public void addVertex(Integer vertex) {
        adjVertices.putIfAbsent(vertex, new ArrayList<>());
    }

    /**
     * Elimina un nod din graf
     *
     * @param vertex nodul de eliminat
     */
    public void removeVertex(Integer vertex) {
        if (adjVertices.containsKey(vertex))
            adjVertices.remove(vertex);
    }

    /**
     * returneaza lista de noduri adiacente nodului dat
     *
     * @param id
     * @return
     */
    public List<Integer> getAdjVertices(int id) {
        return adjVertices.get(id);
    }

    /**
     * metoda ce adauga legatura dintre cele doua noduri
     *
     * @param vertex1 primul nod
     * @param vertex2 al doilea nod
     */
    public void addEdge(Integer vertex1, Integer vertex2) {
        if (!adjVertices.get(vertex1).contains(vertex2))
            adjVertices.get(vertex1).add(vertex2);
        if (!adjVertices.get(vertex2).contains(vertex1))
            adjVertices.get(vertex2).add(vertex1);
    }

    /**
     * Metoda ce elimina legatura dintre cele doua noduril
     *
     * @param vertex1 primuul nod
     * @param vertex2 al doilea nod
     */
    public void removeEdge(Integer vertex1, Integer vertex2) {
        List<Integer> eV1 = adjVertices.get(vertex1);
        List<Integer> eV2 = adjVertices.get(vertex2);
        if (vertex1 != null)
            eV1.remove(vertex2);
        if (eV2 != null)
            eV2.remove(vertex1);
    }

    /**
     * Metoda ce calculeaza toate nodurile vizitate plecand din nodul root
     *
     * @param root nodul de plecare
     * @return toate nodurile vizitate plecand din nodul root
     */
    public Set<Integer> depthFirstTraversal(Integer root) {
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Integer vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (Integer v : adjVertices.get(vertex)) {
                    stack.push(v);
                }
            }
        }
        return visited;
    }

    /**
     * Metoda care parcurge toate componentele din graf si le calculeaza lugimea drumului maxim
     *
     * @param root - radacina grafului din vcare se pleaca
     * @return un tuplu ce contine lunimea pazima si componenta in care s-a gasit
     */
    public Tuple<Integer, Set<Integer>> DFS(Integer root) {
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(root);
        int max = -1, lungime = -1;
        while (!stack.isEmpty()) {
            Integer vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                //System.out.println(vertex);
                lungime++;
                System.out.println(lungime);
                for (Integer v : adjVertices.get(vertex)) {
                    stack.push(v);
                }
            } else {
                if (lungime > max)
                    max = lungime;
                lungime = -1;
            }
            // System.out.println(vertex+"  "+max);
        }
        return new Tuple<Integer, Set<Integer>>(max, visited);
    }

    public Tuple<Integer, Set<Integer>> DFS1(Integer root) {
        Set<Tuple<Integer, Integer>> visited = new HashSet<>();
        Set<Integer> vvisited = new HashSet<>();
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(root);
        vvisited.add(root);
        int max = -1, lungime = 0;
        //System.out.println(root+" "+lungime);
        while (!stack.isEmpty()) {
            Integer vertex = stack.pop();
            int ok = 0;
            for (Integer v : adjVertices.get(vertex)) {
                if (!visited.contains(new Tuple<Integer, Integer>(vertex, v)) && !visited.contains(new Tuple<Integer, Integer>(v, vertex))) {
                    stack.push(v);
                    visited.add(new Tuple<Integer, Integer>(vertex, v));
                    vvisited.add(v);
                    lungime++;
                    // System.out.println(vertex+"  "+v+"  "+lungime);

                }
            }
            //lungime++;
            if (lungime > max)
                max = lungime;
            //lungime=-1;

            //System.out.println(vertex+"  "+lungime);
        }
        return new Tuple<Integer, Set<Integer>>(max, vvisited);
    }

    /**
     * Metoda ce returneaza numarul comunitatiilor din retea
     *
     * @return numarul de componente conexe din graf
     */
    public int connected() {
        int count = 0;
        Map<Integer, Boolean> vizitate = new HashMap<>();

        for (Integer user : adjVertices.keySet())
            vizitate.putIfAbsent(user, false);

        for (Integer user : adjVertices.keySet()) {
            Set<Integer> gaseste;
            if (vizitate.containsKey(user) && vizitate.get(user) == false) {
                gaseste = depthFirstTraversal(user);
                for (Integer prieten : gaseste)
                    vizitate.replace(prieten, true);
                count++;
            }
        }
        return count;
    }

    /**
     * Metoda ce returneaza cea mai sociaabila comunitate din retea
     *
     * @return componenta in care s-a gasit cel mai lung drum
     */
    public Set<Integer> maxconnected() {
        Set<Integer> maxsociabila = new HashSet<>();
        Map<Integer, Boolean> vizitate = new HashMap<>();
        Tuple<Integer, Set<Integer>> maxGaseste;
        int Max = 0;
        for (Integer user : adjVertices.keySet())
            vizitate.putIfAbsent(user, false);

        for (Integer user : adjVertices.keySet()) {
            Set<Integer> gaseste;
            if (vizitate.containsKey(user) && vizitate.get(user) == false) {
                //System.out.println("AICI\n");
                maxGaseste = DFS1(user);
                gaseste = maxGaseste.getRight();
                //System.out.println(maxGaseste.getLeft());
                if (maxGaseste.getLeft() > Max) {
                    maxsociabila.clear();
                    for (Integer e : gaseste)
                        maxsociabila.add(e);
                    Max = maxGaseste.getLeft();
                }
                for (Integer prieten : gaseste)
                    vizitate.replace(prieten, true);
            }
        }
        return maxsociabila;
    }
}
