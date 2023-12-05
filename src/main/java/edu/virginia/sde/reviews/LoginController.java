package edu.virginia.sde.reviews;

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

    protected String username;
    protected String password;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void handleLogin(ActionEvent event) throws IOException, SQLException {
        // Capture the username and password when the login button is clicked
        username = username_text_box.getText();
        password = password_text_box.getText();



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
}
