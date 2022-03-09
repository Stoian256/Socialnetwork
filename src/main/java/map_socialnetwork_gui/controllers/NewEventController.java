package com.lab6.map_socialnetwork_gui.controllers;


import com.lab6.map_socialnetwork_gui.business.NetworkService;
import com.lab6.map_socialnetwork_gui.domain.Message;
import com.lab6.map_socialnetwork_gui.domain.User;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.lab6.map_socialnetwork_gui.utils.Constants.DATE_TIME_FORMATTER;


public class NewEventController {

    @FXML
    private TextField textFieldEventName;
   @FXML
   private DatePicker eventDate;
   @FXML
    ImageView eventImage;

    private NetworkService networkService;
    User currentUser;
    Stage dialogStage;
    FileChooser fileChooser;
    @FXML
    private void initialize() {

    }

    public void setServices(NetworkService networkService,User currentUser,Stage stage) {
        this.networkService = networkService;
        this.currentUser=currentUser;
        this.dialogStage=stage;
    }

    @FXML
    public void handleAddEvent(){
        if (eventDate.getValue() != null && textFieldEventName.getText() != ""&&eventImage.getImage()!=null) {
            LocalDateTime eventDateGet = LocalDateTime.of(eventDate.getValue(), LocalTime.of(12,00));
            String eventName= textFieldEventName.getText();
            try {
                networkService.addEvent(currentUser.getUserID(),eventName,eventImage.getAccessibleText(),eventDateGet);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Creare Eveniment", "Evenimentu ls-a creat cu succes!");
                dialogStage.close();
            } catch (RepoException e) {
                e.printStackTrace();
            }


        } else {
            String erori = "";
            if (textFieldEventName.getText() == "")
                erori += "Nu ati dat numele evenimentului";
            if (eventDate.getValue() == null )
                erori += "\nNu ati selectat data!";
            if (eventImage.getImage() == null )
                erori += "\nNu ati selectat imaginea!";
            MessageAlert.showErrorMessage(null, erori);
        }
    }

    @FXML
    public void handleChooseImage(){
        fileChooser =new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        File file =fileChooser.showOpenDialog(new Stage());
        if(file!=null) {
            String fileName=file.getAbsolutePath();
            String extension = fileName.substring(fileName.lastIndexOf('.')+1);
            File destinationFile = new File("./src/main/resources/com/lab6/map_socialnetwork_gui/images/"+textFieldEventName.getText()+"."+extension);

            try {
                Files.copy(file.toPath(),destinationFile.toPath());
                eventImage.setImage(new Image("file:"+destinationFile));
                eventImage.setAccessibleText(textFieldEventName.getText()+"."+extension);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}