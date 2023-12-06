package edu.virginia.sde.reviews;

import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;


public class MyReviewsController {
    @FXML
    private Button back_button;
    @FXML
    Slider rating_slider;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private User activeUser;
    private int courseID;

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setActiveUser(User user){
        this.activeUser = user;
    }
    public User getActiveUser(){
        return this.activeUser;
    }



    @FXML
    private TableColumn<MyReview,String> col_comment;

    @FXML
    private TableColumn<MyReview,Timestamp> col_date;

    @FXML
    private TableColumn<MyReview,String> col_courseNum;

    @FXML
    private TableColumn<MyReview,Integer> col_rating;

    @FXML
    private TableColumn<MyReview,String> col_department;

    @FXML
    private TableColumn<MyReview, Integer> col_courseID;

    @FXML
    private TableView<MyReview> myReviews;

    DatabaseDriver databaseDriver = new DatabaseDriver("appDatabase.sqlite");


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
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    UsernameSingleton currentUsername = UsernameSingleton.getInstance();
    CourseIDSingleton currentCourseID = CourseIDSingleton.getInstance();
    public void initialize() throws IOException, SQLException{

        String username = currentUsername.getUsername();

        databaseDriver.connect();
        int userID = databaseDriver.getUserID(username);


        ArrayList<MyReview> myReviewList = databaseDriver.getMyReviews(userID);
        databaseDriver.disconnect();
        ObservableList<MyReview> observableReviews = FXCollections.observableArrayList(myReviewList);
        col_department.setCellValueFactory(new PropertyValueFactory<MyReview, String>("department"));
        col_courseNum.setCellValueFactory(new PropertyValueFactory<MyReview, String>("courseNumber"));
        col_rating.setCellValueFactory(new PropertyValueFactory<MyReview, Integer>("rating"));
        col_comment.setCellValueFactory(new PropertyValueFactory<MyReview, String>("reviewText"));
        col_date.setCellValueFactory(new PropertyValueFactory<MyReview, Timestamp>("timestamp"));
        col_courseID.setCellValueFactory(new PropertyValueFactory<MyReview, Integer>("courseID"));


        myReviews.setItems(observableReviews);

        myReviews.setRowFactory(tv -> {
            TableRow<MyReview> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    MyReview rowData = row.getItem();
                    setCourseID(rowData.getCourseID());
                    currentCourseID.setCourseID(courseID);
                    try {
                        root = FXMLLoader.load(new File("src/main/resources/edu/virginia/sde/reviews/course-reviews-final.fxml").toURI().toURL());
                        // Switch to the new scene
                        Stage stage = (Stage) myReviews.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Perform actions based on the clicked courseID or rowData
                }
            });
            return row;
        });
    }


}