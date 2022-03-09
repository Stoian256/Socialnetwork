package com.lab6.map_socialnetwork_gui.repository.file;

import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.repository.memory.UserInMemoryRepo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class UserFileRepository extends UserInMemoryRepo {
    String fileName;

    /**
     * Consturctor com.lab6.map_socialnetwork_gui.repository pentru utilizatori cu memorarea in fisier
     *
     * @param fileName calea catre fisierul cu utiliatori
     */
    public UserFileRepository(String fileName) {
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
                User entity = extractEntity(attributes);
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
    protected void writeToFile(User entity) {
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
    public User extractEntity(List<String> attributes) {
        User user = new User(Integer.parseInt(attributes.get(0)), attributes.get(1), attributes.get(2), "userUsername", "userPassword");
        return user;
    }

    /**
     * Metoda ce creeaza un string cu datale utilizatorului dat ca argument
     *
     * @param user utilizatorul cu datele caruia se creeaza string-ul
     * @return un string cu id-ul, numle si prenumele utilizatorului
     */
    protected String createEntityAsString(User user) {
        return user.getUserID() + ";" + user.getUserFirstName() + ";" + user.getUserLastName();
    }

    /**
     * Metoda ce adauga un utilizator un memorie si in fisier
     *
     * @param user utilizatorul de adaugat
     * @throws RepoException daca exista deja un utilizator cu acelasi id
     */
    @Override
    public void add(User user) throws RepoException {
        super.add(user);
        writeToFile(user);
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
        for (User user : super.getAll())
            writeToFile(user);
    }
}
