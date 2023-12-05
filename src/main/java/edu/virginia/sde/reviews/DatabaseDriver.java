package edu.virginia.sde.reviews;

import java.lang.module.Configuration;
import java.sql.*;
import java.util.*;

public class DatabaseDriver {

    private final String sqliteFilename;
    private Connection connection;


    public DatabaseDriver (String sqlListDatabaseFilename) {
        this.sqliteFilename = sqlListDatabaseFilename;
    }

    /**
     * Connect to a SQLite Database. This turns out Foreign Key enforcement, and disables auto-commits
     * @throws SQLException
     */
    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The connection is already opened");
        }
        if(Objects.equals(sqliteFilename, "dbc:sqlite::memory:")){
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        } else {
            connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilename);
        }

        //the next line enables foreign key enforcement - do not delete/comment out
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        //the next line disables auto-commit - do not delete/comment out
        connection.setAutoCommit(false);
    }

    /**
     * Commit all changes since the connection was opened OR since the last commit/rollback
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Rollback to the last commit, or when the connection was opened
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Ends the connection to the database
     */
    public void disconnect() throws SQLException {
        connection.close();
    }

    public void createTables() throws SQLException {
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        createUsersTable();
        createCoursesTable();
        createReviewsTable();
    }

    public void createUsersTable() throws SQLException {
        String usersTableString;
        usersTableString = """
                create table if not exists Users
                (
                    ID        INTEGER not null
                        primary key autoincrement,
                    Username TEXT    not null UNIQUE,
                    Password  TEXT    not null
                );
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(usersTableString);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void createCoursesTable() throws SQLException{
        String coursesTableString;
        coursesTableString = """
                create table if not exists Courses
                (
                    ID        INTEGER not null
                        primary key autoincrement,
                    Department TEXT    not null,
                    CourseNumber  TEXT    not null,
                    Title   TEXT    not null UNIQUE
                );
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(coursesTableString);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void createReviewsTable() throws SQLException{
        String reviewsTableString;
        reviewsTableString = """
                create table if not exists Reviews
                (
                    ID        INTEGER not null
                        primary key autoincrement,
                    UserID INTEGER    not null,
                    CourseID  INTEGER    not null,
                    ReviewText   TEXT    not null,
                    Rating    INTEGER    not null,
                    FOREIGN KEY (UserID) references Users(ID)
                        on delete cascade,
                    FOREIGN KEY (CourseID) references Courses(ID)
                        on delete cascade
                );
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(reviewsTableString);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }


    public void addUser(User user) throws SQLException{
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Users(Username, Password) values (?, ?)");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            rollback(); //rolls back any changes before the Exception was thrown
            throw e; //still throws the SQLException
        }

    }

    public void addCourse(Course course) throws SQLException{
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Courses(Department, CourseNumber, Title) values (?, ?, ?)");
            statement.setString(1, course.getDepartment());
            statement.setString(2, course.getCourseNumber());
            statement.setString(3, course.getTitle());
            statement.executeUpdate();
        } catch (SQLException e) {
            rollback(); //rolls back any changes before the Exception was thrown
            throw e; //still throws the SQLException
        }

    }

    public void addReview(Review review) throws SQLException{
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Reviews(UserID, CourseID, ReviewText, Rating) values (?, ?, ?, ?)");
            statement.setInt(1, review.getUserID());
            statement.setInt(2, review.getCourseID());
            statement.setString(3, review.getReviewText());
            statement.setInt(4, review.getRating());
            statement.executeUpdate();
        } catch (SQLException e) {
            rollback(); //rolls back any changes before the Exception was thrown
            throw e; //still throws the SQLException
        }

    }




    public void clearTables() throws SQLException {
        //Got help with statements from ChatGPT - Prompt: what is the difference between a statement and a prepared statement in jdbc
        Statement clearUsers = connection.createStatement();
        String clearUsersString = "DELETE FROM Users";
        String clearUsersSequence = "delete from sqlite_sequence where name='Users'";
        clearUsers.execute(clearUsersString);
        clearUsers.execute(clearUsersSequence);
        clearUsers.close();

        Statement clearCourses = connection.createStatement();
        String clearCourseString = "DELETE FROM Courses";
        String clearCoursesSequence = "delete from sqlite_sequence where name='Courses'";
        clearCourses.execute(clearCourseString);
        clearCourses.execute(clearCoursesSequence);
        clearCourses.close();

        Statement clearReviews = connection.createStatement();
        String clearReviewsString = "DELETE FROM Reviews";
        String clearReviewsSequence = "delete from sqlite_sequence where name='Reviews'";
        clearReviews.execute(clearReviewsString);
        clearReviews.execute(clearReviewsSequence);
        clearReviews.close();


    }



}
