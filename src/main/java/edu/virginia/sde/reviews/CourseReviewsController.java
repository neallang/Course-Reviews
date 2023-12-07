package edu.virginia.sde.reviews;

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
import javafx.scene.text.Text;


import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class CourseReviewsController {
    @FXML
    private Button back_button;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private RadioButton button_one, button_two, button_three, button_four, button_five;
    private Timestamp timestamp;
    private int courseID;

    private int userID;
    private boolean userReviewAlreadyExists;
    UsernameSingleton currentUsername = UsernameSingleton.getInstance();
    int rating = -1;
    String comment;
    @FXML Label course_title_label;
    CurrentReviewSingleton currentReviewSingleton = CurrentReviewSingleton.getInstance();
    @FXML TextField comment_text_box;
    @FXML Label null_rating_label;

    @FXML private TableView<Review> tableView;
    @FXML private TableColumn<Review, Integer> ratingColumn;    //rating
    @FXML private TableColumn<Review, String> commentColumn;      //reviewText
    @FXML private TableColumn<Review, Timestamp> dateTimeColumn;     //timeStamp
    @FXML private Label average_review_double;
    @FXML private Label messageLabel;
    DatabaseDriver databaseDriver = new DatabaseDriver("appDatabase.sqlite");



    public void setRatingNumber(ActionEvent event){
        if (button_one.isSelected() ){
            rating = 1;
        }
        else if (button_two.isSelected()){
            rating = 2;
        }
        else if (button_three.isSelected()){
            rating = 3;
        }
        else if (button_four.isSelected()){
            rating = 4;
        }
        else if (button_five.isSelected()){
            rating = 5;
        }
    }

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

    public void initialize() throws IOException, SQLException {
        CourseIDSingleton courseIDSingleton = CourseIDSingleton.getInstance();
        courseID = courseIDSingleton.getCourseID();
        databaseDriver.connect();
        course_title_label.setText(databaseDriver.getCourseName(courseID));
        ArrayList<Review> reviewArrayList= databaseDriver.getCourseReviews(courseID);
        double average = databaseDriver.getAverageCourseRating(courseID);
        userID = databaseDriver.getUserID(currentUsername.getUsername());
        userReviewAlreadyExists = databaseDriver.userReviewExists(currentUsername.getUsername(), courseID);
        if(userReviewAlreadyExists){

            int userRating = databaseDriver.getUserRating(userID, courseID);
            if (userRating == 1){
                button_one.setSelected(true);
                rating = 1;
            }
            else if (userRating == 2){
                button_two.setSelected(true);
                rating = 2;
            }
            else if (userRating == 3){
                button_three.setSelected(true);
                rating = 3;
            }
            else if (userRating == 4){
                button_four.setSelected(true);
                rating = 4;
            }
            else if (userRating == 5){
                button_five.setSelected(true);
                rating = 5;
            }

            comment_text_box.setText(databaseDriver.getComment(userID, courseID));
        }
        databaseDriver.disconnect();

        ObservableList<Review> observableReviewList = FXCollections.observableArrayList(reviewArrayList);
        ratingColumn.setCellValueFactory(new PropertyValueFactory<Review, Integer>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<Review, String>("reviewText"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Review, Timestamp>("timeStamp"));

        tableView.setItems(observableReviewList);

        //AI Agent: CHATGPT
        //Prompt: How can I round a double to 2 decimal places in Java?
        DecimalFormat df = new DecimalFormat("#.##");
        String roundedAverage = df.format(average);
        average_review_double.setText(roundedAverage);


    }



    public void save(javafx.event.ActionEvent actionEvent) throws SQLException, IOException {

        String comment = comment_text_box.getText();
        timestamp = new Timestamp(java.lang.System.currentTimeMillis());
        databaseDriver.connect();
        if(rating == -1){
            messageLabel.setText("Please provide a rating.");
            databaseDriver.disconnect();
        } else if(userReviewAlreadyExists){
            databaseDriver.updateReview(comment, rating, userID, courseID);
            databaseDriver.commit();
            messageLabel.setText("");
            databaseDriver.disconnect();
        } else {
            Review review = new Review(userID, courseID, comment, rating, timestamp);
            databaseDriver.addReview(review);
            databaseDriver.commit();
            messageLabel.setText("");
            databaseDriver.disconnect();
        }


        initialize();


    }

    public void delete(javafx.event.ActionEvent actionEvent) throws SQLException, IOException{
        databaseDriver.connect();
        if(userReviewAlreadyExists){
            databaseDriver.deleteReview(userID, courseID);
            databaseDriver.commit();

        }
        databaseDriver.disconnect();
        initialize();
    }


    }

