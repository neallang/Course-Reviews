package edu.virginia.sde.reviews;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;


public class CourseReviewsController {
    @FXML
    private Button back_button;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private RadioButton button_one, button_two, button_three, button_four, button_five;
    private Timestamp timestamp;


    public void getReviewNumber(ActionEvent event){
        if (button_one.isSelected()){
            button_one.setText("button one selected");  //will change this to update the review value
        }
        else if (button_two.isSelected()){
            button_two.setText("button two selected");  //will change this to update the review value
        }
        else if (button_three.isSelected()){
            button_three.setText("button three selected");  //will change this to update the review value
        }
        else if (button_four.isSelected()){
            button_four.setText("button four selected");  //will change this to update the review value
        }
        else if (button_five.isSelected()){
            button_five.setText("button five selected");  //will change this to update the review value
        }
    }


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

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/login.fxml").toURI().toURL());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }

    @FXML private TableView<Review> tableView;
    @FXML private TableColumn<Review, Integer> ratingColumn;    //rating
    @FXML private TableColumn<Review, String> commentColumn;      //reviewText
    @FXML private TableColumn<Review, Timestamp> dateTimeColumn;     //timeStamp
    @FXML private Label course_title_label;

    CourseIDSingleton currentCourseID = CourseIDSingleton.getInstance();
    public void initialize() throws IOException, SQLException {
        course_title_label.setText(currentCourseID.toString());



    }






}
