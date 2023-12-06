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
        User user2 = new User("neal", "something");
        databaseDriver.addUser(user);
        databaseDriver.addUser(user2);
        Course course = new Course("CS", "3140", "SDE");
        Course course2 = new Course("ANTH", "4300", "New Class");
        databaseDriver.addCourse(course);
        databaseDriver.addCourse(course2);
        Timestamp timestamp = new Timestamp(java.lang.System.currentTimeMillis());
        System.out.println(timestamp.toString());
        Review review = new Review(1, 1, "I liked this class!", 4, timestamp);
        Review review2 = new Review(1,2,"Meh", 2, timestamp);
        databaseDriver.addReview(review);
        databaseDriver.addReview(review2);
        databaseDriver.commit();
        databaseDriver.disconnect();
    }

    @Test
    void getCoursesBy() throws SQLException{
        databaseDriver.connect();
        ArrayList<Course> query = databaseDriver.getCoursesByFilter("CourseNumber", "3140");
        Course course = new Course("CS", "3140", "SDE", 0.0);
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
        databaseDriver.disconnect();
        assertTrue(bool);
    }

    @Test
    void userAlreadyExists() throws SQLException{
        databaseDriver.connect();
        boolean bool = databaseDriver.userAlreadyExists("stan   ");
        databaseDriver.disconnect();
        assertTrue(bool);
    }

    @Test
    void getMyReviews() throws SQLException{
        databaseDriver.connect();
        ArrayList<MyReview> myReviews = databaseDriver.getMyReviews(1);
        System.out.println(myReviews);
        databaseDriver.disconnect();
    }

    @Test
    void addReview_changesAverage() throws SQLException{
        databaseDriver.connect();
        Course course = databaseDriver.getCourseByID(1);
        assertEquals(4, course.getAverageCourseRating());
        Review review = new Review(2, 1, "Glad to be done.", 2, new Timestamp(java.lang.System.currentTimeMillis()));
        databaseDriver.addReview(review);
        Course courseAgain = databaseDriver.getCourseByID(1);
        assertEquals(3.0, courseAgain.getAverageCourseRating());
        databaseDriver.commit();
        databaseDriver.disconnect();
    }

    @Test
    void getUserID() throws SQLException{
        databaseDriver.connect();
        int ID = databaseDriver.getUserID("Neal");
        assertEquals(2, ID);
    }

    @Test
    void destroyTables() throws SQLException{
        databaseDriver.connect();
        databaseDriver.clearTables();
        databaseDriver.commit();
        databaseDriver.disconnect();
    }

    @Test
    void dropReviewsTable() throws  SQLException{
        databaseDriver.connect();
        databaseDriver.dropReviewsTable();
        databaseDriver.commit();
        databaseDriver.disconnect();
    }

    @Test
    void dropCoursesTable() throws SQLException{
        databaseDriver.connect();
        databaseDriver.dropCoursesTable();;
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