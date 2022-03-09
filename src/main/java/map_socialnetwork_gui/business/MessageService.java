package com.lab6.map_socialnetwork_gui.business;

import com.lab6.map_socialnetwork_gui.domain.Message;
import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.repository.IRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class MessageService {

    private final IRepository<Message> messageRepo;
    private final IRepository<User> userRepo;
    private int messageId;

    /**
     * MessageService - constructorul clasei de service pentru mesaje care initializeaza id-ul mesajelor cu 1
     *
     * @param messageRepo referinta la repo-ul pentru mesaje
     * @param userRepo    referinta la repo-ul pentru utilizatori
     */
    public MessageService(IRepository<Message> messageRepo, IRepository<User> userRepo) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;

        this.messageId = -1;
        Collection<Message> messages = messageRepo.getAll();
        for (Message message : messages) {
            if (message.getMessageID() > messageId)
                messageId = message.getMessageID();
        }
        messageId += 2;
    }

    public int getMessagesNumber() {
        return messageRepo.size();
    }

    /**
     * Metoda ce adauga un mesaj in repo ul de mesaje pe baza id-ului utiliztorului care trimite mesajul, id-urile utilizatoriilor
     * care primesc mesajul , mesajul propriuzis  si data la care se trimite
     *
     * @param userID   id-ul utilizatorului car etrimite mesajul
     * @param usersIds lista de id-uri ale utilizatorilor care primesc mesajul
     * @param message  mesajul in sine
     * @throws RepoException
     */
    public int sendMessage(int userID, List<Integer> usersIds, String message) throws RepoException {
        Collection<Message> messages = messageRepo.getAll();
        for (Message message1 : messages) {
            if (message1.getMessageID() > messageId)
                messageId = message1.getMessageID();
        }
        messageId+=1;
        User user = userRepo.getOne(userID);
        List<User> users = new ArrayList<>();
        for (Integer id : usersIds)
            users.add(userRepo.getOne(id));
        Message messageObject = new Message(messageId, user, users, message, LocalDateTime.now());
        messageRepo.add(messageObject);
        messageId++;
        return messageId-1;
    }

    /**
     * Metoda prin care utilizatorul cu id ul dat  face reply la un mesaj al carui id se da ca parametru cu mesajul message dat
     * de asemenea ca parametru
     *
     * @param userID  id-ul utilizatorului care face reply
     * @param messId  id-ul mesajului caruia se face reply
     * @param message mesajul in sine
     * @throws RepoException cu mesajul "Nu puteti da reply la un mesaj care nu vi se adreseaza!"
     *                       daca utilizatorul cu id ul dat nu se afla pe lista destinatarilor ai mesajului cu id ul dat
     */
    public int replyMessage(int userID, int messId, String message) throws RepoException {
        User user = userRepo.getOne(userID);
        Message message1 = messageRepo.getOne(messId);
        if (!message1.getTo().contains(user))
            throw new RepoException("Nu puteti da reply la un mesaj care nu vi se adreseaza!");
        List<User> users = new ArrayList<>();
        users.add(message1.getFrom());
        Collection<Message> messages = messageRepo.getAll();
        for (Message messageForID : messages) {
            if (messageForID.getMessageID() > messageId)
                messageId = messageForID.getMessageID();
        }
        messageId+=1;
        Message messageObject = new Message(messageId, user, users, message, LocalDateTime.now(), message1);
        messageRepo.add(messageObject);
        messageId++;
        return messageId-1;
    }

    public void replyAllMessages(int userID, int messId, String message) throws RepoException {
        User user = userRepo.getOne(userID);
        Message message1 = messageRepo.getOne(messId);
        //if (!message1.getTo().contains(user))
        if(false)
            throw new RepoException("Nu puteti da reply la un mesaj care nu vi se adreseaza!");
        List<User> users = new ArrayList<>();
        users.add(message1.getFrom());
        for(User toUser:message1.getTo()){
            if(toUser.getUserID()!=userID)
                users.add(toUser);
        }
        Message messageObject = new Message(messageId, user, users, message, LocalDateTime.now(), message1);
        messageRepo.add(messageObject);
        messageId++;
    }

    /**
     * @return mesajele din memorie
     */
    public Collection<Message> getMessagesList() throws RepoException {
        Collection<Message> messages = messageRepo.getAll();
        for (Message message : messages) {
            message.getFrom().setUserFirstName(userRepo.getOne(message.getFrom().getUserID()).getUserFirstName());
            message.getFrom().setUserLastName(userRepo.getOne(message.getFrom().getUserID()).getUserLastName());
            message.getFrom().setUserUsername(userRepo.getOne(message.getFrom().getUserID()).getUserUsername());
            message.getFrom().setUserPassword(userRepo.getOne(message.getFrom().getUserID()).getUserPassword());
            if (message.getReply() != null && message.getReply().getMessageID() != 0) {
                Message replyMessage = messageRepo.getOne(message.getReply().getMessageID());
                replyMessage.getFrom().setUserFirstName(userRepo.getOne(replyMessage.getFrom().getUserID()).getUserFirstName());
                replyMessage.getFrom().setUserLastName(userRepo.getOne(replyMessage.getFrom().getUserID()).getUserLastName());
                replyMessage.getFrom().setUserUsername(userRepo.getOne(replyMessage.getFrom().getUserID()).getUserUsername());
                replyMessage.getFrom().setUserPassword(userRepo.getOne(replyMessage.getFrom().getUserID()).getUserPassword());
                replyMessage.setReply(null);
                message.setReply(replyMessage);
            }

        }
        return messages;
    }

    /**
     * Returneaza mesajele dintre doi utilizatori cronologic
     *
     * @param firstId  id-ul orimului utilizator angrenat in conversatie
     * @param secondId id-ul celui de al doilea utilizator angrenat in conversatie
     * @return Lista mesajelor dintre cei doi utilizatori
     * @throws RepoException daca nu sunt utilizatori cu id-urile date
     */
    public List<Message> getTwoUsersMessages(int firstId, int secondId) throws RepoException {
        User firstUser = userRepo.getOne(firstId);
        User secondUser = userRepo.getOne(secondId);
        Collection<Message> messages = messageRepo.getAll().stream()
                .filter(t -> (t.getFrom().getUserID() == firstId && t.getTo().contains(secondUser)) || (t.getFrom().getUserID() == secondId && t.getTo().contains(firstUser)))
                .sorted((x, y) -> x.getDate().compareTo(y.getDate()))
                .collect(Collectors.toList());
        for (Message message : messages) {
            message.getFrom().setUserFirstName(userRepo.getOne(message.getFrom().getUserID()).getUserFirstName());
            message.getFrom().setUserLastName(userRepo.getOne(message.getFrom().getUserID()).getUserLastName());
            message.getFrom().setUserUsername(userRepo.getOne(message.getFrom().getUserID()).getUserUsername());
            message.getFrom().setUserPassword(userRepo.getOne(message.getFrom().getUserID()).getUserPassword());
            if (message.getReply() != null && message.getReply().getMessageID() != 0) {
                Message replyMessage = messageRepo.getOne(message.getReply().getMessageID());
                replyMessage.getFrom().setUserFirstName(userRepo.getOne(replyMessage.getFrom().getUserID()).getUserFirstName());
                replyMessage.getFrom().setUserLastName(userRepo.getOne(replyMessage.getFrom().getUserID()).getUserLastName());
                replyMessage.getFrom().setUserUsername(userRepo.getOne(replyMessage.getFrom().getUserID()).getUserUsername());
                replyMessage.getFrom().setUserPassword(userRepo.getOne(replyMessage.getFrom().getUserID()).getUserPassword());
                message.setReply(replyMessage);
            }
        }
        return messages.stream().toList();
    }
}
