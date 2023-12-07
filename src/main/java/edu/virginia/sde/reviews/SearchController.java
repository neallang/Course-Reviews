package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
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

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    private int courseID;


    @FXML
    private TableColumn<Course,String> subjectCol;
    @FXML
    private TableColumn<Course,String> numCol;
    @FXML
    private TableColumn<Course,String> titleCol;
    @FXML
    private TableColumn<Course,Double> ratingCol;
    @FXML
    private TextField subjectSearch;
    @FXML
    private TextField courseNumSearch;
    @FXML
    private TextField courseTitleSearch;
    @FXML
    private TextField subjectAdd;
    @FXML
    private TextField courseNumAdd;
    @FXML
    private TextField courseTitleAdd;
    @FXML
    private TableView<Course> displayCourses;
    @FXML
    private Button searchButton;
    @FXML
    private Button addButton;
    @FXML
    private Label courseExistsLabel;

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

    public void switchToLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/login.fxml").toURI().toURL());

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        scene = new Scene(root);
        stage.setTitle("Log In");
        stage.setScene(scene);
        stage.show();
    }
    CourseIDSingleton currentCourseID = CourseIDSingleton.getInstance();

    public void initialize() throws IOException, SQLException{
        databaseDriver.connect();
        ArrayList<Course> courseArrayList = databaseDriver.getAllCourses();
        // Reference for populating table:
        // https://stackoverflow.com/questions/11180884/how-to-populate-a-tableview-that-is-defined-in-an-fxml-file-that-is-designed-in
        ObservableList<Course> observableCourses = FXCollections.observableArrayList(courseArrayList);
        numCol.setCellValueFactory(new PropertyValueFactory<Course, String>("courseNumber"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<Course, String>("department"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Course, String>("title"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Course, Double>("averageCourseRating"));
        ratingCol.setCellFactory(ratings -> new TableCell<Course, Double>(){
            //reference for setting the ratings column:
            //https://stackoverflow.com/questions/46671643/javafx-tableview-displays-null-values
            @Override
            protected void updateItem(Double item, boolean empty){
                super.updateItem(item, empty);
                if(item == null || empty || item == 0.0){
                    setText(null);
                }else{
                    setText(String.format("%.2f", item));
                }
            }

        });
        displayCourses.setItems(observableCourses);
        displayCourses.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Course rowData = row.getItem();
                    try {
                        databaseDriver.connect();
                        setCourseID(databaseDriver.getCourseID(rowData.getTitle()));
                        databaseDriver.disconnect();
                        currentCourseID.setCourseID(courseID);
                        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/course-reviews-final.fxml").toURI().toURL());
                        // Switch to the new scene
                        Stage stage = (Stage) displayCourses.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // Perform actions based on the clicked courseID or rowData
                }
            });
            return row;
        });
        databaseDriver.disconnect();
    }
    public void searchCourses(javafx.event.ActionEvent actionEvent) throws IOException, SQLException{
        String inputDept = subjectSearch.getText().toUpperCase();
        String inputNum = courseNumSearch.getText();
        String inputTitle = courseTitleSearch.getText();
        databaseDriver.connect();
        ArrayList<Course> searchedCourses = new ArrayList<>(databaseDriver.getCoursesBySearch(inputDept,inputNum,inputTitle));
        databaseDriver.disconnect();
        ObservableList<Course> observableCourses = FXCollections.observableArrayList(searchedCourses);
        numCol.setCellValueFactory(new PropertyValueFactory<Course, String>("courseNumber"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<Course, String>("department"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Course, String>("title"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Course, Double>("averageCourseRating"));

        displayCourses.setItems(observableCourses);
    }

    public void addCourseInSearch(javafx.event.ActionEvent actionEvent) throws IOException, SQLException {
        String inputDept = subjectAdd.getText();
        String inputNum = courseNumAdd.getText();
        String inputTitle = courseTitleAdd.getText();
        Course newCourse = new Course(inputDept, inputNum, inputTitle);
        databaseDriver.connect();
        if(databaseDriver.courseAlreadyExists(inputDept, inputTitle)){
            courseExistsLabel.setText("Course already exists.");
        } else if(inputDept.isEmpty() || inputNum.isEmpty() || inputTitle.isEmpty()) {
            courseExistsLabel.setText("Please complete all fields for course name.");
        }else if(inputDept.length() > 4 || inputDept.length() < 2){
            courseExistsLabel.setText("Please input valid department mnemonic.");
        }else if(inputNum.length() != 4 || !isDigits(inputNum)) {
            courseExistsLabel.setText("Please input valid course number.");
        }else if(inputTitle.length()>50){
            courseExistsLabel.setText("Course title must be 50 characters or less.");
        } else {
            databaseDriver.addCourse(newCourse);
            databaseDriver.commit();
            courseExistsLabel.setText("");
        }


        databaseDriver.disconnect();
        initialize();
    }

    public boolean isDigits(String courseNum){
        for(int i = 0; i<courseNum.length(); i++){
            if(!Character.isDigit(courseNum.charAt(i))){
                return false;
            }
        }
        return true;
    }

}

