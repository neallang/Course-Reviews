package edu.virginia.sde.reviews;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
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
import java.net.MalformedURLException;
import java.sql.*;
import javafx.collections.*;

import java.util.*;




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

    DatabaseDriver databaseDriver = new DatabaseDriver("appDatabase.sqlite");

    public void handleCourseIDButton() {
        messageLabel.setText("You pressed the button!");
    }
    public void handleCourseNameButton() {
        messageLabel.setText("You pressed the button!");
    }
    public void handleCourseMnemonicButton() {
        messageLabel.setText("You pressed the button!");
    }

//    private Button viewButton() throws IOException{
//        Button button = new Button("View");
//        button.setOnAction(event -> {
//            try{
//                switchToMyReviews(event);
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//        });
//    }
    public void switchToMyReviews(javafx.event.ActionEvent actionEvent) throws IOException {
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
        databaseDriver.disconnect();
        ObservableList<Course> observableCourses = FXCollections.observableArrayList(courseArrayList);
        numCol.setCellValueFactory(new PropertyValueFactory<Course, String>("courseNumber"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<Course, String>("department"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Course, String>("title"));
//        reviewsCol.setCellFactory(view -> new TableCell<Button, Void>(){
//            private Button viewButton() throws IOException{
//                Button button = new Button("View");
//                button.setOnAction(event -> {
//                    try{
//                        switchToMyReviews(event);
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                });
//            }
//        });

        displayCourses.setItems(observableCourses);
    }

//    @FXML
//    void searchCourses(javafx.event.ActionEvent actionEvent) throws IOException, SQLException {
//
//        databaseDriver.connect();
//        //databaseDriver.getAllCourses();
//        ArrayList<Course> courseArrayList = databaseDriver.getAllCourses();
//        ObservableList<Course> observableCourses = FXCollections.observableArrayList(courseArrayList);
//        System.out.println("This is in the observableList " + observableCourses.get(0).getTitle());
//        databaseDriver.disconnect();
//        numCol.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
//
//        displayCourses.setItems(observableCourses);
//        List<Course> courseList = courseArrayList;
//        ObservableList<Course> courses = FXCollections.observableList(courseList);
//        displayCourses.setItems(courses);
        //displayCourses.getItems().clear();
//
//        databaseDriver.connect();
//
//        ArrayList<Course> databaseCourses = databaseDriver.getAllCourses();
//        databaseDriver.disconnect();
//        String query = "SELECT * FROM course";
//        String inputDept = subject.getText();
//        String inputNum = courseNum.getText();
//        String inputTitle = courseTitle.getText();
//        for (Course course: databaseCourses){
//            if (course.getCourseNumber().equalsIgnoreCase(inputNum) || course.getDepartment().equalsIgnoreCase(inputDept)
//            || course.getTitle().equalsIgnoreCase(inputTitle)){
//
//                courses.add(course);
//            }
//        }
//        displayCourses.setItems(courses);

//        }

}
