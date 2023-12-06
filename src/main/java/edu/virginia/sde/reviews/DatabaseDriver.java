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
                    ReviewTime  TIMESTAMP    not null,
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

    public ArrayList<User> getAllUsers() throws SQLException {
        //Got the code for how to get stops from the OCT 26 Lecture example code (Database.java)
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            var Username = resultSet.getString(2);
            var Password = resultSet.getString(3);


            User user = new User(Username, Password);
            users.add(user);
        }
        return users;

    }

    public ArrayList<User> getUsersByFilter(String column, String value) throws SQLException {
        //Got the code for how to get stops from the OCT 26 Lecture example code (Database.java)
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users where " + column + " = " + "\'" + value + "\'");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            var Username = resultSet.getString(2);
            var Password = resultSet.getString(3);


            User user = new User(Username, Password);
            users.add(user);
        }
        return users;

    }

    public int getUserID(String username) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM Users where Username = " + "\'" + username + "\'");
        ResultSet resultSet = statement.executeQuery();
        return resultSet.getInt(1);
    }


    public boolean autheticateUser(String username, String password) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users where Username = " + "\'" + username  + "\'" + " AND " + " Password = " + "\'" + password + "\'");
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public boolean userAlreadyExists(String username) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Users where Username = " + "\'" + username + "\'");
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
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

    public ArrayList<Course> getAllCourses() throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Courses");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            var Department = resultSet.getString(2);
            var CourseNumber = resultSet.getString(3);
            var Title = resultSet.getString(4);


            Course course = new Course(Department, CourseNumber, Title);
            courses.add(course);
        }
        return courses;
    }

    public ArrayList<Course> getCoursesByFilter(String column, String value) throws SQLException {
        //Got the code for how to get stops from the OCT 26 Lecture example code (Database.java)
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Courses where " + column + " = " + "\'" + value + "\'");
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            var Department = resultSet.getString(2);
            var CourseNumber = resultSet.getString(3);
            var Title = resultSet.getString(4);


            Course course = new Course(Department, CourseNumber, Title);
            courses.add(course);
        }
            return courses;

    }


    public void addReview(Review review) throws SQLException{
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Reviews(UserID, CourseID, ReviewText, Rating, ReviewTime) values (?, ?, ?, ?, ?)");
            statement.setInt(1, review.getUserID());
            statement.setInt(2, review.getCourseID());
            statement.setString(3, review.getReviewText());
            statement.setInt(4, review.getRating());
            statement.setTimestamp(5,review.getTimeStamp());
            statement.executeUpdate();
        } catch (SQLException e) {
            rollback(); //rolls back any changes before the Exception was thrown
            throw e; //still throws the SQLException
        }

    }

    public ArrayList<MyReview> getMyReviews(String userID) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("select Reviews.ReviewText, Reviews.Rating, Reviews.ReviewTime, Reviews.UserID, Reviews.CourseID, Courses.CourseNumber, Courses.Department, Courses.ID from Reviews full join Courses on Reviews.CourseID = Courses.ID where Reviews.UserID = " + userID );
        ResultSet resultSet = statement.executeQuery();
        ArrayList<MyReview> myReviews = new ArrayList<>();
        while (resultSet.next()) {
            var ReviewText = resultSet.getString(1);
            var Rating = resultSet.getInt(2);
            var ReviewTime = resultSet.getTimestamp(3);
            var CourseNumber = resultSet.getString(4);
            var Department = resultSet.getString(5);
            var CourseID = resultSet.getInt(6);


            MyReview myReview = new MyReview(ReviewText, Rating, ReviewTime, CourseNumber, Department, CourseID);
            myReviews.add(myReview);
        }
        return myReviews;
    }




    public ArrayList<MyReview> getCourseReviews(String courseID) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("select Reviews.ReviewText, Reviews.Rating, Reviews.ReviewTime, Reviews.UserID, Reviews.CourseID, Courses.CourseNumber, Courses.Department, Courses.ID from Reviews full join Courses on Reviews.CourseID = Courses.ID where Reviews.CourseID = " + courseID );
        ResultSet resultSet = statement.executeQuery();
        ArrayList<MyReview> myReviews = new ArrayList<>();
        while (resultSet.next()) {
            var ReviewText = resultSet.getString(1);
            var Rating = resultSet.getInt(2);
            var ReviewTime = resultSet.getTimestamp(3);
            var CourseNumber = resultSet.getString(4);
            var Department = resultSet.getString(5);
            var CourseID = resultSet.getInt(6);


            MyReview myReview = new MyReview(ReviewText, Rating, ReviewTime, CourseNumber, Department, CourseID);
            myReviews.add(myReview);
        }
        return myReviews;
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

    public boolean checkEmpty() throws SQLException{
        ArrayList<Course> courses = getAllCourses();
        ArrayList<User> users = getAllUsers();
        return courses.isEmpty() || users.isEmpty();
    }

    public void dropReviewsTable() throws SQLException{
        Statement dropReviews = connection.createStatement();
        String dropReviewsString = "DROP TABLE Reviews";
        dropReviews.execute(dropReviewsString);
        dropReviews.close();
    }
}
