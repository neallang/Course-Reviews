package edu.virginia.sde.reviews;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToReviews(ActionEvent event) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-reviews-final.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Course Review");
        stage.setScene(scene);
        stage.show();
    }
    public void switchToSearch(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("search-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Course Review");
        stage.setScene(scene);
        stage.show();
    }
}
