package edu.virginia.sde.reviews;

import javax.xml.transform.Result;
import java.lang.module.Configuration;
import java.sql.*;
import java.text.DecimalFormat;
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
                    Title   TEXT    not null UNIQUE,
                    AverageCourseRating DOUBLE not null
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
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Courses(Department, CourseNumber, Title, AverageCourseRating) values (?, ?, ?, ?)");
            statement.setString(1, course.getDepartment());
            statement.setString(2, course.getCourseNumber());
            statement.setString(3, course.getTitle());
            statement.setDouble(4, course.getAverageCourseRating());
            statement.executeUpdate();
        } catch (SQLException e) {
            rollback(); //rolls back any changes before the Exception was thrown
            throw e; //still throws the SQLException
        }

    }

    public boolean courseAlreadyExists(String department, String title) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Courses WHERE Department = " + "\'" + department + "\'" + " AND Title = " + "\'" + title + "\'");
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
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
            var AverageCourseRating = resultSet.getDouble(5);


            Course course = new Course(Department, CourseNumber, Title, AverageCourseRating);
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
            var AverageCourseRating = resultSet.getDouble(5);


            Course course = new Course(Department, CourseNumber, Title, AverageCourseRating);
            courses.add(course);
        }
            return courses;

    }

    public ArrayList<Course> getCoursesBySearch(String department, String courseNumber, String title) throws SQLException{

        //Got the code for how to get stops from the OCT 26 Lecture example code (Database.java)
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }

        String findCourses = "SELECT * FROM Courses WHERE 1=1";
        ArrayList<String> searchFor = new ArrayList<>();

        if(!department.isEmpty()){
            findCourses += " AND Department = ?";
            searchFor.add(department);
        }
        if(!courseNumber.isEmpty()){
            findCourses += " AND CourseNumber = ?";
            searchFor.add(courseNumber);
        }
        if(!title.isEmpty()){
            findCourses += " AND Title LIKE ?";
            searchFor.add("%"+title+"%");
        }


        PreparedStatement statement = connection.prepareStatement(findCourses);

        for(int i = 0; i<searchFor.size(); i++){
            statement.setString(i+1, searchFor.get(i));
        }

        ResultSet rs = statement.executeQuery();

        ArrayList<Course> foundCourses = new ArrayList<>();
        while (rs.next()) {
            var Department = rs.getString(2);
            var CourseNumber = rs.getString(3);
            var Title = rs.getString(4);
            var Rating = rs.getDouble(5);


            Course course = new Course(Department, CourseNumber, Title, Rating);

            foundCourses.add(course);
        }

        return foundCourses;
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
            updateAverageCourseRating(review.getCourseID());
        } catch (SQLException e) {
            rollback(); //rolls back any changes before the Exception was thrown
            throw e; //still throws the SQLException
        }


    }

    public void deleteReview(int userID, int courseID) throws SQLException{
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Reviews where UserID = " + userID + " and CourseID = " + courseID);
            statement.execute();
            updateAverageCourseRating(courseID);

        } catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public boolean checkReviewsExist(int courseID) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Reviews WHERE CourseID = " + courseID);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public void updateReview(String comment, int rating, int userID, int courseID) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Reviews SET ReviewText = " + "\'" + comment + "\'" + ", Rating = " + rating + " WHERE UserID = " + userID + " AND CourseID = " + courseID);
            statement.execute();
            updateAverageCourseRating(courseID);

        } catch (SQLException e){
            rollback();
            throw e;
        }
    }

    public String getComment(int userID, int courseID) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT ReviewText FROM Reviews WHERE UserID = " + userID + " AND CourseID = " + courseID);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.getString(1);
    }

    public boolean userReviewExists(String username, int courseID) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        int userID = getUserID(username);
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Reviews where UserID = " + userID + " and CourseID = " + courseID);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();

    }


    public void updateAverageCourseRating(int courseID) throws SQLException{
        try {
            double newAverage;
            if(checkReviewsExist(courseID)){
                 newAverage = getAverageCourseRating(courseID);
            } else {
                newAverage = 0.0;
            }

            PreparedStatement statement = connection.prepareStatement("UPDATE Courses SET AverageCourseRating = " + newAverage + " WHERE ID = " + courseID);
            statement.execute();

        } catch (SQLException e){
            rollback();
            throw e;
        }

    }

    public ArrayList<MyReview> getMyReviews(int userID) throws SQLException{
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
            var CourseNumber = resultSet.getString(6);
            var Department = resultSet.getString(7);
            var CourseID = resultSet.getInt(5);


            MyReview myReview = new MyReview(ReviewText, Rating, ReviewTime, CourseNumber, Department, CourseID);
            myReviews.add(myReview);
        }
        return myReviews;
    }




    public ArrayList<Review> getCourseReviews(int courseID) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("select * from Reviews where CourseID = " + courseID );
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Review> reviews = new ArrayList<>();
        while (resultSet.next()) {
            var UserID = resultSet.getInt(2);
            var CourseID = resultSet.getInt(3);
            var ReviewText = resultSet.getString(4);
            var Rating = resultSet.getInt(5);
            var ReviewTime = resultSet.getTimestamp(6);

            Review review = new Review(UserID, CourseID, ReviewText, Rating, ReviewTime);
            reviews.add(review);
        }
        return reviews;
    }

    public int getCourseID(String title) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT ID FROM Courses where Title = " + "\'" + title + "\'");
        ResultSet resultSet = statement.executeQuery();
        return resultSet.getInt(1);
    }

    public Course getCourseByID(int courseID) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Courses where ID = " + courseID);
        ResultSet resultSet = statement.executeQuery();
        return new Course(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getDouble(5));
    }

    public double getAverageCourseRating(int courseID) throws SQLException{
        if (connection.isClosed()){
            throw new IllegalStateException("Connection is not open");
        }
        double average = 0.0;
        double total = 0;
        double numReviews = 0;
        PreparedStatement statement = connection.prepareStatement("SELECT Rating FROM Reviews where CourseID = " + courseID);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            int currentRating = resultSet.getInt(1);
            total += currentRating;
            numReviews++;
        }
        average = total/numReviews;
        return average;

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

    public void dropCoursesTable() throws SQLException{
        Statement dropCourses = connection.createStatement();
        String dropCoursesString = "DROP TABLE Courses";
        dropCourses.execute(dropCoursesString);
        dropCourses.close();
    }

    public void dropUsersTable() throws SQLException{
        Statement dropUsers = connection.createStatement();
        String dropUsersString = "DROP TABLE Users";
        dropUsers.execute(dropUsersString);
        dropUsers.close();
    }
}
