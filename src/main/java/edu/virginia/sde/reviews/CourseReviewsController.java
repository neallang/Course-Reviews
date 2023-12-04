package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

public class CourseReviewsController {
    @FXML
    private Button back_button;
    @FXML
    Slider rating_slider;

    public void goBack() {
        back_button.setText("You pressed the button!");
    }
//    public void incrementSlider(){
//        rating_slider.setMax(5);
//        rating_slider.setBlockIncrement(1);
//    }
}
