package edu.virginia.sde.reviews;

public class Course {
    private String department;
    private String courseNumber;
    private String title;

    public Course(String department, String courseNumber, String title) {
        this.department = department;
        this.courseNumber = courseNumber;
        this.title = title;
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
}
