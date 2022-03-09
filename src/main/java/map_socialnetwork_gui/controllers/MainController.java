package com.lab6.map_socialnetwork_gui.controllers;

import com.lab6.map_socialnetwork_gui.business.MessageService;
import com.lab6.map_socialnetwork_gui.business.NetworkService;
import com.lab6.map_socialnetwork_gui.business.UserService;
import com.lab6.map_socialnetwork_gui.domain.*;
import com.lab6.map_socialnetwork_gui.exceptions.RepoException;
import com.lab6.map_socialnetwork_gui.exceptions.ServiceException;
import com.lab6.map_socialnetwork_gui.exceptions.ValidException;
import com.lab6.map_socialnetwork_gui.model.FriendStatus;
import com.lab6.map_socialnetwork_gui.utils.Observer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.controlsfx.control.textfield.TextFields;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



import static com.lab6.map_socialnetwork_gui.utils.Constants.DATE_TIME_FORMATTER;

public class MainController extends Observer {

    private User currentUser;
    private NetworkService networkService;
    private UserService userService;
    @FXML
    Tab profileTab;

    @FXML
    ListView listViewEvents;
    @FXML
    TableView<User> tableViewEvents;
    @FXML
    TableColumn<User,String> firstNameEvents;
    @FXML
    TableColumn<User,String> lastNameEvents;
    //PAGE
    @FXML
    TabPane tabPane;
    @FXML
    Label pageUsernameLabel;

    @FXML
    Button seeAllFriends;

    ObservableList<FriendDto> modelFriend1 = FXCollections.observableArrayList();
    @FXML
    TableView<FriendDto> friendsTableView1;
    @FXML
    TableColumn<FriendDto,String> tableColumnFirstName1;
    @FXML
    TableColumn<FriendDto,String> tableColumnLastName1;
    @FXML
    TableColumn<FriendDto,String> tableColumnFriendshipID1;
    @FXML
    TableColumn<FriendDto,String> tableColumnDate1;

    ObservableList<RequestDTO> modelRequests1 = FXCollections.observableArrayList();
    @FXML
    TableView<RequestDTO> requestsTableView1;
    @FXML
    TableColumn<RequestDTO, String> tableColumnRequestsFirstName1;
    @FXML
    TableColumn<RequestDTO, String> tableColumnRequestsLastName1;
    @FXML
    TableColumn<RequestDTO, String> tableColumnRequestsDate1;

    ObservableList<RequestDTO> modelSentRequests1 = FXCollections.observableArrayList();
    @FXML
    TableView<RequestDTO> sentRequestsTableView1;
    @FXML
    TableColumn<RequestDTO, String> tableColumnSentRequestsFirstName1;
    @FXML
    TableColumn<RequestDTO, String> tableColumnSentRequestsLastName1;
    @FXML
    TableColumn<RequestDTO, String> tableColumnSentRequestsDate1;
    @FXML
    TableColumn<RequestDTO, String> tableColumnSentRequestsStatus1;

    //PAGE
    @FXML
    Button buttonLogOut;

    ObservableList<FriendDto> modelFriend = FXCollections.observableArrayList();
    @FXML
    TableView<FriendDto> friendsTableView;
    @FXML
    TableColumn<FriendDto,String> tableColumnFirstName;
    @FXML
    TableColumn<FriendDto,String> tableColumnLastName;
    @FXML
    TableColumn<FriendDto,String> tableColumnFriendshipID;
    @FXML
    TableColumn<FriendDto,String> tableColumnDate;

    ObservableList<RequestDTO> modelRequests = FXCollections.observableArrayList();
    @FXML
    TableView<RequestDTO> requestsTableView;
    @FXML
    TableColumn<RequestDTO, String> tableColumnRequestsFirstName;
    @FXML
    TableColumn<RequestDTO, String> tableColumnRequestsLastName;
    @FXML
    TableColumn<RequestDTO, String> tableColumnRequestsDate;

    ObservableList<RequestDTO> modelSentRequests = FXCollections.observableArrayList();
    @FXML
    TableView<RequestDTO> sentRequestsTableView;
    @FXML
    TableColumn<RequestDTO, String> tableColumnSentRequestsFirstName;
    @FXML
    TableColumn<RequestDTO, String> tableColumnSentRequestsLastName;
    @FXML
    TableColumn<RequestDTO, String> tableColumnSentRequestsDate;
    @FXML
    TableColumn<RequestDTO, String> tableColumnSentRequestsStatus;

    //MESSAGES
    @FXML
    private TextArea messageBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label onlineCountLabel;
    @FXML
    private ImageView userImageView;
    @FXML
    private Button recordBtn;
    @FXML
    ListView chatPane;
    @FXML
    TextField searchFriendMessage;
    @FXML
    ListView statusList;
    @FXML
    BorderPane borderPane;
    @FXML
    ScrollPane messageScrollPane;
    @FXML
    ComboBox statusComboBox;
    @FXML
    ImageView microphoneImageView;
    @FXML
    TableView<User> userList;
    ScrollBar userListScrollBar;
    ScrollBar eventListScrollBar;
    @FXML
    TableColumn<User,String> tableUsersNume;
    @FXML
    TableColumn<User,String> tableusersPrenume;
    @FXML
    TableColumn<User,String> tableusersid;
    ObservableList<User> modelFriendMessages = FXCollections.observableArrayList();
    Thread threadSyncMessages;
    Thread threadRefreshChatPane;
    private int numberOfMessages=-1;


