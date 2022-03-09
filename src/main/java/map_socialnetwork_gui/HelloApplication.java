package com.lab6.map_socialnetwork_gui;

import com.lab6.map_socialnetwork_gui.business.MessageService;
import com.lab6.map_socialnetwork_gui.business.NetworkService;
import com.lab6.map_socialnetwork_gui.business.UserService;
import com.lab6.map_socialnetwork_gui.controllers.LoginController;
import com.lab6.map_socialnetwork_gui.domain.*;
import com.lab6.map_socialnetwork_gui.repository.IRepository;
import com.lab6.map_socialnetwork_gui.repository.database.*;
import com.lab6.map_socialnetwork_gui.repository.paging.PagingRepository;
import com.lab6.map_socialnetwork_gui.utils.Graph;
import com.lab6.map_socialnetwork_gui.validator.FriendshipValidator;
import com.lab6.map_socialnetwork_gui.validator.IValidator;
import com.lab6.map_socialnetwork_gui.validator.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        PagingRepository<User> repoUserDb = new UserDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetwork", "postgres", "postgres");
        PagingRepository<Event> repoEventDb = new EventDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetwork", "postgres", "postgres");

        IRepository<Friendship> repoFriendshipsDb = new FriendshipDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetwork", "postgres", "postgres");
        Graph graph = new Graph();
        IValidator<User> userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        UserService userService = new UserService(repoUserDb, userValidator, graph, repoFriendshipsDb);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("controllers/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 500);
        stage.setTitle("Log in / Register");
        stage.setScene(scene);


        IRepository<FriendRequest> requestRepo = new RequestDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetwork", "postgres", "postgres");
        NetworkService networkService = new NetworkService(repoFriendshipsDb, graph, repoUserDb, requestRepo, repoEventDb,friendshipValidator);
        IRepository<Message> messageRepo = new MessageDbRepo("jdbc:postgresql://localhost:5432/mapSocialNetwork", "postgres", "postgres");
        MessageService messageService = new MessageService(messageRepo, repoUserDb);

        LoginController loginController = fxmlLoader.getController();
        loginController.setService(userService,networkService,messageService);
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}