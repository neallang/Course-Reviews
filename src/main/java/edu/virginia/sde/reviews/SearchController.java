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
import java.sql.*;
import javafx.collections.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;




public class SearchController {
    @FXML
    private Label messageLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableColumn<?,?> subjectCol;
    @FXML
    private TableColumn<?,?> numCol;
    @FXML
    private TableColumn<?,?> titleCol;
    @FXML
    private TableColumn<?,?> ratingCol;
    @FXML
    private TableColumn<?,?> reviewsCol;
    @FXML
    private TableColumn<?,?> addCol;
    @FXML
    private TextField subject;
    @FXML
    private TextField courseNum;
    @FXML
    private TextField courseTitle;
    @FXML
    private TableView displayCourses;



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
    /*
    public void searchCourses(javafx.event.ActionEvent actionEvent) throws IOException {
        try{
            Connection con = ;
            fetchCourses(con);
            con.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
*/

    private void fetchCourses(Connection con) throws SQLException{

        ObservableList<Course> courses = FXCollections.observableArrayList();

        // Mocking courses (replace this with your actual mock data)
        Course course1 = new Course("Computer Science", "CS101", "Introduction to Programming");
        Course course2 = new Course("Mathematics", "MATH202", "Calculus II");
        Course course3 = new Course("Physics", "PHYS303", "Modern Physics");

        // Adding mock courses to the ObservableList
        courses.addAll(course1, course2, course3);

        displayCourses.getItems().clear();
        displayCourses.setItems(courses);

        String query = "SELECT * FROM course WHERE department = ? AND courseNumber = ? LIKE title = ?";
        String inputDept = subject.getText();
        String inputNum = courseNum.getText();
        String inputTitle = courseTitle.getText();
        try (PreparedStatement findCourses = con.prepareStatement(query)){
            findCourses.setString(1, inputDept);
            findCourses.setString(2, inputNum);
            findCourses.setString(3, "%" + inputTitle + "%");
            ResultSet rs = findCourses.executeQuery();
            while(rs.next()){
                Course matchingCourse = new Course(rs.getString("department"),
                        rs.getString("courseNum"), rs.getString("title"));
                courses.add(matchingCourse);
            }

        }
        displayCourses.getItems().clear();
        displayCourses.setItems(courses);
    }


}
