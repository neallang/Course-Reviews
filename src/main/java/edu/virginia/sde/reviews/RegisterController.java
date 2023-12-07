package edu.virginia.sde.reviews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
    DatabaseDriver databaseDriver = new DatabaseDriver("appDatabase.sqlite");

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label blank_label;

    @FXML
    private PasswordField password_text_box;

    @FXML
    private TextField username_text_box;

    @FXML
    Label passwordValidationLabel;

    protected String username;
    protected String password;


    @FXML
    void handleRegister(ActionEvent event) throws IOException, SQLException {
        // Capture the username and password when the login button is clicked
        username = username_text_box.getText();
        password = password_text_box.getText();
        passwordValidationLabel.setText("");
        blank_label.setText("");

        if (password.length() < 8){
            passwordValidationLabel.setText("Password must be at least 8 characters long.");
        }
        if (username.isEmpty() || password.isEmpty()){
            blank_label.setText("Either username or password is not set.");
        } else if (ifUserExists() || password.length() < 8){
            blank_label.setText("Username is already taken.");
        }
        // Now you can perform the login logic, such as validation or authentication
        // For example, you might check if the username and password are valid
        else if (!ifUserExists()) {
            databaseDriver.connect();
            User user = new User(username, password);
            databaseDriver.addUser(user);
            databaseDriver.commit();
            passwordValidationLabel.setText("");
            databaseDriver.disconnect();
            switchToLogin(event);
        } else {
            blank_label.setText("Username is already taken.");
        }
    }

    private boolean ifUserExists() throws SQLException {
        databaseDriver.connect();
        Boolean ifExists= databaseDriver.userAlreadyExists(username);
        databaseDriver.disconnect();

        return ifExists;
    }

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/login.fxml").toURI().toURL());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setTitle("Course Search");
        stage.setScene(scene);
        stage.show();
    }
}
