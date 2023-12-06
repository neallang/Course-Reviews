package edu.virginia.sde.reviews;

public class CurrentReviewSingleton {
    private static final CurrentReviewSingleton instance = new CurrentReviewSingleton();

    private int rating;
    private String comment;

    private CurrentReviewSingleton(){};

    public static CurrentReviewSingleton getInstance(){
        return instance;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
