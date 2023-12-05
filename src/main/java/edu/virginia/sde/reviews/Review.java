package edu.virginia.sde.reviews;


import java.sql.Timestamp;

public class Review {
    private int userID;
    private int courseID;
    private String reviewText;
    private int rating;
    private Timestamp timeStamp;

    public Review(int userID, int courseID, String reviewText, int rating, Timestamp timeStamp) {
        this.userID = userID;
        this.courseID = courseID;
        this.reviewText = reviewText;
        this.rating = rating;
        this.timeStamp = timeStamp;
    }

    public int getUserID() {
        return userID;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getReviewText() {
        return reviewText;
    }
}
