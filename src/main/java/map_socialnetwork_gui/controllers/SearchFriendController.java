package com.lab6.map_socialnetwork_gui.controllers;


import com.lab6.map_socialnetwork_gui.business.NetworkService;
import com.lab6.map_socialnetwork_gui.business.UserService;
import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.exceptions.ServiceException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.util.function.Predicate;
import java.util.stream.Collectors;


public class SearchFriendController {
    ObservableList<User> modelUser = FXCollections.observableArrayList();
    @FXML
    TableColumn<User, String>  tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableView<User> tableViewUsers;
    ScrollBar tableViewUsersScrollBar;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;

    private NetworkService networkService;
    private UserService userService;
    User currentUser;
    Stage dialogStage;
    @FXML
    private void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("userFirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("userLastName"));
        tableViewUsers.setItems(modelUser);

        textFieldFirstName.textProperty().addListener(o -> handleFilter());
        textFieldLastName.textProperty().addListener(o -> handleFilter());
    }

    private void handleFilter() {
        Predicate<User> p1 = n -> n.getUserFirstName().toUpperCase().startsWith(textFieldFirstName.getText().toUpperCase());
        Predicate<User> p2 = n -> n.getUserLastName().toUpperCase().startsWith(textFieldLastName.getText().toUpperCase());

        modelUser.setAll(userService.getAllUsers()
                .stream()
                .filter(t->t.getUserID()!=currentUser.getUserID())
                .filter(p1.and(p2))
                .collect(Collectors.toList()));
    }
    public void setServices(NetworkService networkService,User currentUser,Stage stage,UserService userService) {
        this.networkService = networkService;
        this.userService = userService;
        userService.setPageSize(7);
        this.currentUser=currentUser;
        this.dialogStage=stage;
        /*modelUser.setAll(userService.getAllUsers()
                .stream()
                .filter(t->t.getUserID()!=currentUser.getUserID())
                .collect(Collectors.toList()));*/
        Platform.runLater(() -> {
        tableViewUsersScrollBar = (ScrollBar) tableViewUsers.lookup(".scroll-bar:vertical");

        tableViewUsersScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((Double) newValue == 1.0) {
                modelUser.addAll(userService.getNextUsers()
                        .stream()
                        .filter(t->t.getUserID()!=currentUser.getUserID())
                        .collect(Collectors.toList()));
            }
        });
        });
        modelUser.setAll(userService.getUsersOnPage(0)
                .stream()
                .filter(t->t.getUserID()!=currentUser.getUserID())
                .collect(Collectors.toList()));
    }

    @FXML
    public void handleSendRequest(){
        if(tableViewUsers.getSelectionModel().getSelectedItem() != null) {
            User user = modelUser.get(tableViewUsers.getSelectionModel().getSelectedIndex());
            try {
                networkService.sendRequest(currentUser.getUserID(), user.getUserID());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Cerere prietenie", "Cerere trimisa cu succes!");
                dialogStage.close();
            } catch (RepoException | ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else
        MessageAlert.showErrorMessage(null, "Nu ati selectat un utilizator!");

    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}
