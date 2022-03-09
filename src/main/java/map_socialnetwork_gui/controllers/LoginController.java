package com.lab6.map_socialnetwork_gui.controllers;

import com.lab6.map_socialnetwork_gui.HelloApplication;
import com.lab6.map_socialnetwork_gui.business.MessageService;
import com.lab6.map_socialnetwork_gui.business.NetworkService;
import com.lab6.map_socialnetwork_gui.business.UserService;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.exceptions.ValidException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    private UserService userService;
    private NetworkService networkService;
    private  MessageService messageService;
    @FXML
    private TabPane tabPaneMain;
    @FXML
    private TextField textFieldLoginUsername;
    @FXML
    private PasswordField textFieldLoginPassword;
    @FXML
    private TextField textFieldRegisterFirstName;
    @FXML
    private TextField textFieldRegisterLastName;
    @FXML
    private TextField textFieldRegisterUsername;
    @FXML
    private PasswordField textFieldRegisterPassword;
    @FXML
    private Button buttonLogin;
    @FXML
    private Button buttonRegister;
    @FXML
    private Label labelLoginError;
    @FXML
    private Label labelRegisterError;
    @FXML
    private Label labelLoginErrorUsername;
    @FXML
    private Label labelLoginErrorPassword;
    @FXML
    private Label labelRegisterErrorFirstName;
    @FXML
    private Label labelRegisterErrorLastName;
    @FXML
    private Label labelRegisterErrorUsername;
    @FXML
    private Label labelRegisterErrorPassword;

    public void setService(UserService userService, NetworkService networkService, MessageService messageService) {
        this.userService = userService;
        this.networkService=networkService;
        this.messageService=messageService;
    }

    private void initObjects() {
        initErrorLabels();
        initTextFields();
    }

    private void initErrorLabels() {
        labelLoginErrorUsername.setText("");
        labelLoginErrorPassword.setText("");
        labelRegisterErrorFirstName.setText("");
        labelRegisterErrorLastName.setText("");
        labelRegisterErrorUsername.setText("");
        labelRegisterErrorPassword.setText("");
        labelLoginError.setText("");
        labelRegisterError.setText("");
    }

    private void initTextFields() {
        textFieldLoginUsername.setText("");
        textFieldLoginPassword.setText("");
        textFieldRegisterFirstName.setText("");
        textFieldRegisterLastName.setText("");
        textFieldRegisterUsername.setText("");
        textFieldRegisterPassword.setText("");
    }

    @FXML
    public void initialize() {
        initObjects();
    }

    @FXML
    protected void onTabPaneSelectionChanged() {
        initObjects();
    }

    @FXML
    protected void onTextFieldClick() {
        initErrorLabels();
    }

    @FXML
    protected void onButtonLoginClick() {
        boolean allGood = true;
        String username = textFieldLoginUsername.getText();
        String password = textFieldLoginPassword.getText();

        if (userService.getUserByUsername(username) == null) {
            labelLoginErrorUsername.setText("Username does not exist!");
            allGood = false;
        } else {
            if (encryptMD5(password)!=null && !userService.getUserByUsername(username).getUserPassword().equals(encryptMD5(password))) {
                labelLoginErrorPassword.setText("Incorrect password!");
                allGood = false;
            }
        }
        if (username.equals("")) {
            labelLoginErrorUsername.setText("Username can not be empty!");
            allGood = false;
        }
        if (password.equals("")) {
            labelLoginErrorPassword.setText("Password can not be empty!");
            allGood = false;
        }

        if (allGood) {
            try {
                Stage parentStage = (Stage)buttonLogin.getScene().getWindow();

                // create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("main-view.fxml"));

                // Create the dialog Stage.
                Stage mainStage = new Stage();
                mainStage.setTitle("Main view");
                mainStage.initModality(Modality.WINDOW_MODAL);
                //mainStage.initOwner(parentStage);
                Scene scene = new Scene(loader.load(), 823, 453);
                mainStage.setScene(scene);
                //mainStage.setOnHidden(e -> parentStage.show());

                MainController mainController = loader.getController();
                mainController.setUser(userService.getUserByUsername(username));
                mainController.setServices(networkService,userService,messageService);
                mainStage.show();
                //parentStage.hide();
                parentStage.close();

            } catch (IOException e) {
                labelLoginError.setText(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    protected void onButtonRegisterClick() {
        boolean allGood = true;
        String firstName = textFieldRegisterFirstName.getText();
        String lastName = textFieldRegisterLastName.getText();
        String username = textFieldRegisterUsername.getText();
        String password = textFieldRegisterPassword.getText();

        if (firstName.equals("")) {
            labelRegisterErrorFirstName.setText("First name can not be empty!");
            allGood = false;
        }
        if (lastName.equals("")) {
            labelRegisterErrorLastName.setText("Last name can not be empty!");
            allGood = false;
        }
        if (username.equals("")) {
            labelRegisterErrorUsername.setText("Username can not be empty!");
            allGood = false;
        }
        if (password.equals("")) {
            labelRegisterErrorPassword.setText("Password can not be empty!");
            allGood = false;
        }
        if (userService.getUserByUsername(username) != null) {
            labelRegisterErrorUsername.setText("Username is already used!");
            allGood = false;
        }

        if (allGood) {
            try {
                if(encryptMD5(password)!=null)
                    userService.addUser(firstName, lastName, username, encryptMD5(password));
                initObjects();
                tabPaneMain.getSelectionModel().select(0);
            } catch (ValidException | RepoException e) {
                labelRegisterError.setText(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    private String encryptMD5(String input){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] password = messageDigest.digest(input.getBytes());
            BigInteger bigInteger = new BigInteger(1, password);
            String hashtext = bigInteger.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