    private MessageService messageService;
    //MESSAGES

    //STATISTICS
    @FXML
    DatePicker statisticsStartDate;
    @FXML
    DatePicker statisticsEndDate;
    @FXML
    Button statisticsFriendsMessages;
    @FXML
    Button statisticsFriendMessages;
    @FXML
    Button statisticsSave;
    @FXML
    TextArea statisticsTextArea;
    @FXML
    TextField statisticsTextField;
    @FXML
    RadioButton txtRadioButton;
    @FXML
    RadioButton pdfRadioButton;
    FileChooser fileChooser;
    //STATISTICS

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setServices(NetworkService networkService,UserService userService,MessageService messageService) {
        this.networkService = networkService;
        networkService.addObserver(this);
        this.userService = userService;
        this.messageService = messageService;
        threadRefreshChatPane =new Thread((this::handleThreadRefreshChatPane));
        threadSyncMessages = new Thread(this::handleThreadSyncMessages);
        threadSyncMessages.setDaemon(true);
        threadRefreshChatPane.setDaemon(true);
        threadSyncMessages.start();
        threadRefreshChatPane.start();
        initModel();
    }

    public void initialize() {
        tableColumnFriendshipID.setCellValueFactory(new PropertyValueFactory<FriendDto, String>("friendshipID"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<FriendDto, String>("firsName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<FriendDto, String>("lastName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<FriendDto, String>("date"));
        friendsTableView.setItems(modelFriend);

        tableColumnRequestsFirstName.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("firstName"));
        tableColumnRequestsLastName.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("lastName"));
        tableColumnRequestsDate.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("date"));
        requestsTableView.setItems(modelRequests);

