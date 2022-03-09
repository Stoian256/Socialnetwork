package com.lab6.map_socialnetwork_gui.business;

import com.lab6.map_socialnetwork_gui.domain.*;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.exceptions.ServiceException;
import com.lab6.map_socialnetwork_gui.exceptions.ValidException;
import com.lab6.map_socialnetwork_gui.model.FriendStatus;
import com.lab6.map_socialnetwork_gui.repository.IRepository;
import com.lab6.map_socialnetwork_gui.repository.paging.Page;
import com.lab6.map_socialnetwork_gui.repository.paging.Pageable;
import com.lab6.map_socialnetwork_gui.repository.paging.PageableImplementation;
import com.lab6.map_socialnetwork_gui.repository.paging.PagingRepository;
import com.lab6.map_socialnetwork_gui.utils.Graph;
import com.lab6.map_socialnetwork_gui.utils.Observable;
import com.lab6.map_socialnetwork_gui.validator.FriendshipValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class NetworkService extends Observable {
    private final Graph network;
    private final IRepository<User> userRepo;
    private final IRepository<Friendship> friendshipRepo;
    private final IRepository<FriendRequest> requestRepo;
    private final PagingRepository<Event> eventRepo;
    private final FriendshipValidator friendshipValidator;
    private int friendshipID;
    private int requestID;
    private int eventsID;

    /**
     * NetworkService - constructorul clasei de service pentru reteaua de prietenii care initializeaza id-ul prieteniilor pe
     * baza celor existente deja in memoie
     *
     * @param friendshipRepo      referinta la repo-ul retelei de utilizatori
     * @param network             referinta la un obiect de tipul Graph ce gestioneaza relatiile dintre utilizatori
     * @param userRepo            referinta la repo-ul pentru utilizatori
     * @param requestRepo         referinta la repo-ul pentru cereri de prietenie
     * @param friendshipValidator referinta la validatorul de prietenii
     */
    public NetworkService(IRepository<Friendship> friendshipRepo, Graph network, IRepository<User> userRepo, IRepository<FriendRequest> requestRepo, PagingRepository<Event> eventRepo,FriendshipValidator friendshipValidator) {
        this.friendshipRepo = friendshipRepo;
        this.userRepo = userRepo;
        this.requestRepo = requestRepo;
        this.eventRepo = eventRepo;
        this.friendshipValidator = friendshipValidator;
        this.network = network;

        friendshipID = -1;
        Collection<Friendship> friendships = friendshipRepo.getAll();
        for (Friendship friendship : friendships) {
            if (friendship.getFriendshipID() > friendshipID)
                friendshipID = friendship.getFriendshipID();
            //network.addEdge(friendship.getFirst(), friendship.getSecond());
        }
        friendshipID += 2;

        requestID = -1;
        Collection<FriendRequest> requests = requestRepo.getAll();
        for (FriendRequest request : requests) {
            if (request.getRequestID() > requestID)
                requestID = request.getRequestID();
        }
        requestID += 2;

        eventsID = -1;
        Collection<Event> events = eventRepo.getAll();
        for (Event event : events) {
            if (event.getEventID() > eventsID)
                eventsID = event.getEventID();
        }
        eventsID += 2;
    }

    public void addEvent(int creatorID,String eventName,String photoURL,LocalDateTime dateTime) throws RepoException {
        Collection<Event> events = eventRepo.getAll();
        for (Event event : events) {
            if (event.getEventID() > eventsID)
                eventsID = event.getEventID();
        }
        eventsID += 1;
        Event event = new Event(eventsID,creatorID,new ArrayList<Integer>(),eventName,photoURL, dateTime);
        eventsID += 1;
        eventRepo.add(event);
    }

    public void joinEvent(int eventsID,int participant) throws RepoException {
        Event event =new Event(eventsID,participant,null,"join",null,null);
        eventRepo.update(event);
    }

    public void leaveEvent(int eventsID,int participant) throws RepoException {
        Event event =new Event(eventsID,participant,null,"leave",null,null);
        eventRepo.update(event);
    }

    public void removeEvent(int ID) throws RepoException {
        eventRepo.remove(ID);
    }

    public Event getOneEvent(int ID) throws RepoException {
        return eventRepo.getOne(ID);
    }

    public Collection<Event> getAllEvents() {
        return eventRepo.getAll();
    }
    /**
     * Metoda ce returneaza nurarul prieteniilor din retea
     *
     * @return numarul de prietenii din retea
     */
    public int getFriendshipsNumber() {
        return friendshipRepo.size();
    }

    /**
     * Metoda ce valideaza utilizatorii furnizati, creeaza prietenia si apoi o adauga in retea si in lista de prietenii
     *
     * @param firstUser  id-ul primului utilizator din prietenie
     * @param secondUser id-ul celui de-al doilea utilizator din prietenie
     * @throws ValidException daca nu exista in retea utilizatori ce au id-urile date ca argumente
     * @throws RepoException  daca nu se poate adauga prietenia pentru ca exista exista deja una cu aceasi utilizatori
     */
    public void addFriendship(int firstUser, int secondUser) throws ValidException, RepoException {
        Collection<Friendship> friendships = friendshipRepo.getAll();
        for (Friendship friendship1 : friendships) {
            if (friendship1.getFriendshipID() > friendshipID)
                friendshipID = friendship1.getFriendshipID();
        }
        friendshipID += 1;
        Friendship friendship = new Friendship(friendshipID, firstUser, secondUser, LocalDateTime.now());
        //friendshipValidator.validate(friendship, network);
        friendshipID++;
        friendshipRepo.add(friendship);
        //network.addEdge(firstUser, secondUser);
        notifyObservers();
    }

    /**
     * Metoda ce sterge o prietenie din retea si lista de prieteni pe baza id-ul dat ca parametru
     *
     * @param ID id-ul prieteniei de sters
     * @throws RepoException daca nu exista o prietenie cu id-ul dat
     */
    public void removeFriendship(int ID) throws RepoException {
        //network.removeEdge(friendshipRepo.getOne(ID).getFirst(), friendshipRepo.getOne(ID).getSecond());
        friendshipRepo.remove(ID);
        notifyObservers();
    }

    /**
     * Metoda ce returneaza numarul comunitatiilor din retea
     *
     * @return numarul de comunitati din retea (componentele conexe din graful retelei)
     */
    public int NrCommunities() {
        return network.connected();
    }

    /**
     * Metoda ce returneaza cea mai sociaabila comunitate din retea
     *
     * @return lista cu id-urile utilizatoruilor din componenta conexa cea mai sociabila (cu cel mai lung drum)
     */
    public Set<Integer> MostSociable() {
        return network.maxconnected();
    }

    /**
     * Metoda ce returneaza prieteniile dintre utilizatori
     *
     * @return colectia de prietenii ce se afla in memorie
     */
    public Collection<Friendship> getFriendshipList() {
        return friendshipRepo.getAll();
    }

    /**
     * Metoda ce returneaza prieteniile unui utilizator cu id-ul dat
     *
     * @param userID id-ul utilizatorului pentru care se cauta prietenii
     * @return lista de FriendDto (contine nume, prenume si data) care reprezinta prietenii userului cu id-ul dat
     * @throws RepoException
     */
    public List<FriendDto> getFriends(int userID) throws RepoException {
        return friendshipRepo.getAll().stream()
                .filter(t -> t.getFirst() == userID || t.getSecond() == userID)
                .map(x -> {
                    try {
                        User user;
                        if (x.getFirst() == userID)
                            user = userRepo.getOne(x.getSecond());
                        else
                            user = userRepo.getOne(x.getFirst());

                        return new FriendDto(x.getFriendshipID(),user.getUserLastName(), user.getUserFirstName(),user.getUserUsername(), x.getDate());
                    } catch (RepoException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }


    public List<FriendDto> getFriendsPeriod(int userID,LocalDateTime startDate,LocalDateTime endDate) throws RepoException {
        return friendshipRepo.getAll().stream()
                .filter(t -> (t.getFirst() == userID || t.getSecond() == userID )&& t.getDate() != null && t.getDate().isAfter(startDate) && t.getDate().isBefore(endDate))
                .map(x -> {
                    try {
                        User user;
                        if (x.getFirst() == userID)
                            user = userRepo.getOne(x.getSecond());
                        else
                            user = userRepo.getOne(x.getFirst());

                        return new FriendDto(x.getFriendshipID(),user.getUserLastName(), user.getUserFirstName(),user.getUserUsername(), x.getDate());
                    } catch (RepoException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    /**
     * Metoda ce returneaza prieteniile create intr-o anumita luna ale unui utilizator cu id-ul dat
     *
     * @param userID id-ul utilizatorului pentru care se cauta prietenii
     * @param month  luna in care s-a creat prietenia
     * @return lista de FriendDto (contine nume, prenume si data) care reprezinta prietenii userului cu id-ul dat creati in luna data ca parametru
     * @throws RepoException
     */
    public List<FriendDto> getFriendsMonth(int userID, int month) throws RepoException {
        return friendshipRepo.getAll().stream()
                .filter(t -> (t.getFirst() == userID || t.getSecond() == userID) && t.getDate() != null && t.getDate().getMonthValue() == month)
                .map(x -> {
                    try {
                        User user;
                        if (x.getFirst() == userID)
                            user = userRepo.getOne(x.getSecond());
                        else
                            user = userRepo.getOne(x.getFirst());
                        return new FriendDto(x.getFriendshipID(),user.getUserLastName(), user.getUserFirstName(),user.getUserUsername(), x.getDate());
                    } catch (RepoException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    /**
     * Metoda ce returneaza toate cererile de prietenie ale unui user cu id-ul dat
     *
     * @param userID id-ul userului
     * @return cererile de prietenie ale utilizatorului cu id-ul dat
     */
    public List<FriendRequest> getRequestsForUser(int userID) throws RepoException {
        List<FriendRequest> friendRequests = requestRepo.getAll().stream()
                .filter(t -> t.getSecondUser().getUserID() == userID && t.getStatus() == FriendStatus.PENDING)
                .collect(Collectors.toList());
        User first_user;
        User second_user;
        for (FriendRequest request : friendRequests) {
            first_user = userRepo.getOne(request.getFirstUser().getUserID());
            second_user = userRepo.getOne(request.getSecondUser().getUserID());
            request.getFirstUser().setUserFirstName(first_user.getUserFirstName());
            request.getFirstUser().setUserLastName(first_user.getUserLastName());
            request.getFirstUser().setUserUsername(first_user.getUserUsername());
            request.getFirstUser().setUserPassword(first_user.getUserPassword());
            request.getSecondUser().setUserFirstName(second_user.getUserFirstName());
            request.getSecondUser().setUserLastName(second_user.getUserLastName());
            request.getSecondUser().setUserUsername(second_user.getUserUsername());
            request.getSecondUser().setUserPassword(second_user.getUserPassword());
        }
        return friendRequests;
    }

    private RequestDTO createRequestDTO(FriendRequest request, User user) {
        int requestID = request.getRequestID();
        int userID = user.getUserID();
        String firstName = user.getUserFirstName();
        String lastName = user.getUserLastName();
        LocalDateTime dateTime = request.getDateTime();
        FriendStatus status = request.getStatus();
        return new RequestDTO(requestID, userID, firstName, lastName, dateTime, status);
    }

    private List<RequestDTO> requestsToRequestsDTO(List<FriendRequest> friendRequests) throws RepoException {
        List<RequestDTO> requestDTOS = new ArrayList<RequestDTO>();
        for (FriendRequest request : friendRequests) {
            User firstUser = userRepo.getOne(request.getFirstUser().getUserID());
            RequestDTO requestDTO = createRequestDTO(request, firstUser);
            requestDTOS.add(requestDTO);
        }
        return requestDTOS;
    }

    private List<RequestDTO> requestsToSentRequestsDTO(List<FriendRequest> friendRequests) throws RepoException {
        List<RequestDTO> requestDTOS = new ArrayList<RequestDTO>();
        for (FriendRequest request : friendRequests) {
            User secondUser = userRepo.getOne(request.getSecondUser().getUserID());
            RequestDTO requestDTO = createRequestDTO(request, secondUser);
            requestDTOS.add(requestDTO);
        }
        return requestDTOS;
    }

    /**
     * Metoda ce returneaza toate cererile de prietenie ale unui user cu id-ul dat
     *
     * @param userID id-ul userului
     * @return cererile de prietenie ale utilizatorului cu id-ul dat
     */
    public List<RequestDTO> getRequestsForUserDTO(int userID) throws RepoException {
        List<FriendRequest> friendRequests = requestRepo.getAll().stream()
                .filter(t -> t.getSecondUser().getUserID() == userID && t.getStatus() == FriendStatus.PENDING)
                .collect(Collectors.toList());
        return requestsToRequestsDTO(friendRequests);
    }

    /**
     * Metoda ce returneaza toate cererile de prietenie ale unui user cu id-ul dat
     *
     * @param userID id-ul userului
     * @return cererile de prietenie ale utilizatorului cu id-ul dat
     */
    public List<RequestDTO> getSentRequestsForUserDTO(int userID) throws RepoException {
        List<FriendRequest> friendRequests = requestRepo.getAll().stream()
                .filter(t -> t.getFirstUser().getUserID() == userID)
                .collect(Collectors.toList());
        return requestsToSentRequestsDTO(friendRequests);
    }
    /**
     * Metoda ce returneaza toate cererile de prietenie
     *
     * @return toate cererile de prietenie
     * @throws RepoException
     */
    public Collection<FriendRequest> getAllRequests() throws RepoException {
        Collection<FriendRequest> friendRequests = requestRepo.getAll();
        User first_user;
        User second_user;
        for (FriendRequest request : friendRequests) {
            first_user = userRepo.getOne(request.getFirstUser().getUserID());
            second_user = userRepo.getOne(request.getSecondUser().getUserID());
            request.getFirstUser().setUserFirstName(first_user.getUserFirstName());
            request.getFirstUser().setUserLastName(first_user.getUserLastName());
            request.getFirstUser().setUserUsername(first_user.getUserUsername());
            request.getFirstUser().setUserPassword(first_user.getUserPassword());
            request.getSecondUser().setUserFirstName(second_user.getUserFirstName());
            request.getSecondUser().setUserLastName(second_user.getUserLastName());
            request.getSecondUser().setUserUsername(second_user.getUserUsername());
            request.getSecondUser().setUserPassword(second_user.getUserPassword());
        }
        return friendRequests;
    }

    /**
     * Metoda care trimite o cerere de prietenie de la utilizatorul cu id-ul firstUser la utilizatorul cu id-ul secondUser
     *
     * @param firstUser  id-ul utilizatorului care trimite cererea
     * @param secondUser id-ul utilizatorului caruia i se adreseaza cererea
     * @throws ServiceException daca utilizatorii sunt deja prieteni
     * @throws RepoException    daca utilizatorii nu exista
     */
    public void sendRequest(int firstUser, int secondUser) throws ServiceException, RepoException {
        Collection<FriendRequest> requests = requestRepo.getAll();
        if (friendshipRepo.getAll().stream()
                .filter(t ->( t.getFirst() == firstUser && t.getSecond() == secondUser)  ||
                        (t.getFirst() == secondUser && t.getSecond() == firstUser))
                .count() != 0)
            throw new ServiceException("Sunteti deja prieteni!");
        for (FriendRequest request : requests) {
            if (request.getRequestID() > requestID)
                requestID = request.getRequestID();
        }
        requestID += 1;
        User user1 = userRepo.getOne(firstUser);
        User user2 = userRepo.getOne(secondUser);
        /*if (network.getAdjVertices(firstUser).contains(secondUser) || network.getAdjVertices(secondUser).contains(firstUser))
            throw new ServiceException("Sunteti deja prieteni!");*/
        FriendRequest friendRequest = new FriendRequest(requestID, user1, user2, LocalDateTime.now());
        if (requestRepo.getAll().stream()
                .filter(t -> t.getStatus() == FriendStatus.PENDING && (t.getFirstUser().getUserID() == firstUser && t.getSecondUser().getUserID() == secondUser ||
                                                                      t.getFirstUser().getUserID() == secondUser && t.getSecondUser().getUserID() == firstUser))
                .count() != 0)
            throw new ServiceException("Cerere de prietenie existenta!");
        requestRepo.add(friendRequest);
        notifyObservers();
        requestID++;
    }

    /**
     * Metoda prin care utilizatorul cu id-ul firstUser raspunde cererii de prietenie primita de la utilizatorul cu id-ul secondUser
     *
     * @param firstUser  id-ul utilizatorului care raspunde la cerere
     * @param secondUser id-ul utilizatorului care a trimis cererea
     * @param status     1 - APPROVED, 2 - REJECTED
     */
    public void respondToRequest(int firstUser, int secondUser, int status) throws RepoException, ValidException, ServiceException {
        List<FriendRequest> friendRequests = requestRepo.getAll().stream()
                .filter(t -> t.getFirstUser().getUserID() == secondUser && t.getSecondUser().getUserID() == firstUser && t.getStatus() == FriendStatus.PENDING)
                .collect(Collectors.toList());
        if (friendRequests.isEmpty())
            throw new ServiceException("Cererea nu exista!");
        if (status == 1) {
            friendRequests.get(0).setStatus(FriendStatus.APPROVED);
            addFriendship(firstUser, secondUser);
        }
        if (status == 2)
            friendRequests.get(0).setStatus(FriendStatus.REJECTED);

        requestRepo.update(friendRequests.get(0));
        notifyObservers();
    }

    public void removeRequest(int requestID) throws RepoException {
        requestRepo.remove(requestID);
        notifyObservers();
    }


    private int page = 0;
    private int size = 1;

    private Pageable pageable;

    public void setPageSize(int size) {
        this.size = size;
    }

    public List<Event> getNextEvents() {
        this.page++;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<Event> eventPage = eventRepo.findAll(pageable);
        return eventPage.getContent().collect(Collectors.toList());
    }

    public List<Event> getEventsOnPage(int page)  {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        Page<Event> eventPage = eventRepo.findAll(pageable);
        return eventPage.getContent().collect(Collectors.toList());
    }
}
