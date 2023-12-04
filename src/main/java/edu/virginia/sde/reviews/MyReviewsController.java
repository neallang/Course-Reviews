package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class MyReviewsController {
    @FXML
    private Label messageLabel;

    public void handleCourseIDButton() {
        messageLabel.setText("You pressed the button!");
    }
    public void handleCourseNameButton() {
        messageLabel.setText("You pressed the button!");
    }
    public void handleCourseMnemonicButton() {
        messageLabel.setText("You pressed the button!");
    }
}
