package com.lab6.map_socialnetwork_gui.repository.file;

import com.lab6.map_socialnetwork_gui.domain.Friendship;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.repository.memory.FriendshipRepo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class FriendshipFileRepo extends FriendshipRepo {
    private String fileName;

    public FriendshipFileRepo(String fileName) {
        this.fileName = fileName;
        loadData();
    }

    /**
     * Metoda ce citeste utilizatorii din fisier si le adauga in memorie
     */
    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                List<String> attributes = Arrays.asList(line.split(";"));
                Friendship entity = extractEntity(attributes);
                try {
                    super.add(entity);
                } catch (RepoException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Metoda ce scrie in fisier datelel unui utilizator
     *
     * @param entity utilizatorul de adaugat in fisier
     */
    protected void writeToFile(Friendship entity) {
        String line = createEntityAsString(entity);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metoda ce transofrma o list de string-uri intr-un utilizator
     *
     * @param attributes lista de atribute in format string pe baza caruia se creeaza utilizatorul
     * @return utilizatorul contruit pe baza arugumentelor
     */
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship(Integer.parseInt(attributes.get(0)), Integer.parseInt(attributes.get(1)), Integer.parseInt(attributes.get(2)), null);
        return friendship;
    }

    protected String createEntityAsString(Friendship friendship) {
        return friendship.getFriendshipID() + ";" + friendship.getFirst() + ";" + friendship.getSecond();
    }

    @Override
    public void add(Friendship friendship) throws RepoException {
        super.add(friendship);
        writeToFile(friendship);
    }

    /**
     * Metoda ce sterge un utilizator din memorie si fisier
     *
     * @param ID id-ul utilizatorului de sters
     * @throws RepoException daca nu exista un utilizator cu id-ul dat
     */
    @Override
    public void remove(int ID) throws RepoException {
        super.remove(ID);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));) {
            bw.write("");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        for (Friendship friendship : super.getAll())
            writeToFile(friendship);
    }
}
