package edu.virginia.sde.reviews;

import java.sql.Time;
import java.sql.Timestamp;

public class MyReview {
    private String reviewText;
    private int rating;
    private Timestamp timestamp;
    private String courseNumber;
    private String department;
    private int courseID;


    public MyReview(String reviewText, int rating, Timestamp timestamp, String courseNumber, String department, int courseID) {
        this.reviewText = reviewText;
        this.rating = rating;
        this.timestamp = timestamp;
        this.courseNumber = courseNumber;
        this.department = department;
        this.courseID = courseID;
    }


    public String getReviewText() {
        return reviewText;
    }

    public int getRating() {
        return rating;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getDepartment() {
        return department;
    }

    public int getCourseID() {
        return courseID;
    }
}
