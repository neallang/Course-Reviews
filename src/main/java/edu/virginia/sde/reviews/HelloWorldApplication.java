package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class HelloWorldApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("course-reviews-final.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        DatabaseDriver databaseDriver = new DatabaseDriver("appDatabase.sqlite");
        databaseDriver.connect();
        databaseDriver.createTables();
        if(databaseDriver.checkEmpty()){
            addData(databaseDriver);
        }
        databaseDriver.disconnect();
    }

    public void addData(DatabaseDriver databaseDriver) throws SQLException {
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        Course course1 = new Course("CS", "3140", "Software Development Essentials");
        Course course2 = new Course("CS", "2100", "Data Structures and Algorithms I");
        Course course3 = new Course("ANTH", "2130", "Languages of the World");
        Course course4 = new Course("JAPN", "1010", "First Year Japanese");
        User user1 = new User("Stanley", "password");
        User user2 = new User("Neal", "password");
        User user3 = new User("Danny", "password");
        User user4 = new User("Connolly", "password");
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courses.add(course4);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        for(User user : users){
            databaseDriver.addUser(user);
        }
        for(Course course : courses){
            databaseDriver.addCourse(course);
        }
        databaseDriver.commit();



    }
}