        tableColumnSentRequestsFirstName.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("firstName"));
        tableColumnSentRequestsLastName.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("lastName"));
        tableColumnSentRequestsDate.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("date"));
        tableColumnSentRequestsStatus.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("status"));
        sentRequestsTableView.setItems(modelSentRequests);

        tableUsersNume.setCellValueFactory(new PropertyValueFactory<User, String>("userLastName"));
        tableusersPrenume.setCellValueFactory(new PropertyValueFactory<User, String>("userFirstName"));
        tableusersid.setCellValueFactory(new PropertyValueFactory<User, String>("userID"));
        userList.setItems(modelFriendMessages);

        //PAGE

        profileTab.setOnSelectionChanged(eventProfile -> {
            if (profileTab.isSelected()) {
                listViewEvents.getItems().clear();
                tableViewEvents.getItems().clear();
                List<Event> events = networkService.getEventsOnPage(0);
                for(Event event:events){
                    Image image;
                    ImageView imageView=new ImageView();
                    imageView.setFitHeight(230);
                    imageView.setFitWidth(400);
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    imageView.setCache(true);

                    Label denumire=new Label();
                    denumire.setFont(Font.font(24));
                    denumire.setTextFill(Color.BLACK);
                    denumire.setAlignment(Pos.TOP_LEFT);

                    Label data=new Label();
                    data.setFont(Font.font (Font.getDefault().getFamily(),FontWeight.BOLD,24));
                    data.setTextFill(Color.RED);
                    data.setAlignment(Pos.TOP_LEFT);

                    VBox x = new VBox();
                    x.setAlignment(Pos.TOP_LEFT);
                    if(event.getPhotoURL().startsWith("https"))
                        image=new Image(event.getPhotoURL());

                    else image =new Image("file:"+"./src/main/resources/com/lab6/map_socialnetwork_gui/images/"+event.getPhotoURL());

                    imageView.setImage(image);
                    denumire.setText(event.getEventName());
                    data.setText(event.getDateTime().format(DATE_TIME_FORMATTER));
                    x.getChildren().setAll(imageView,data,denumire);
                    x.setAccessibleText(String.valueOf(event.getEventID()));
                    listViewEvents.getItems().add(x);
                }
            }
        });
        tableColumnFriendshipID1.setCellValueFactory(new PropertyValueFactory<FriendDto, String>("friendshipID"));
        tableColumnFirstName1.setCellValueFactory(new PropertyValueFactory<FriendDto, String>("firsName"));
        tableColumnLastName1.setCellValueFactory(new PropertyValueFactory<FriendDto, String>("lastName"));
        tableColumnDate1.setCellValueFactory(new PropertyValueFactory<FriendDto, String>("date"));
        friendsTableView1.setItems(modelFriend);

        tableColumnRequestsFirstName1.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("firstName"));
        tableColumnRequestsLastName1.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("lastName"));
        tableColumnRequestsDate1.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("date"));
        requestsTableView1.setItems(modelRequests);

        tableColumnSentRequestsFirstName1.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("firstName"));
        tableColumnSentRequestsLastName1.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("lastName"));
        tableColumnSentRequestsDate1.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("date"));
        tableColumnSentRequestsStatus1.setCellValueFactory(new PropertyValueFactory<RequestDTO, String>("status"));
        sentRequestsTableView1.setItems(modelSentRequests);
        //PAGE

        firstNameEvents.setCellValueFactory(new PropertyValueFactory<User, String>("userFirstName"));
        lastNameEvents.setCellValueFactory(new PropertyValueFactory<User, String>("userLastName"));
    }

    private void initModel() {
        initModelFriend();
        initModelRequests();
        initModelSentRequests();
        initModelMessages();
        initStatistics();
        initPage();
        initModelEvents();
    }

    @FXML
    private void initModelEvents(){

        networkService.setPageSize(2);
        Platform.runLater(() -> {
            eventListScrollBar = (ScrollBar) listViewEvents.lookup(".scroll-bar:vertical");

            eventListScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                if ((Double) newValue == 1.0) {
                    List<Event> tempEvents=networkService.getNextEvents();
                    for(Event event:tempEvents){
                        Image image;
                        ImageView imageView=new ImageView();
                        imageView.setFitHeight(230);
                        imageView.setFitWidth(400);
                        imageView.setPreserveRatio(true);
                        imageView.setSmooth(true);
                        imageView.setCache(true);

                        Label denumire=new Label();
                        denumire.setFont(Font.font(24));
                        denumire.setTextFill(Color.BLACK);
                        denumire.setAlignment(Pos.TOP_LEFT);

                        Label data=new Label();
                        data.setFont(Font.font (Font.getDefault().getFamily(),FontWeight.BOLD,24));
                        data.setTextFill(Color.RED);
                        data.setAlignment(Pos.TOP_LEFT);

                        VBox x = new VBox();
                        x.setAlignment(Pos.TOP_LEFT);
                        if(event.getPhotoURL().startsWith("https"))
                            image=new Image(event.getPhotoURL());

                        else image =new Image("file:"+"./src/main/resources/com/lab6/map_socialnetwork_gui/images/"+event.getPhotoURL());

                        imageView.setImage(image);
                        denumire.setText(event.getEventName());
                        data.setText(event.getDateTime().format(DATE_TIME_FORMATTER));
                        x.getChildren().setAll(imageView,data,denumire);
                        x.setAccessibleText(String.valueOf(event.getEventID()));
                        listViewEvents.getItems().add(x);
                    }
                }
            });
        });



        tableViewEvents.getItems().clear();
        List<Event> events = networkService.getEventsOnPage(0);
        for(Event event:events){
            Image image;
            ImageView imageView=new ImageView();
            imageView.setFitHeight(230);
            imageView.setFitWidth(400);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            Label denumire=new Label();
            denumire.setFont(Font.font(24));
            denumire.setTextFill(Color.BLACK);
            denumire.setAlignment(Pos.TOP_LEFT);

            Label data=new Label();
            data.setFont(Font.font (Font.getDefault().getFamily(),FontWeight.BOLD,24));
            data.setTextFill(Color.RED);
            data.setAlignment(Pos.TOP_LEFT);

            VBox x = new VBox();
            x.setAlignment(Pos.TOP_LEFT);
            if(event.getPhotoURL().startsWith("https"))
                image=new Image(event.getPhotoURL());

            else image =new Image("file:"+"./src/main/resources/com/lab6/map_socialnetwork_gui/images/"+event.getPhotoURL());

            imageView.setImage(image);
            denumire.setText(event.getEventName());
            data.setText(event.getDateTime().format(DATE_TIME_FORMATTER));
            x.getChildren().setAll(imageView,data,denumire);
            x.setAccessibleText(String.valueOf(event.getEventID()));
            listViewEvents.getItems().add(x);
        }
    }

    @FXML
    void  onButtonJoinEventClick(){
        if(listViewEvents.getSelectionModel().getSelectedItem() != null) {
            try {
                VBox vBox = (VBox) listViewEvents.getSelectionModel().getSelectedItem();
                networkService.joinEvent(Integer.valueOf(vBox.getAccessibleText()), currentUser.getUserID());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Joined Event"," Joined event successfully!");
                tableViewEvents.getItems().add(currentUser);
            } catch (RepoException e) {
                MessageAlert.showErrorMessage(null,e.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"Nu ati ales niciun eveniment!");
    }

    @FXML
    void  onButtonLeaveEventClick(){
        if(listViewEvents.getSelectionModel().getSelectedItem() != null) {
            try {
                VBox vBox = (VBox) listViewEvents.getSelectionModel().getSelectedItem();
                networkService.leaveEvent(Integer.valueOf(vBox.getAccessibleText()), currentUser.getUserID());
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Leave Event"," Left event successfully!");
                tableViewEvents.getItems().remove(currentUser);
            } catch (RepoException e) {
                MessageAlert.showErrorMessage(null,e.getMessage());
            }
        }
        else MessageAlert.showErrorMessage(null,"Nu ati ales niciun eveniment!");
    }

    @FXML
    void  onButtonAddNewEventClick() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("new-event.fxml"));

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add event");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            NewEventController newEventController = loader.getController();
            newEventController.setServices(networkService, currentUser, dialogStage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadParticipants(){
        tableViewEvents.getItems().clear();
        if(listViewEvents.getSelectionModel().getSelectedItem() != null) {
            try {
                VBox vBox = (VBox) listViewEvents.getSelectionModel().getSelectedItem();
                List<Integer> participants = networkService.getOneEvent(Integer.valueOf(vBox.getAccessibleText())).getParticipants();
                if(participants.get(0)!=0) {
                    for (Integer participant : participants) {
                        //MessageAlert.showErrorMessage(null, participant.toString());
                        User user = userService.getOneUser(participant);
                        tableViewEvents.getItems().add(user);
                    }
                }
            } catch (RepoException e) {
                e.printStackTrace();
            }
        }

    }
    @FXML
    private void initModelFriend() {
        try {

            if(currentUser!=null) {
                List<FriendDto> friendDtos = networkService.getFriends(currentUser.getUserID());
                modelFriend.setAll(friendDtos);
            }
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initModelRequests() {
        try {
            List<RequestDTO> requests = networkService.getRequestsForUserDTO(currentUser.getUserID());
            modelRequests.setAll(requests);
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initModelSentRequests() {
        try {
            List<RequestDTO> sentRequests = networkService.getSentRequestsForUserDTO(currentUser.getUserID());
            modelSentRequests.setAll(sentRequests);
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }

    //PAGE
    @FXML
    private void initPage() {
        //initModelFriend();
        //initModelRequests();
        //initModelSentRequests();
        this.pageUsernameLabel.setText(currentUser.getUserFirstName()+"  "+currentUser.getUserLastName());
    }
    //PAGE

    //STATISTICS
    @FXML
    private void initStatistics() {
        txtRadioButton.setSelected(true);
        fileChooser =new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        try {
            List<FriendDto> friendDtos  =networkService.getFriends(currentUser.getUserID());
            TextFields.bindAutoCompletion(statisticsTextField,friendDtos);
        } catch (RepoException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTxtRadioButton(){
       if(pdfRadioButton.selectedProperty().get() == true)
           pdfRadioButton.setSelected(false);
    }

    @FXML
    private void handlePdfRadioButton(){
        if(txtRadioButton.selectedProperty().get() == true)
            txtRadioButton.setSelected(false);
    }

    @FXML
    private void handleSaveStatistics(){
        if(txtRadioButton.selectedProperty().get() == true)
            saveInTxt();
        else if(pdfRadioButton.selectedProperty().get() == true)
            saveInPdf();
    }

    private void saveInTxt(){
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text file","*.txt"));
        File file =fileChooser.showSaveDialog(new Stage());
        if(file!=null) {
            try {
                PrintWriter printWriter = new PrintWriter(file);
                printWriter.write(statisticsTextArea.getText());
                printWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private void saveInPdf() {
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf file", "*.pdf"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            PDDocument doc = new PDDocument();

            try {
                PDPage page = new PDPage();
                doc.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(doc, page);
                float leading = 1.5f * 15;

                List<String> lines = List.of(statisticsTextArea.getText().split("\n"));
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 15);
                contentStream.newLineAtOffset(page.getMediaBox().getLowerLeftX(), page.getMediaBox().getUpperRightY());

                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    contentStream.newLineAtOffset(0, -leading);
                    contentStream.showText(line);
                    if (i % 34 == 0 && i != 0) {
                        contentStream.endText();
                        contentStream.close();
                        page = new PDPage();
                        doc.addPage(page);
                        contentStream = new PDPageContentStream(doc, page);
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 15);
                        contentStream.newLineAtOffset(page.getMediaBox().getLowerLeftX(), page.getMediaBox().getUpperRightY());
                    }

                }
                contentStream.endText();
                contentStream.close();
                //"C:\\Users\\silvi\\IdeaProjects\\MAP_SocialNetwork_GUI\\myfile.pdf"
                doc.save(file);
                doc.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleFriendsMessages() {
        if (statisticsStartDate.getValue() != null && statisticsEndDate.getValue() != null) {
            LocalDateTime startDate = LocalDateTime.of(statisticsStartDate.getValue(), LocalTime.MIN);
            LocalDateTime endDate = LocalDateTime.of(statisticsEndDate.getValue(), LocalTime.MAX);
            statisticsTextArea.clear();
            try {
                List<FriendDto> friendDtos = networkService.getFriendsPeriod(currentUser.getUserID(), startDate, endDate);
                friendDtos.forEach(t -> {
                    statisticsTextArea.appendText(t.getFirsName() + "  ");
                    statisticsTextArea.appendText(t.getLastName() + "  ");
                    statisticsTextArea.appendText(t.getDate().format(DATE_TIME_FORMATTER) + "\n");
                });

                statisticsTextArea.appendText("\n");
                List<Message> messageList = messageService.getMessagesList().stream().filter(t -> t.getDate() != null && t.getDate().isAfter(startDate) && t.getDate().isBefore(endDate)).collect(Collectors.toList());
                messageList.forEach(t -> {
                    statisticsTextArea.appendText("From:" + t.getFrom().getUserFirstName() + " " + t.getFrom().getUserLastName() + "\n");
                    statisticsTextArea.appendText("To:" + t.getTo().get(0).getUserFirstName() + " " + t.getTo().get(0).getUserLastName() + "\n");
                    if (t.getReply() != null)
                        statisticsTextArea.appendText("Reply:" + t.getReply().getMessage() + "\n");
                    statisticsTextArea.appendText("Message:" + t.getMessage().trim() + "\n");
                    statisticsTextArea.appendText("Date:" + t.getDate().format(DATE_TIME_FORMATTER) + "\n\n");
                });

                if(friendDtos.size()==0&&messageList.size()==0)
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "", "Nu s-au gasit informatii!");
            } catch (RepoException e) {
                e.printStackTrace();
            }

        } else
            MessageAlert.showErrorMessage(null, "Nu ati selectat perioada!");
    }

    @FXML
    private void handleFriendMessages() {
        if (statisticsStartDate.getValue() != null && statisticsEndDate.getValue() != null && statisticsTextField.getText() != "") {
            LocalDateTime startDate = LocalDateTime.of(statisticsStartDate.getValue(), LocalTime.MIN);
            LocalDateTime endDate = LocalDateTime.of(statisticsEndDate.getValue(), LocalTime.MAX);
            String[] userData = statisticsTextField.getText().split("\\|");
            if (userData.length < 4)
                MessageAlert.showErrorMessage(null, "Date incorecte prieten!");
            else {
                String username = userData[2].strip();
                List<User> users = userService.getAllUsers().stream().filter(t -> t.getUserUsername().equals(username)).collect(Collectors.toList());

                if (users.size() == 0)
                    MessageAlert.showErrorMessage(null, "Date incorecte prieten!");
                else {
                    User friend = users.get(0);

                    statisticsTextArea.clear();
                    try {
                        List<Message> messageList = messageService.getTwoUsersMessages(currentUser.getUserID(), friend.getUserID()).stream().filter(t -> t.getDate() != null && t.getDate().isAfter(startDate) && t.getDate().isBefore(endDate)).collect(Collectors.toList());
                        messageList.forEach(t -> {
                            statisticsTextArea.appendText("From:" + t.getFrom().getUserFirstName() + " " + t.getFrom().getUserLastName() + "\n");
                            statisticsTextArea.appendText("To:" + t.getTo().get(0).getUserFirstName() + " " + t.getTo().get(0).getUserLastName() + "\n");
                            if (t.getReply() != null)
                                statisticsTextArea.appendText("Reply:" + t.getReply().getMessage() + "\n");
                            statisticsTextArea.appendText("Message:" + t.getMessage().trim() + "\n");
                            statisticsTextArea.appendText("Date:" + t.getDate().format(DATE_TIME_FORMATTER) + "\n\n");
                        });
                        if(messageList.size()==0)
                            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "", "Nu s-au gasit informatii!");

                    } catch (RepoException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            String erori = "";
            if (statisticsStartDate.getValue() == null || statisticsEndDate.getValue() == null)
                erori += "Nu ati selectat perioada!";
            if (statisticsTextField.getText() == "")
                erori += "\nNu ati selectat prietenul!";
            MessageAlert.showErrorMessage(null, erori);
        }
    }
    //STATISTICS
    //MESSAGES
    @FXML
    private void initModelMessages(){
        numberOfMessages=-1;
        this.usernameLabel.setText(currentUser.getUserFirstName()+"  "+currentUser.getUserLastName());
        userService.setPageSize(10);
        List<User> users=userService.getUsersOnPage(0).stream().filter(t->!t.getUserUsername().equals(currentUser.getUserUsername())).toList();
        modelFriendMessages.setAll(users);
        searchFriendMessage.textProperty().addListener(o -> handleFilter());
        Platform.runLater(() -> {
            userListScrollBar = (ScrollBar) userList.lookup(".scroll-bar:vertical");

            userListScrollBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                if ((Double) newValue == 1.0) {
                    modelFriendMessages.addAll(userService.getNextUsers()
                            .stream()
                            .filter(t->t.getUserID()!=currentUser.getUserID())
                            .collect(Collectors.toList()));
                }
            });
        });

    }

    private void handleFilter() {
        String searchFriend = searchFriendMessage.getText().trim();
        //Predicate<User> p1 = n -> n.getUserFirstName().toUpperCase().startsWith(prenume.toUpperCase());
        Predicate<User> p1 = n -> (n.getUserFirstName()+" "+n.getUserLastName()).toUpperCase().startsWith(searchFriend.toUpperCase());
        Predicate<User> p2 = n -> (n.getUserLastName()+" "+n.getUserFirstName()).toUpperCase().startsWith(searchFriend.toUpperCase());
        //Predicate<User> p2 = n -> n.getUserLastName().toUpperCase().startsWith(nume.toUpperCase());
        modelFriendMessages.setAll(userService.getAllUsers()
                .stream()
                .filter(t->t.getUserID()!=currentUser.getUserID())
                .filter(p1.or(p2))
                .collect(Collectors.toList()));
    }

    public  void sendButtonAction() throws IOException {
        if (userList.getSelectionModel().getSelectedItem() != null) {
            String msg = messageBox.getText();
            int ID = modelFriendMessages.get(userList.getSelectionModel().getSelectedIndex()).getUserID();

            if (!messageBox.getText().isEmpty() && Pattern.compile("[a-zA-Z0-9]").matcher(messageBox.getText()).find()) {
                List<Integer> list = Arrays.asList(ID);
                int messageID;
                try {
                    if(chatPane.getSelectionModel().getSelectedItem()!=null) {
                        HBox hBox = (HBox) chatPane.getSelectionModel().getSelectedItem();
                        Label label = (Label) hBox.getChildren().get(0);
                        messageID= messageService.replyMessage(currentUser.getUserID(), Integer.parseInt(label.getAccessibleText()),msg);
                    }
                    else
                        messageID=messageService.sendMessage(currentUser.getUserID(), list, msg);
                    //numberOfMessages++;
                    addToChat(messageBox.getText(), currentUser.getUserID(),messageID);
                } catch (RepoException e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }

                messageBox.clear();
                chatPane.scrollTo(chatPane.getItems().size() - 1);
                chatPane.getSelectionModel().clearSelection();
            }
        }
        else
            MessageAlert.showErrorMessage(null, "Nu ati selectat un utilizator!");
    }



    @FXML
    public synchronized void loadMessages(){
        chatPane.getItems().clear();
        if(userList.getSelectionModel().getSelectedItem() != null) {
            numberOfMessages=0;
            int ID = modelFriendMessages.get(userList.getSelectionModel().getSelectedIndex()).getUserID();

            try {
                List<Message> messageList = messageService.getTwoUsersMessages(currentUser.getUserID(), ID);
                //numberOfMessages = messageList.size();
                for (Message message : messageList) {
                    if (message.getFrom().getUserID() == currentUser.getUserID())
                        if(message.getReply()!=null)
                        addToChatYourMessage(message.getMessage(),message.getReply().getMessage(),message.getMessageID());
                        else
                       addToChatYourMessage(message.getMessage(),"",message.getMessageID());
                    else{
                             if(message.getReply()!=null)
                        addToChatOtherMessage(message.getMessage(),message.getReply().getMessage(),message.getMessageID());
                             else
                        addToChatOtherMessage(message.getMessage(),"",message.getMessageID());
                             numberOfMessages++;
                    }


                }

            } catch (RepoException e) {
                e.printStackTrace();
            }
            chatPane.scrollTo(chatPane.getItems().size()-1);
        }
    }

    private void handleThreadSyncMessages(){
        while (1==1) {
            try {
                Thread.sleep(200);
                syncMessages();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
    private void handleThreadRefreshChatPane(){
        while (1==1) {
            try {
                Thread.sleep(100);
                chatPane.refresh();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
    public  synchronized void syncMessages(){
        if(messageService.getMessagesNumber()>numberOfMessages&&userList.getSelectionModel().getSelectedItem()!=null&&numberOfMessages!=-1) {
            try {
                int ID = modelFriendMessages.get(userList.getSelectionModel().getSelectedIndex()).getUserID();
                List<Message> messageList = messageService.getTwoUsersMessages(currentUser.getUserID(), ID).stream().filter(t->t.getFrom().getUserID()!=currentUser.getUserID()).collect(Collectors.toList());

                if (messageList.size() > numberOfMessages) {
                    for (int i = numberOfMessages; i < messageList.size(); i++) {
                        Message message = messageList.get(i);
                        if (message.getFrom().getUserID() == currentUser.getUserID())
                            Platform.runLater(() -> {
                                if (message.getReply() != null)
                                    addToChatYourMessage(message.getMessage(), message.getReply().getMessage(), message.getMessageID());
                                else
                                    addToChatYourMessage(message.getMessage(), "", message.getMessageID());
                                chatPane.scrollTo(chatPane.getItems().size()-1);
                            });
                        else
                            Platform.runLater(() -> {
                                if (message.getReply() != null)
                                    addToChatOtherMessage(message.getMessage(), message.getReply().getMessage(), message.getMessageID());
                                else
                                    addToChatOtherMessage(message.getMessage(), "", message.getMessageID());
                                chatPane.scrollTo(chatPane.getItems().size()-1);
                            });
                        numberOfMessages++;
                    }
                    chatPane.scrollTo(chatPane.getItems().size()-1);
                }

            } catch (RepoException e) {
                e.printStackTrace();
            }

        }
    }


    private synchronized HBox addToChatYourMessage(String msg,String replyMessage,int messageID){
        if(chatPane.getSelectionModel().getSelectedItem()!=null&&replyMessage.equals("")) {
            HBox hBox = (HBox) chatPane.getSelectionModel().getSelectedItem();
            Label label = (Label) hBox.getChildren().get(0);
            replyMessage=label.getText();
        }

        Label label = new Label();
        label.setText(msg);
        label.setFont(Font.font(30));
        label.setWrapText(true);
        label.setAlignment(Pos.TOP_CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setBackground(new Background(new BackgroundFill(Color.web("#059CF4"), new CornerRadii(20), null)));
        label.setTextFill(Color.WHITE);

        Label reply=new Label();
        reply.setText(replyMessage);
        reply.setFont(Font.font(30));
        reply.setWrapText(true);
        reply.setAlignment(Pos.TOP_CENTER);
        reply.setTextAlignment(TextAlignment.CENTER);
        reply.setBackground(new Background(new BackgroundFill(Color.web("#E7EAE6"), new CornerRadii(20), null)));
        reply.setTextFill(Color.BLACK);

        if(msg.length()>replyMessage.length()){
            if(msg.length()*16<chatPane.getWidth()/4*3)
                reply.setMinWidth(msg.length()*16);
            else
                reply.setMinWidth(chatPane.getWidth()/4*3);
            label.setMaxWidth(chatPane.getWidth()/4*3);
            reply.setMaxWidth(chatPane.getWidth()/4*3);
        }else{
            label.setMaxWidth(chatPane.getWidth()/4*3);
            reply.setMaxWidth(chatPane.getWidth()/4*3);
        }

        if(replyMessage!="") {
            label.setGraphic(reply);
            label.setContentDisplay(ContentDisplay.TOP);
        }
        label.setAccessibleText(String.valueOf(messageID));
        HBox x = new HBox();
        x.getChildren().add(label);
        x.setAlignment(Pos.TOP_RIGHT);
        chatPane.getItems().add(x);

        return x;
    }

    private synchronized HBox addToChatOtherMessage(String msg,String replyMessage,int messageID){
        if(chatPane.getSelectionModel().getSelectedItem()!=null&&replyMessage.equals("")) {
            HBox hBox = (HBox) chatPane.getSelectionModel().getSelectedItem();
            Label label = (Label) hBox.getChildren().get(0);
            replyMessage=label.getText();
        }

        Label label=new Label();
        label.setText(msg);
        label.setFont(Font.font(30));
        label.setWrapText(true);
        label.setAlignment(Pos.TOP_CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setBackground(new Background(new BackgroundFill(Color.web("#7BF087"), new CornerRadii(20), null)));
        label.setTextFill(Color.BLACK);

        Label reply=new Label();
        reply.setText(replyMessage);
        reply.setFont(Font.font(30));
        reply.setWrapText(true);
        reply.setAlignment(Pos.TOP_CENTER);
        reply.setTextAlignment(TextAlignment.CENTER);
        reply.setBackground(new Background(new BackgroundFill(Color.web("#B7DFF7"), new CornerRadii(20), null)));
        reply.setTextFill(Color.BLACK);

        if(msg.length()>replyMessage.length()){
            if(msg.length()*16<chatPane.getWidth()/4*3)
                reply.setMinWidth(msg.length()*16);
            else
                reply.setMinWidth(chatPane.getWidth()/4*3);
                //reply.setMinWidth(msg.length()*16);
                label.setMaxWidth(chatPane.getWidth()/4*3);
                reply.setMaxWidth(chatPane.getWidth()/4*3);
        }else{
                label.setMaxWidth(chatPane.getWidth()/4*3);
                reply.setMaxWidth(chatPane.getWidth()/4*3);
        }

        if(replyMessage!="") {
            label.setGraphic(reply);
            label.setContentDisplay(ContentDisplay.TOP);
        }
        label.setAccessibleText(String.valueOf(messageID));
        HBox x = new HBox();
        x.getChildren().add(label);
        x.setAlignment(Pos.TOP_LEFT);
        chatPane.getItems().add(x);
        return x;
    }
    public synchronized void addToChat(String msg,int from,int messageID) {
        if (from== currentUser.getUserID()) {
            Thread t2 = new Thread(String.valueOf(addToChatYourMessage(msg,"",messageID)));
            t2.setDaemon(true);
            t2.start();
        } else {
            Thread t = new Thread(String.valueOf(addToChatOtherMessage(msg,"",messageID)));
            t.setDaemon(true);
            t.start();
        }
    }
    @FXML
    public void setUsernameLabel() {
        this.usernameLabel.setText(currentUser.getUserFirstName()+"  "+currentUser.getUserLastName());
    }

    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }

    public void sendMethod(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            sendButtonAction();
        }
    }

    //MESSAGES


    @FXML
    public void handleDelete(){
        if (friendsTableView.getSelectionModel().getSelectedItem() != null) {
            try {
                int friendshipID = modelFriend.get(friendsTableView.getSelectionModel().getSelectedIndex()).getFriendshipID();
                networkService.removeFriendship(friendshipID);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Stergere prieten","Prieten sters cu succes!");
                initModel();
            } catch ( RepoException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "Nu ati selectat un utilizator!");
    }

    @FXML
    private void handleSearchFriend(){
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("search-friend.fxml"));

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Search Friend");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            SearchFriendController searchFriendController = loader.getController();
            searchFriendController.setServices(networkService,currentUser,dialogStage,userService);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onButtonAcceptClick() {
        if (requestsTableView.getSelectionModel().getSelectedItem() != null) {
            try {
                RequestDTO requestDTO = modelRequests.get(requestsTableView.getSelectionModel().getSelectedIndex());
                networkService.respondToRequest(currentUser.getUserID(), requestDTO.getUserID(), 1);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Accept request","Request accepted successfully!");
                initModel();
            } catch (RepoException | ValidException | ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "No selected request!");
    }

    @FXML
    private void onButtonRejectClick() {
        if (requestsTableView.getSelectionModel().getSelectedItem() != null) {
            try {
                RequestDTO requestDTO = modelRequests.get(requestsTableView.getSelectionModel().getSelectedIndex());
                networkService.respondToRequest(currentUser.getUserID(), requestDTO.getUserID(), 2);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Reject request","Request rejected successfully!");
                initModel();
            } catch (RepoException | ValidException | ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "No selected request!");
    }

    @FXML
    private void onButtonCancelRequestClick() {
        if (sentRequestsTableView.getSelectionModel().getSelectedItem() != null) {
            try {
                RequestDTO requestDTO = modelSentRequests.get(sentRequestsTableView.getSelectionModel().getSelectedIndex());
                networkService.removeRequest(requestDTO.getRequestID());
                if (requestDTO.getStatus() == FriendStatus.PENDING)
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sent request","Request canceled successfully!");
                else
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sent request","Request removed successfully!");
                initModel();
            } catch (RepoException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "No selected request!");
    }

    @FXML
    private void onButtonLogOutClick() {
        try {
            Stage parentStage = (Stage)buttonLogOut.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("login-view.fxml"));

            Scene scene = new Scene(loader.load(), 300, 500);

            Stage stage = new Stage();
            stage.setTitle("Log in / Register");
            stage.setScene(scene);

            LoginController loginController = loader.getController();
            loginController.setService(userService, networkService,messageService);
            stage.show();
            parentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onButtonSeeAllFriendsClick(){
        tabPane.getSelectionModel().select(1);
    }

    @FXML
    private void onButtonSeeAllRequests(){
        tabPane.getSelectionModel().select(2);
    }

    @FXML
    private void onButtonSeeAllSentRequests(){
        tabPane.getSelectionModel().select(3);
    }
    @FXML
    private void onButtonSeeAllMessages(){
        tabPane.getSelectionModel().select(4);
    }
    @FXML
    public void handleDeletePage(){
        if (friendsTableView1.getSelectionModel().getSelectedItem() != null) {
            try {
                int friendshipID = modelFriend.get(friendsTableView1.getSelectionModel().getSelectedIndex()).getFriendshipID();
                networkService.removeFriendship(friendshipID);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Stergere prieten","Prieten sters cu succes!");
                initModel();
            } catch ( RepoException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "Nu ati selectat un utilizator!");
    }

    @FXML
    private void onPageButtonAcceptClick() {
        if (requestsTableView1.getSelectionModel().getSelectedItem() != null) {
            try {
                RequestDTO requestDTO = modelRequests.get(requestsTableView1.getSelectionModel().getSelectedIndex());
                networkService.respondToRequest(currentUser.getUserID(), requestDTO.getUserID(), 1);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Accept request","Request accepted successfully!");
                initModel();
            } catch (RepoException | ValidException | ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "No selected request!");
    }

    @FXML
    private void onPageButtonRejectClick() {
        if (requestsTableView1.getSelectionModel().getSelectedItem() != null) {
            try {
                RequestDTO requestDTO = modelRequests.get(requestsTableView1.getSelectionModel().getSelectedIndex());
                networkService.respondToRequest(currentUser.getUserID(), requestDTO.getUserID(), 2);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Reject request","Request rejected successfully!");
                initModel();
            } catch (RepoException | ValidException | ServiceException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "No selected request!");
    }

    @FXML
    private void onPageButtonCancelRequestClick() {
        if (sentRequestsTableView1.getSelectionModel().getSelectedItem() != null) {
            try {
                RequestDTO requestDTO = modelSentRequests.get(sentRequestsTableView1.getSelectionModel().getSelectedIndex());
                networkService.removeRequest(requestDTO.getRequestID());
                if (requestDTO.getStatus() == FriendStatus.PENDING)
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sent request","Request canceled successfully!");
                else
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Sent request","Request removed successfully!");
                initModel();
            } catch (RepoException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        }
        else
            MessageAlert.showErrorMessage(null, "No selected request!");
    }

    @Override
    public void update() {
        initModelFriend();
        initModelRequests();
        initModelSentRequests();
    }
}
