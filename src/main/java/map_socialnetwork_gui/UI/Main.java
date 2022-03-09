package com.lab6.map_socialnetwork_gui.UI;

import com.lab6.map_socialnetwork_gui.business.MessageService;
import com.lab6.map_socialnetwork_gui.business.NetworkService;
import com.lab6.map_socialnetwork_gui.business.UserService;
import com.lab6.map_socialnetwork_gui.domain.FriendRequest;
import com.lab6.map_socialnetwork_gui.domain.Friendship;
import com.lab6.map_socialnetwork_gui.domain.Message;
import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.repository.IRepository;
import com.lab6.map_socialnetwork_gui.repository.database.FriendshipDbRepo;
import com.lab6.map_socialnetwork_gui.repository.database.MessageDbRepo;
import com.lab6.map_socialnetwork_gui.repository.database.RequestDbRepo;
import com.lab6.map_socialnetwork_gui.repository.database.UserDbRepo;
import com.lab6.map_socialnetwork_gui.repository.file.FriendshipFileRepo;
import com.lab6.map_socialnetwork_gui.repository.file.UserFileRepository;
import com.lab6.map_socialnetwork_gui.repository.memory.FriendshipRepo;
import com.lab6.map_socialnetwork_gui.repository.memory.UserInMemoryRepo;
import com.lab6.map_socialnetwork_gui.repository.paging.PagingRepository;
import com.lab6.map_socialnetwork_gui.teste.Teste;
import com.lab6.map_socialnetwork_gui.utils.Graph;
import com.lab6.map_socialnetwork_gui.validator.FriendshipValidator;
import com.lab6.map_socialnetwork_gui.validator.IValidator;
import com.lab6.map_socialnetwork_gui.validator.UserValidator;


public class Main {

    public static void main(String[] args) {
        Teste.runAllTests();
        IRepository<User> userInMemoryRepository = new UserInMemoryRepo();
        IRepository<User> userFileRepository = new UserFileRepository("data/users.csv");
        PagingRepository<User> repoUserDb = new UserDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetwork", "postgres", "postgres");

        IRepository<Friendship> friendshipInMemoryRepo = new FriendshipRepo();
        IRepository<Friendship> friendshipFileRepo = new FriendshipFileRepo("data/friendships.csv");
        IRepository<Friendship> repoFriendshipsDb = new FriendshipDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetwork", "postgres", "postgres");

        IRepository<FriendRequest> requestRepo = new RequestDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetwork", "postgres", "postgres");
        IRepository<Message> messageRepo = new MessageDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetwork", "postgres", "postgres");

        Graph graph = new Graph();
        IValidator<User> userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();

        UserService userService = new UserService(repoUserDb, userValidator, graph, repoFriendshipsDb);
        NetworkService networkService = new NetworkService(repoFriendshipsDb, graph, repoUserDb, requestRepo,null, friendshipValidator);
        MessageService messageService = new MessageService(messageRepo, repoUserDb);

        UI UI = new UI(userService, networkService, messageService);
        UI.run();
    }
}
