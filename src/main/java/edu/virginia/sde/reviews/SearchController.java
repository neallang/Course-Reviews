package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


import java.sql.*;
import javafx.collections.*;

import java.util.*;


import java.net.MalformedURLException;
import java.sql.SQLException;



public class SearchController {
    @FXML
    private Label messageLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableColumn<Course,String> subjectCol;
    @FXML
    private TableColumn<Course,String> numCol;
    @FXML
    private TableColumn<Course,String> titleCol;
    @FXML
    private TableColumn<?,?> ratingCol;
    @FXML
    private TableColumn<Button,Void> reviewsCol;
    @FXML
    private TableColumn<?,Button> addCol;
    @FXML
    private TextField subject;
    @FXML
    private TextField courseNum;
    @FXML
    private TextField courseTitle;
    @FXML
    private TableView<Course> displayCourses;

    //MyReviewsController myReviewsController = new MyReviewsController();


    private User activeUser;
    public void setActiveUser(User user){
        this.activeUser = user;
    }
    public User getActiveUser(){
        return this.activeUser;
    }

    DatabaseDriver databaseDriver = new DatabaseDriver("appDatabase.sqlite");

    public void switchToMyReviews(javafx.event.ActionEvent actionEvent) throws IOException, SQLException {
        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/my-reviews.fxml").toURI().toURL());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("My Reviews");
        stage.setScene(scene);

        stage.show();

    }

    public void switchToCourseReview(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/course-reviews-final.fxml").toURI().toURL());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setTitle("Course");
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

    public void initialize() throws IOException, SQLException{
        databaseDriver.connect();
        ArrayList<Course> courseArrayList = databaseDriver.getAllCourses();

        ObservableList<Course> observableCourses = FXCollections.observableArrayList(courseArrayList);
        numCol.setCellValueFactory(new PropertyValueFactory<Course, String>("courseNumber"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<Course, String>("department"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Course, String>("title"));

        displayCourses.setItems(observableCourses);
        displayCourses.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Course rowData = row.getItem();
                    int courseID = 0; // Assuming there's a method to get courseID
                    try {
                        courseID = databaseDriver.getCourseID(rowData.getTitle());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Clicked on CourseID: " + courseID);
                    // Perform actions based on the clicked courseID or rowData
                }
            });
            return row;
        });
        databaseDriver.disconnect();
    }


}
