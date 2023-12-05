package edu.virginia.sde.reviews;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class DatabaseDriverTest {
    //DatabaseDriver databaseDriver = new DatabaseDriver("dbc:sqlite::memory:");
    User testUser = new User("Stan", "password");
    DatabaseDriver databaseDriver = new DatabaseDriver("test.sqlite");

    @Test
    void createTables() throws SQLException{
        databaseDriver.connect();
        databaseDriver.createTables();

        databaseDriver.commit();
        databaseDriver.disconnect();

    }

    @Test
    void addData() throws SQLException{
        databaseDriver.connect();
        User user = new User("stan", "password");
        databaseDriver.addUser(user);
        Course course = new Course("CS", "3140", "SDE" );
        Course course2 = new Course("ANTH", "4300", "New Class");
        databaseDriver.addCourse(course);
        databaseDriver.addCourse(course2);
        Timestamp timestamp = new Timestamp(2022);
        Review review = new Review(1, 1, "I liked this class!", 4, timestamp);
        databaseDriver.addReview(review);
        databaseDriver.commit();
        databaseDriver.disconnect();
    }

    @Test
    void getCoursesBy() throws SQLException{
        databaseDriver.connect();
        ArrayList<Course> query = databaseDriver.getCoursesByFilter("CourseNumber", "3140");
        Course course = new Course("CS", "3140", "SDE" );
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(course);
        System.out.println(courses.get(0).getDepartment());
        System.out.println(query.get(0).getDepartment());
        System.out.println(courses.get(0).getCourseNumber());
        System.out.println(query.get(0).getCourseNumber());
        System.out.println(courses.get(0).getTitle());
        System.out.println(query.get(0).getTitle());
        //assertEquals(course, query.get(0));
    }

    @Test
    void authenticateUser() throws SQLException{
        databaseDriver.connect();
        boolean bool = databaseDriver.autheticateUser("stan", "password");
        assertTrue(bool);
    }

    @Test
    void destroyTables() throws SQLException{
        databaseDriver.connect();
        databaseDriver.clearTables();
        databaseDriver.commit();
        databaseDriver.disconnect();
    }
//    @BeforeEach
//    void setUp() throws SQLException{
//        databaseDriver.connect();
//        databaseDriver.createTables();
//        databaseDriver.commit();
//    }
//
//    @Test
//    void addUser() throws SQLException{
//        databaseDriver.addUser(testUser);
//
//    }
//
//    @AfterEach
//    void clear() throws SQLException{
//        databaseDriver.clearTables();
//        databaseDriver.commit();
//        databaseDriver.disconnect();
//    }
}