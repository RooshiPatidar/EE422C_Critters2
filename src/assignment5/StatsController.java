package assignment5;

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
            m.invoke(c, (Critter.getInstances(className)));
        } catch (Exception e) {
            System.out.println("error processing: ");
        }
    }
}
