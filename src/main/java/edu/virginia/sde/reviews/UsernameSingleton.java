package edu.virginia.sde.reviews;
//Got the code for how to create a singleton class from this youtube video: https://www.youtube.com/watch?v=MsgiJdf5njc
public class UsernameSingleton {
    private static final UsernameSingleton instance = new UsernameSingleton();

    private String username;

    private UsernameSingleton(){};

    public static UsernameSingleton getInstance(){
        return instance;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
}
