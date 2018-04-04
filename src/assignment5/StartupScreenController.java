package assignment5;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class StartupScreenController {

    @FXML Slider widthSlider;
    @FXML Slider heightSlider;
    @FXML Button nextButton;
    @FXML JFXTextField seedTextField;


    @FXML
    public void initialize() {
    }

    @FXML
    protected void toMainScreen() {
        Params.world_height = (int)Math.round(heightSlider.getValue());
        Params.world_width = (int)Math.round(widthSlider.getValue());

        if (seedTextField.getText().length() > 0) {
            try {
                int parsed = Integer.parseInt(seedTextField.getText());
                Critter.setSeed(parsed);
            } catch (Exception e) {
                System.out.println("Invalid Seed");
            }
        }

        //Main.worldHeight = (int)Math.round(heightSlider.getValue());
        //Main.worldWidth = (int)Math.round(widthSlider.getValue());
        Main.getInstance().goToMainScreen();
    }

}
