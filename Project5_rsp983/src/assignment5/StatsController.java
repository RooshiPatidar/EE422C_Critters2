package assignment5;

/* CRITTERS StatsController.java
 * EE422C Project 4 submission by
 * Rooshi Patidar
 * rsp983
 * 15500
 * Spring 2018
 */

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.lang.reflect.Method;

public class StatsController {

    @FXML private Label statsLabel;
    private String className;

    @FXML protected void initialize() {
        className = Main.getInstance().lastClickedStats;
        update();
    }

    public void update() {
        try {
            Class<?> c = Class.forName("assignment5" + "." + className);
            Class[] params = {java.util.List.class};
            Method m = c.getDeclaredMethod("runStats", params);
            String result = (String)m.invoke(c, (Critter.getInstances(className)));
            statsLabel.setText(result);
        } catch (Exception e) {
            System.out.println("error processing: ");
        }
    }
}
