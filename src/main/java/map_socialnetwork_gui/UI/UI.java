package com.lab6.map_socialnetwork_gui.UI;

import com.lab6.map_socialnetwork_gui.business.MessageService;
import com.lab6.map_socialnetwork_gui.business.NetworkService;
import com.lab6.map_socialnetwork_gui.business.UserService;
import com.lab6.map_socialnetwork_gui.domain.FriendDto;
import com.lab6.map_socialnetwork_gui.domain.FriendRequest;
import com.lab6.map_socialnetwork_gui.domain.Friendship;
import com.lab6.map_socialnetwork_gui.domain.Message;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.exceptions.ServiceException;
import com.lab6.map_socialnetwork_gui.exceptions.ValidException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.lab6.map_socialnetwork_gui.utils.Constants.DATE_TIME_FORMATTER;


public class UI {
    private final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
    private final UserService userService;
    private final NetworkService networkService;
    private final MessageService messageService;

    public UI(UserService userService, NetworkService networkService, MessageService messageService) {
        this.userService = userService;
        this.networkService = networkService;
        this.messageService = messageService;
    }

    public void run() {
        int cmd;
        while (true) {
            System.out.println("MENIU:");
            System.out.println(" 0. Exit");
            System.out.println(" 1. AddUser            2. RemoveUser              3. GetAllUsers");
            System.out.println(" 4. AddFriend          5. RemoveFriend            6. GetAllFrienships");
            System.out.println(" 7. ComunitiesNumber   8. MostSociable");
            System.out.println(" 9. GetUserFriends    10. GetUserFriendsByMonth");
            System.out.println("11. SendMessage       12. ReplyMessage");
            System.out.println("13. GetAllMessages    14. GetTwoUsersMessages");
            System.out.println("15. getAllRequests    16. getRequestsForUser");
            System.out.println("17. sendRequest       18. respondToRequest");
            System.out.print("Enter your command: ");

            try {
                cmd = Integer.parseInt(console.readLine());
                System.out.println();

                if (cmd == 0) {
                    System.out.println("Program ended!");
                    break;
                }
                if (cmd < 0 || cmd > 18)
                    System.out.println("Invalid command!");
                if (cmd == 1)
                    addUser();
                if (cmd == 2)
                    removeUser();
                if (cmd == 3)
                    getAllUsers();
                if (cmd == 4)
                    addFriend();
                if (cmd == 5)
                    removeFriend();
                if (cmd == 6)
                    getAllFriendships();
                if (cmd == 7)
                    getComunitiesNumber();
                if (cmd == 8)
                    getMostSociable();
                if (cmd == 9)
                    getFriendsByUserId();
                if (cmd == 10)
                    getFriendsByUserIdAndMonth();
                if (cmd == 11)
                    sendMessage();
                if (cmd == 12)
                    replyMessage();
                if (cmd == 13)
                    getAllMessages();
                if (cmd == 14)
                    getTwoUsersMessages();
                if (cmd == 15)
                    getAllRequests();
                if (cmd == 16)
                    getRequestsForUser();
                if (cmd == 17)
                    sendRequest();
                if (cmd == 18)
                    respondToRequest();
            } catch (NumberFormatException | IOException | RepoException | ServiceException | ValidException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }

    public void addUser() throws IOException, ValidException, RepoException {
        String firstName, lastName;
        System.out.print("Enter user first name: ");
        firstName = console.readLine();
        System.out.print("Enter user last name: ");
        lastName = console.readLine();
        userService.addUser(firstName, lastName, "userUsername", "userPassword");
    }

    public void removeUser() throws IOException, RepoException {
        int ID;
        System.out.print("Enter user ID: ");
        ID = Integer.parseInt(console.readLine());
        userService.removeUser(ID);
    }

    public void getAllUsers() {
        userService.getAllUsers().forEach(System.out::println);
    }

    public void addFriend() throws IOException, ValidException, RepoException {
        int firstUserID, secondUserID;
        System.out.print("Enter first user ID: ");
        firstUserID = Integer.parseInt(console.readLine());
        System.out.print("Enter second user ID: ");
        secondUserID = Integer.parseInt(console.readLine());
        networkService.addFriendship(firstUserID, secondUserID);
    }

    public void removeFriend() throws IOException, RepoException {
        int friendshipID;
        System.out.print("Enter friendship ID: ");
        friendshipID = Integer.parseInt(console.readLine());
        networkService.removeFriendship(friendshipID);
    }

    public void getAllFriendships() throws RepoException {
        for (Friendship friendship : networkService.getFriendshipList()) {
            System.out.println(
                    friendship.getFriendshipID() + " " +
                            userService.getOneUser(friendship.getFirst()) + " " +
                            userService.getOneUser(friendship.getSecond())
            );
        }
    }

    public void getComunitiesNumber() {
        System.out.println(networkService.NrCommunities());
    }

    public void getMostSociable() throws RepoException {
        for (Integer V : networkService.MostSociable()) {
            System.out.println(userService.getOneUser(V));
        }
    }

    private void getFriendsByUserId() throws IOException, RepoException {
        int userID;
        System.out.print("Enter user ID:");
        userID = Integer.parseInt(console.readLine());
        List<FriendDto> friends = networkService.getFriends(userID);
        System.out.println(userService.getOneUser(userID) + " friends:");
        friends.forEach(System.out::println);
    }

    public void getFriendsByUserIdAndMonth() throws IOException, RepoException {
        int userID, month;
        System.out.print("Enter user ID: ");
        userID = Integer.parseInt(console.readLine());
        System.out.print("Enter month (must be int from 1 to 12): ");
        month = Integer.parseInt(console.readLine());
        List<FriendDto> friends = networkService.getFriendsMonth(userID, month);
        System.out.println(userService.getOneUser(userID) + " friends: ");
        friends.forEach(System.out::println);
    }

    public void sendMessage() throws IOException, RepoException {
        int userID;
        String usersIds, message;
        System.out.print("Enter your user ID: ");
        userID = Integer.parseInt(console.readLine());
        System.out.print("Enter the users IDs to receive your message (separated by comma): ");
        usersIds = console.readLine();
        System.out.print("Enter your message: ");
        message = console.readLine();
        List<Integer> IDs = Arrays.stream(usersIds.split(","))
                .map(x -> Integer.parseInt(x.strip()))
                .collect(Collectors.toList());
        if (IDs.contains(userID))
            System.out.println("Nu poti sa iti trimiti mesaj tie!");
        else
            messageService.sendMessage(userID, IDs, message);
    }

    public void replyMessage() throws IOException, RepoException {
        int userID, messageID;
        String message;
        System.out.print("Enter your user ID :");
        userID = Integer.parseInt(console.readLine());
        System.out.print("Enter message id to reply: ");
        messageID = Integer.parseInt(console.readLine());
        System.out.print("Enter your message: ");
        message = console.readLine();
        messageService.replyMessage(userID, messageID, message);
    }

    public void getAllMessages() throws RepoException {
        Collection<Message> messages;
        messages = messageService.getMessagesList();
        if (messages.isEmpty())
            System.out.println("Nu sunt mesaje!");
        else
            messages.forEach(System.out::println);
    }

    public void getTwoUsersMessages() throws IOException, RepoException {
        int firstUser, secondUser;
        System.out.print("Enter first user ID: ");
        firstUser = Integer.parseInt(console.readLine());
        System.out.print("Enter second user ID: ");
        secondUser = Integer.parseInt(console.readLine());

        List<Message> messages = messageService.getTwoUsersMessages(firstUser, secondUser);
        if (messages.isEmpty())
            System.out.println("Nu sunt mesaje intre cei doi utilizatori!");
        else {
            for (Message mes : messages) {
                if (mes.getReply() != null)
                    System.out.print("Reply=" + mes.getReply());
                System.out.print("messsageID=" + mes.getMessageID() + " ");
                System.out.print("From:" + mes.getFrom() + " ");
                System.out.print("To:" + mes.getTo().stream().filter(t -> t.getUserID() == firstUser || t.getUserID() == secondUser).collect(Collectors.toList()).get(0) + " ");
                System.out.println("Message=" + mes.getMessage() + "  Date=" + mes.getDate().format(DATE_TIME_FORMATTER) + "\n");
            }
        }
    }

    public void getAllRequests() throws RepoException {
        Collection<FriendRequest> friendRequests;
        friendRequests = networkService.getAllRequests();
        if (friendRequests.isEmpty())
            System.out.println("Nu sunt cereri de prietenie!");
        else
            friendRequests.forEach(System.out::println);
    }

    public void getRequestsForUser() throws IOException, RepoException {
        int userID;
        System.out.print("Enter user ID: ");
        userID = Integer.parseInt(console.readLine());
        Collection<FriendRequest> friendRequests = networkService.getRequestsForUser(userID);
        if (friendRequests.isEmpty())
            System.out.println("Nu sunt cereri de prietenie!");
        else
            friendRequests.forEach(System.out::println);
    }

    public void sendRequest() throws IOException, ServiceException, RepoException {
        int userID, toUser;
        System.out.print("Enter your user ID: ");
        userID = Integer.parseInt(console.readLine());
        System.out.print("Enter user ID to send request: ");
        toUser = Integer.parseInt(console.readLine());
        networkService.sendRequest(userID, toUser);
    }

    public void respondToRequest() throws IOException, RepoException, ValidException, ServiceException {
        int userID, toUser, status;
        System.out.print("Enter your user ID: ");
        userID = Integer.parseInt(console.readLine());
        System.out.print("Enter user ID to respond: ");
        toUser = Integer.parseInt(console.readLine());
        System.out.print("For APPROVED press 1 / For REJECT press 2: ");
        status = Integer.parseInt(console.readLine());
        networkService.respondToRequest(userID, toUser, status);
    }
}
