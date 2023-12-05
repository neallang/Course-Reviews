package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;


public class SearchController {
    @FXML
    private Label messageLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void handleCourseIDButton() {
        messageLabel.setText("You pressed the button!");
    }
    public void handleCourseNameButton() {
        messageLabel.setText("You pressed the button!");
    }
    public void handleCourseMnemonicButton() {
        messageLabel.setText("You pressed the button!");
    }


    public void switchToMyReviews(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/my-reviews.fxml").toURI().toURL());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setTitle("My Reviews");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/login.fxml").toURI().toURL());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }

}
