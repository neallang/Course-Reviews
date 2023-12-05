package edu.virginia.sde.reviews;

public class Review {
    private int userID;
    private int courseID;
    private String reviewText;
    private int rating;

    public Review(int userID, int courseID, String reviewText, int rating) {
        this.userID = userID;
        this.courseID = courseID;
        this.reviewText = reviewText;
        this.rating = rating;
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
    public int getRating(){return rating;}
}
