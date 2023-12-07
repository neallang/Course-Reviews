package edu.virginia.sde.reviews;
//Got the code for how to create a singleton class from this youtube video: https://www.youtube.com/watch?v=MsgiJdf5njc
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
