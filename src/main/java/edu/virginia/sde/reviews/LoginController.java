package edu.virginia.sde.reviews;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    DatabaseDriver databaseDriver = new DatabaseDriver("appDatabase.sqlite");

    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField password_text_box;

    @FXML
    private TextField username_text_box;

    @FXML Button close_button;

    private String username;


    private String password;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private User activeUser;


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public void setActiveUser(User user){
        this.activeUser = user;
    }
    public User getActiveUser(){
        return  activeUser;
    }

    UsernameSingleton currentUsername = UsernameSingleton.getInstance();
    @FXML
    void handleLogin(ActionEvent event) throws IOException, SQLException {
        // Capture the username and password when the login button is clicked
        username = username_text_box.getText();
        password = password_text_box.getText();

        setActiveUser(new User(username, password));
        currentUsername.setUsername(username);



        // Now you can perform the login logic, such as validation or authentication
        // For example, you might check if the username and password are valid
        if (isValidLogin()) {
            messageLabel.setText("Login successful!");
            switchToSearch(event);

        } else {
            messageLabel.setText("Invalid username or password.");
        }
    }

    private boolean isValidLogin() throws SQLException {
        databaseDriver.connect();
        Boolean isValid = databaseDriver.autheticateUser(username, password);
        databaseDriver.disconnect();


        return !username.isEmpty() && !password.isEmpty() && isValid;
    }

    public void switchToSearch(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/search-screen.fxml").toURI().toURL());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        SearchController searchController = new SearchController();
        searchController.setActiveUser(getActiveUser());

        MyReviewsController myReviewsController = new MyReviewsController();
        myReviewsController.setActiveUser(getActiveUser());

        scene = new Scene(root);
        stage.setTitle("Course Search");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRegister(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/register.fxml").toURI().toURL());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setTitle("Course Search");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleCloseButton(ActionEvent event) {
        // Close the application when the close button is pressed
        Platform.exit();
    }



}
