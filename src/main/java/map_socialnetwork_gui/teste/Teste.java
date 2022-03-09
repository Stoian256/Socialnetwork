package com.lab6.map_socialnetwork_gui.teste;

import com.lab6.map_socialnetwork_gui.business.MessageService;
import com.lab6.map_socialnetwork_gui.business.NetworkService;
import com.lab6.map_socialnetwork_gui.business.UserService;
import com.lab6.map_socialnetwork_gui.domain.*;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.exceptions.ServiceException;
import com.lab6.map_socialnetwork_gui.exceptions.ValidException;
import com.lab6.map_socialnetwork_gui.model.FriendStatus;
import com.lab6.map_socialnetwork_gui.repository.IRepository;
import com.lab6.map_socialnetwork_gui.repository.database.FriendshipDbRepo;
import com.lab6.map_socialnetwork_gui.repository.database.MessageDbRepo;
import com.lab6.map_socialnetwork_gui.repository.database.RequestDbRepo;
import com.lab6.map_socialnetwork_gui.repository.database.UserDbRepo;
import com.lab6.map_socialnetwork_gui.repository.memory.FriendshipRepo;
import com.lab6.map_socialnetwork_gui.repository.memory.UserInMemoryRepo;
import com.lab6.map_socialnetwork_gui.repository.paging.PagingRepository;
import com.lab6.map_socialnetwork_gui.utils.Graph;
import com.lab6.map_socialnetwork_gui.validator.FriendshipValidator;
import com.lab6.map_socialnetwork_gui.validator.IValidator;
import com.lab6.map_socialnetwork_gui.validator.UserValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;
import static com.lab6.map_socialnetwork_gui.utils.Constants.DATE_TIME_FORMATTER;

public class Teste {
    //@org.junit.jupiter.api.Test
    public static void runAllTests() {
        utilizatorTests();
        friendshipTests();
        tupleTests();
        validatorTests();
        repoUtilizatorAddTests();
        serviceUtilizatorAddTests();
        dataBaseTests();
        MessageTests();
        domainTests();
    }

    private static void domainTests() {
        LocalDateTime localDateTime = LocalDateTime.now();
        FriendDto friendDto = new FriendDto(1,"a", "b","c", localDateTime);
        assertTrue(friendDto.getDate().equals(localDateTime));
        assertTrue(friendDto.toString().equals("a" + " | " + "b" + " | " + "c" + " | " + localDateTime.format(DATE_TIME_FORMATTER)));

        User user = new User(1, "a", "b", "wrtrgfs", "123");
        User user1 = new User(1, "aa", "bb", "pqwhre", "123");
        FriendRequest friendRequest = new FriendRequest(1, user, user1, LocalDateTime.now());
        assertTrue(friendRequest.getFirstUser().equals(user));
        assertTrue(friendRequest.getSecondUser().equals(user1));
        assertTrue(friendRequest.getStatus().equals(FriendStatus.PENDING));
        friendRequest.setStatus(FriendStatus.APPROVED);
        assertTrue(friendRequest.getStatus().equals(FriendStatus.APPROVED));
        assertTrue(friendRequest.toString().equals("FriendRequest{" +
                "requestID=" + 1 +
                ", from=" + user +
                ", to=" + user1 +
                ", status=" + FriendStatus.APPROVED +
                '}'));
        Message reply = new Message(1, user, Arrays.asList(user1), "a", localDateTime);
        Message message = new Message(2, user1, Arrays.asList(user), "b", localDateTime, reply);

        assertTrue(message.toString().equals("Reply=" + reply + "Message{" +
                "messageID=" + 2 +
                ", from=" + user1 +
                ", to=" + Arrays.asList(user) +
                ", message='" + "b" + '\'' +
                ", date=" + localDateTime.format(DATE_TIME_FORMATTER) +
                '}' + "\n"));
    }

