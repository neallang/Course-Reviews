package edu.virginia.sde.reviews;

public class CourseIDSingleton {

    private static final CourseIDSingleton instance = new CourseIDSingleton();

    private int courseID;

    private CourseIDSingleton(){};

    public static CourseIDSingleton getInstance(){
        return instance;
    }
    public int getCourseID(){
        return this.courseID;
    }
    public void setCourseID(int courseID){
        this.courseID = courseID;
    }

}
