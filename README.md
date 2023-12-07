[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/DC1SF4uZ)
# Homework 6 - Responding to Change

## Authors
1) Neal Langhorne, yhn5yh, [neallang]
2) Danny Know, msv7hq, [dknow17]
3) Stanley Inouye, ete4ec, [Stanley-Inouye]
4) Connolly Woo, egs6nf, [connolly-woo]

## To Run

Run from the CourseReviewsApplication.java class with the VM Argument: <br> --module-path [Path to JavaFX folder] --add-modules javafx.controls,javafx.fxml<br> The path should be in quotes, not brackets. Project preferences should be run on JDK 17.0.9.<br>
The CourseReviewsApplication.java class will open the application with the login screen, as well as create and populate a database with courses, users, and reviews.

## Contributions

List the primary contributions of each author. It is recommended to update this with your contributions after each coding session.:

### [Author 1 - Neal Langhorne]

* Implemented MyReviewsController, SceneController classes
* Created and implemented initialize() method in CourseReviewsController class
* Helped implement switching between screens across controllers
* Created and implemented LoginController

### [Author 2 - Danny Know]

* Implemented switchToRegister() method in LoginController class
* Implemented MyReviewsController, RegisterController classes

### [Author 3 - Stanley Inouye]

* Created Login.fxml file
* Implemented singleton patterns: CourseIDSingleton, CurrentReviewSingleton, UserNameSingleton classes
* Created databaseDriver and all subsequent methods and functionality (Creating and updating datatables, finding average course ratings, manipulating course reviews, and userIds)
* Created Course, Review, MyReview and User classes to instantiate and handle all related data
* Implemented save() and delete() methods in CourseReviewsController class
  

### [Author 4 - Connolly Woo]

* Created search-screen.fxml file
* Created the SearchController and all subsequent methods (Adding courses and all included conditionals, searching courses by different parameters, log out/my reviews buttons)
* Implemented getCoursesBySearch() method in DatabaseDriver
* Helped implement switching between screens across controllers

## Issues
* Username IS CASE SENSITIVE! Not an issue, just clarifying
* For any incorrect logins, we used the same message ("Invalid Username or Password") as a protective measure against any attempts to try multiple passwords for one username/multiple usernames for one password that does work
* Occasionally, when running the search screen, adding invalid courses repeatedly may stop showing error messages. I'm not sure if this is only on my (Connolly's) computer, but if I close it and reopen it works as intended. We believe this may happen when the "Add Course" button is hit subsequent times too quickly, but because the Button handling is a JavaFX feature, we are not sure how to fix this. This was happening extremely infrequently, but if it does, closing and reopening it or logging in and out should fix it.