    private static void serviceUtilizatorAddTests() {
        PagingRepository<User> userRepo = new UserDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetworkTests", "postgres", "postgres");
        IValidator<User> userValidator = new UserValidator();
        IRepository<Friendship> friendshipRepo = new FriendshipDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetworkTests", "postgres", "postgres");
        FriendshipValidator FValidator = new FriendshipValidator();
        Graph network = new Graph();
        UserService userService = new UserService(userRepo, userValidator, network, friendshipRepo);
        IRepository<FriendRequest> requestRepo = new RequestDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetworkTests", "postgres", "postgres");
        NetworkService networkService = new NetworkService(friendshipRepo, network, userRepo, requestRepo,null, FValidator);

        assertEquals(userService.getUsersNumber(), 0);

        String prenume = "2kapa";
        String nume = "Kapa";
        String username = "asd";
        String password = "123";
        try {
            userService.addUser(prenume, nume, username, password);
            assertEquals(userService.getUsersNumber(), 1);
            userService.addUser("Vlad", "Stoian", "qwe", "123");
            userService.addUser("Lucian", "Stoian", "zxc", "123");
            networkService.addFriendship(1, 2);
            assertEquals(networkService.getFriendshipsNumber(), 1);
            assertEquals(networkService.getFriendshipList().size(), 1);
            assertEquals(networkService.NrCommunities(), 2);
            assertEquals(networkService.MostSociable().size(), 2);

            assertTrue(networkService.getFriends(1).get(0).getFirsName().equalsIgnoreCase("Vlad"));
            assertTrue(networkService.getFriends(1).get(0).getLastName().equalsIgnoreCase("Stoian"));

            assertTrue(networkService.getFriendsMonth(1, LocalDate.now().getMonth().getValue()).get(0).getFirsName().equalsIgnoreCase("Vlad"));
            assertTrue(networkService.getFriendsMonth(1, LocalDate.now().getMonth().getValue()).get(0).getLastName().equalsIgnoreCase("Stoian"));
            networkService.removeFriendship(1);
            //networkService/request
            networkService.sendRequest(1, 2);
            assertTrue(requestRepo.getOne(1).getSecondUser().getUserID() == 2);
            assertTrue(requestRepo.size() == 1);
            assertTrue(networkService.getRequestsForUser(2).stream().toList().get(0).getSecondUser().getUserID() == 2);
            networkService.respondToRequest(2, 1, 2);
            assertTrue(networkService.getAllRequests().stream().toList().get(0).getStatus().equals(FriendStatus.REJECTED));
            requestRepo.remove(1);
            assertEquals(networkService.getFriendshipsNumber(), 0);
            assertEquals(userService.getAllUsers().size(), 3);
            assertEquals(userService.getOneUser(1).getUserID(), 1);
            try {
                networkService.sendRequest(1, 2);
                networkService.sendRequest(1, 2);
                fail();
            } catch (ServiceException e) {
                assertEquals(e.getMessage(), "Cerere de prietenie existenta!");
                requestRepo.remove(2);
            }
            userService.removeUser(1);

        } catch (ValidException e) {
            e.printStackTrace();
        } catch (RepoException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        try {
            userService.addUser("", "", "", "");
            fail();
        } catch (ValidException e) {
            assertEquals(e.getMessage(), "Nume invalid!\nPrenume invalid!\nUsername invalid!\nParola invalida!");
        } catch (RepoException e) {
            e.printStackTrace();
        }

        try {
            userService.removeUser(4);
        } catch (RepoException e) {
            assertEquals(e.getMessage(), "Utilizator inexistent!");
        }

        try {
            userService.getOneUser(4);
        } catch (RepoException e) {
            assertEquals(e.getMessage(), "Utilizator inexistent!");
        }

        try {
            userRepo.remove(2);
            userRepo.remove(3);
        } catch (RepoException e) {
            fail();
        }
    }

    private static void repoUtilizatorAddTests() {
        IRepository<User> iRepo = new UserInMemoryRepo();

        assert (iRepo.size() == 0);
        int idUtilizator = 1;
        String prenume = "Silviu";
        String nume = "Stoian";
        String username = "dgdgngfhk";
        String password = "123";

        User utilizator = new User(idUtilizator, nume, prenume, username, password);

        try {
            iRepo.add(utilizator);
            assertEquals(1, iRepo.size());
            User utilizatorGasit = iRepo.getOne(idUtilizator);
            assertEquals(utilizatorGasit, utilizator);
        } catch (RepoException e) {
            fail();
        }
    }

    private static void dataBaseTests() {
        IRepository<User> userRepo = new UserDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetworkTests", "postgres", "postgres");
        IRepository<Friendship> friendshipsRepo = new FriendshipDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetworkTests", "postgres", "postgres");

        assert (userRepo.size() == 0);
        assert (friendshipsRepo.size() == 0);
        int idUtilizator = 1;
        String prenume = "Silviu";
        String nume = "Stoian";
        String username = "gfhkgjhgs";
        String password = "123";

        User utilizator = new User(idUtilizator, prenume, nume, username, password);
        User utilizator1 = new User(2, "Lucian", "Stoian", "hvgcjh", "123");
        User utilizator2 = new User(2, "L", "S", "gjbhbkhjsn", "123");

        try {
            userRepo.add(utilizator);
            userRepo.add(utilizator1);
            assertEquals(2, userRepo.size());
            assertEquals(userRepo.getAll().size(), 2);
            userRepo.update(utilizator2);
            User utilizatorGasit = userRepo.getOne(2);
            assertEquals(utilizatorGasit.getUserFirstName(), "L");
            assertEquals(utilizatorGasit.getUserLastName(), "S");
        } catch (RepoException e) {
            fail();
        }
        Friendship friendship = new Friendship(1, 1, 2, LocalDateTime.now());
        Friendship friendship1 = new Friendship(1, 2, 1, LocalDateTime.now());
        try {
            friendshipsRepo.add(friendship);
            assertEquals(1, friendshipsRepo.size());
            Friendship gasit = friendshipsRepo.getOne(1);
            assertEquals(gasit, friendship);
            assertEquals(friendshipsRepo.getAll().size(), 1);
            friendshipsRepo.update(friendship1);
            gasit = friendshipsRepo.getOne(1);
            assertEquals(gasit.getFirst(), 2);
            assertEquals(gasit.getSecond(), 1);
            friendshipsRepo.remove(1);
            assertEquals(0, friendshipsRepo.size());
            userRepo.remove(1);
            userRepo.remove(2);
            assertEquals(0, userRepo.size());
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }

    private static void validatorTests() {
        PagingRepository<User> userRepo = (PagingRepository<User>) new UserInMemoryRepo();
        IRepository<Friendship> friendshipRepo = new FriendshipRepo();
        IValidator<User> userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        Graph network = new Graph();
        UserService userService = new UserService(userRepo, userValidator, network, friendshipRepo);
        IRepository<FriendRequest> requestRepo = new RequestDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetworkTests", "postgres", "postgres");
        NetworkService networkService = new NetworkService(friendshipRepo, network, userRepo, requestRepo, null,friendshipValidator);
        int idUtilizator = 1;
        String prenume = "Silviu";
        String nume = "Stoian";
        String username = "ouuwei";
        String password = "123";

        User utilizator = new User(idUtilizator, prenume, nume, username, password);
        try {
            userValidator.validate(utilizator);
        } catch (ValidException e) {
            fail();
        }

        utilizator = new User(-1, "", "", "", "");

        try {
            userValidator.validate(utilizator);
            fail();
        } catch (ValidException e) {
            assertEquals("Id invalid!\nNume invalid!\nPrenume invalid!\nUsername invalid!\nParola invalida!", e.getMessage());
        }
        //com.lab6.map_socialnetwork_gui.validator prietenie

        try {
            User u = new User(1, "SS", "SS", "yasdfgh", "123");
            User u1 = new User(2, "SS", "VV", "iohghsoe", "123");
            userService.addUser("SS", "SS", "yasdfgh", "123");
            userService.addUser("SS", "VV", "iohghsoe", "123");
            Friendship friendship = new Friendship(1, 1, 2, null);
            friendshipValidator.validate(friendship, network);
        } catch (ValidException e) {
            fail();
        } catch (RepoException e) {
            e.printStackTrace();
        }


        try {
            Friendship friendship = new Friendship(1, 3, 4, null);
            friendshipValidator.validate(friendship, network);
            fail();
        } catch (ValidException e) {
            assertEquals("Primul utilizator nu exista!\nAl doilea utilizator nu exista!", e.getMessage());
        }
    }

    public static void utilizatorTests() {
        int idUtilizator = 1;
        String prenume = "Silviu";
        String nume = "Stoian";
        String username = "uyyjhg";
        String password = "123";

        User utilizator = new User(idUtilizator, prenume, nume, username, password);
        User utilizator1 = new User(idUtilizator, prenume, nume, username, password);
        assertTrue(utilizator.equals(utilizator1));
        assertTrue(utilizator.hashCode() == utilizator1.hashCode());
        assertTrue(utilizator.toString().equals("User{" +
                "userID=" + idUtilizator +
                ", userFirstName='" + prenume + '\'' +
                ", userLastName='" + nume + '\'' +
                '}'));
        assertEquals(1, utilizator.getUserID());
        assertEquals(utilizator.getUserFirstName(), prenume);
        assertEquals(utilizator.getUserLastName(), nume);


        utilizator.setUserID(2);
        utilizator.setUserFirstName("Vlad");
        utilizator.setUserLastName("SS");
        assertEquals(2, utilizator.getUserID());
        assertEquals(utilizator.getUserFirstName(), "Vlad");
        assertEquals(utilizator.getUserLastName(), "SS");
    }

    public static void tupleTests() {

        int first = 1;
        int second = 2;

        Tuple<Integer, Integer> tuple = new Tuple<>(first, second);
        Tuple<Integer, Integer> tuple1 = new Tuple<>(first, second);
        assertTrue(tuple.equals(tuple1));
        assertTrue(tuple.toString().equals("" + first + "," + second));
        //assertEquals(1, tuple.getLeft());
        //assertEquals(2, tuple.getRight());
        tuple.setLeft(2);
        tuple.setRight(1);
        //assertEquals(2, tuple.getLeft());
        //assertEquals(1, tuple.getRight());
        assertTrue(tuple.hashCode() == Objects.hash(2, 1));

    }


    public static void friendshipTests() {
        int idfr = 1;
        int first = 1;
        int second = 2;
        LocalDateTime date = LocalDateTime.now();

        Friendship fr = new Friendship(idfr, first, second, date);
        Friendship fr1 = new Friendship(idfr + 1, first, second, date);
        assertTrue(fr.equals(fr1));
        assertTrue(fr.toString().equals("Friendship{" +
                "friendshipID=" + idfr +
                ", firstUser=" + first +
                ", secondUser=" + second +
                ", date=" + date.format(DATE_TIME_FORMATTER) +
                '}'));
        assertEquals(1, fr.getFriendshipID());
        assertEquals(1, fr.getFirst());
        assertEquals(2, fr.getSecond());

    }

    public static void MessageTests() {
        IRepository<User> repoUserDb = new UserDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetworkTests", "postgres", "postgres");
        IRepository<Message> messageRepo = new MessageDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetworkTests", "postgres", "postgres");

        MessageService messageService = new MessageService(messageRepo, repoUserDb);
        try {
            User user = new User(1, "a", "b", "yughjsfh", "123");
            User user1 = new User(2, "aa", "bb", "ioohsg", "123");
            repoUserDb.add(user);
            repoUserDb.add(user1);
            List<Integer> list = new ArrayList<>();
            list.add(2);
            messageService.sendMessage(1, list, "Salut");
            assertEquals(messageRepo.getOne(2).getFrom().getUserID(), 1);
            assertTrue(messageRepo.getOne(2).getTo().get(0).getUserID() == 2);
            assertTrue(messageRepo.getOne(2).getMessage().equalsIgnoreCase("Salut"));
            messageRepo.update(new Message(2, null, null, "Buna", LocalDateTime.now(), null));
            assertTrue(messageRepo.getOne(2).getMessage().equalsIgnoreCase("Buna"));
            assertEquals(messageService.getMessagesList().stream().toList().get(0).getMessageID(), 2);
            assertTrue(messageService.getMessagesList().size() == 1);
            messageService.replyMessage(2, 2, "aa");
            assertTrue(messageService.getMessagesList().size() == 2);
            assertEquals(messageRepo.getOne(3).getReply().getMessageID(), 2);
            assertTrue(messageService.getTwoUsersMessages(1, 2).size() == 2);
            messageRepo.remove(3);
            assertTrue(messageRepo.size() == 1);
            messageRepo.remove(2);
            repoUserDb.remove(1);
            repoUserDb.remove(2);
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }
}
