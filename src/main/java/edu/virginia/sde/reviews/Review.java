package edu.virginia.sde.reviews;

public class Review {
    private int userID;
    private int courseID;
    private String reviewText;

    public Review(int userID, int courseID, String reviewText) {
        this.userID = userID;
        this.courseID = courseID;
        this.reviewText = reviewText;
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
