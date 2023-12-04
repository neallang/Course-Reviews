package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;



public class CourseReviewsController {
    @FXML
    private Button back_button;
    @FXML
    Slider rating_slider;
    private Stage stage;
    private Scene scene;
    private Parent root;


//    public void goBack() {
//        back_button.setText("You pressed the button!");
//    }
    @FXML
    public void switchToSearch(ActionEvent event) throws IOException {
        try {
            root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/search-screen.fxml").toURI().toURL());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            scene = new Scene(root);
            stage.setTitle("Course Search");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace(System.out);
        }
    }

    public void sliderStuff(){
        rating_slider.setBlockIncrement(1);
        rating_slider.setMax(5);
        rating_slider.setShowTickLabels(true);
        //rating_slider.increment();


    }


}
