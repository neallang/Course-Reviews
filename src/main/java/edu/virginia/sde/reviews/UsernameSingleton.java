package edu.virginia.sde.reviews;

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
