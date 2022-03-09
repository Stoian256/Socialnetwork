package com.lab6.map_socialnetwork_gui.business;

import com.lab6.map_socialnetwork_gui.domain.FriendRequest;
import com.lab6.map_socialnetwork_gui.domain.Friendship;
import com.lab6.map_socialnetwork_gui.domain.RequestDTO;
import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.exceptions.ValidException;
import com.lab6.map_socialnetwork_gui.repository.IRepository;
import com.lab6.map_socialnetwork_gui.repository.paging.Page;
import com.lab6.map_socialnetwork_gui.repository.paging.Pageable;
import com.lab6.map_socialnetwork_gui.repository.paging.PageableImplementation;
import com.lab6.map_socialnetwork_gui.repository.paging.PagingRepository;
import com.lab6.map_socialnetwork_gui.utils.Graph;
import com.lab6.map_socialnetwork_gui.validator.IValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class UserService {

    private final IRepository<Friendship> friendshipRepo;
    private final PagingRepository<User> userRepository;
    private final IValidator<User> userValidator;
    private final Graph network;
    private int id;

    /**
     * UserService - constructorul clasei de service pentru utilizatori care initializeaza id-ul userilor pe
     * baza celor existente deja in memoie
     *
     * @param userRepository referinta la repo-ul pentru utilizatori
     * @param userValidator  referinta la validatorul de utilizatori
     * @param network        referinta la un obiect de tipul Graph ce prelucreaza si gestioneaza relatiile dintre utilizatori
     */
    public UserService(PagingRepository<User> userRepository, IValidator<User> userValidator, Graph network, IRepository<Friendship> friendshipRepo) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.friendshipRepo = friendshipRepo;
        this.network = network;

        id = -1;
        Collection<User> users = userRepository.getAll();
        for (User user : users) {
            if (user.getUserID() > id) {
                id = user.getUserID();
                network.addVertex(id);
            }
        }
        id += 2;
    }

    /**
     * Metoda ce returneaza numarul de utilizatori
     *
     * @return numarul utilizatorilor din memorie
     */
    public int getUsersNumber() {
        return userRepository.size();
    }

    /**
     * Metoda ce construeste un utilizator, il valideaza si il adauga in lista de utilizatori si in retea
     *
     * @param firstName prenumele utilizatorului de adaugat
     * @param lastName  numele utilizatorului de adaugat
     * @throws ValidException daca numele este vid sau prenumele este vid
     * @throws RepoException  daca  exist deja un utilizator cu acelasi id
     */
    public void addUser(String firstName, String lastName, String userUsername, String userPassword) throws ValidException, RepoException {
        Collection<User> users = userRepository.getAll();
        for (User user1 : users) {
            if (user1.getUserID() > id) {
                id = user1.getUserID();
            }
        }
        id += 1;
        User user = new User(id, firstName, lastName, userUsername, userPassword);
        id++;
        userValidator.validate(user);
        userRepository.add(user);
        network.addVertex(user.getUserID());
    }

    /**
     * Metoda ce returneaza un utilizator pe baza id-ului furnizat ca parametru
     *
     * @param userID id-ul pe baza caruia se cauta utilizatorul
     * @return utilizatorul cu id-ul egal cu argumentul dat
     * @throws RepoException daca nu exista un utilizator cu id-ul dat
     */
    public User getOneUser(int userID) throws RepoException {
        return userRepository.getOne(userID);
    }

    /**
     * Metoda ce elimina un utilizator din lista de utilizatori si din retea pe baza unui id
     *
     * @param userID id-ul utilizatorului de sters
     * @throws RepoException daca nu exista un utilizator cu id-ul dat
     */
    public void removeUser(int userID) throws RepoException {
        userRepository.remove(userID);
        network.removeVertex(userID);
    }

    private int page = 0;
    private int size = 1;

    private Pageable pageable;

    public void setPageSize(int size) {
        this.size = size;
    }

//    public void setPageable(Pageable pageable) {
//        this.pageable = pageable;
//    }

    public List<User> getNextUsers() {
        this.page++;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.getContent().collect(Collectors.toList());
    }

    public List<User> getUsersOnPage(int page)  {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.getContent().collect(Collectors.toList());
    }


    /**
     * Metoda ce returneaza utilizatorii
     *
     * @return o colectie de utilizatori
     */
    public Collection<User> getAllUsers() {
        return userRepository.getAll();
    }

    /**
     * Metoda ce returneaza un utilizator pe baza username-ului furnizat ca parametru
     *
     * @param username username-ul pe baza caruia se cauta utilizatorul
     * @return utilizatorul cu username-ul egal cu argumentul dat
     */
    public User getUserByUsername(String username) {
        List<User> user = userRepository.getAll().stream()
                .filter(u -> u.getUserUsername().equals(username))
                .collect(Collectors.toList());

        if (user.isEmpty()) return null;
        return user.get(0);
    }
}
