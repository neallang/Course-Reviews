package edu.virginia.sde.reviews;

public class Course {
    private String department;
    private String courseNumber;
    private String title;
    private double averageCourseRating;

    public Course(String department, String courseNumber, String title) {
        this.department = department;
        this.courseNumber = courseNumber;
        this.title = title;
        this.averageCourseRating = 0.0;
    }

    public Course(String department, String courseNumber, String title, double averageCourseRating){
        this.department = department;
        this.courseNumber = courseNumber;
        this.title = title;
        this.averageCourseRating = averageCourseRating;
    }

    public String getDepartment() {
        return department;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getTitle() {
        return title;
    }

    public Double getAverageCourseRating() {return averageCourseRating; }
}
