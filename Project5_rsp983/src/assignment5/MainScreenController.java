package assignment5;

/* CRITTERS MainScreenController.java
 * EE422C Project 4 submission by
 * Rooshi Patidar
 * rsp983
 * 15500
 * Spring 2018
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainScreenController {

    @FXML private HBox mainHBox;
    @FXML private JFXSlider makeSlider;
    @FXML private JFXSlider stepSlider;
    @FXML private JFXSlider animateSlider;
    @FXML private VBox statsClassesVBox;
    @FXML private VBox makeClassesVBox;

    private boolean playing = true;
    private Timeline playingTimeline;

    @FXML
    public void initialize() {
        ArrayList<String> foundClasses = new ArrayList<>();
        ArrayList<String> foundStatsClasses = new ArrayList<>();
        File folder = new File(System.getProperty("user.dir") + "\\src\\assignment5");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {

            if (file.isFile()) {

                if (file.getName().contains(".java")) {

                    try {
                        Class<?> c = Class.forName("assignment5" + "." + file.getName().substring(0, file.getName().indexOf('.')));
                        Critter critter = (Critter) c.newInstance();

                        Method methodToFind = null;
                        try {
                            methodToFind = c.getMethod("runStats", List.class);
                        } catch (NoSuchMethodException | SecurityException e) {
                            // Your exception handling goes here
                        }
                        if (methodToFind != null && methodToFind.getDeclaringClass() == c) {
                            foundStatsClasses.add(file.getName());
                        }


                        foundClasses.add(file.getName());

                    } catch (Exception e) {}
                }
            }
        }

        for (String c : foundStatsClasses) {
            c = c.substring(0, c.indexOf('.'));

            JFXButton sButton = new JFXButton();

            String finalC = c;
            sButton.setOnAction(event -> {
                stats(finalC);
            });

            sButton.setText(c);

            sButton.setStyle("\t-fx-background-color: #fb8c00;\n" +
                    "\t-fx-text-fill: white;\n" +
                    "\t-jfx-rippler-fill: #ffba62;\n" +
                    "\t-jfx-button-type: RAISED;\n" +
                    "\t-fx-font-family: \"Roboto\";\n" +
                    "    -fx-font-size: 15.0;");

            statsClassesVBox.getChildren().add(sButton);

        }

        for (String c : foundClasses) {
            c = c.substring(0, c.indexOf('.'));
            String finalC = c;

            JFXButton mButton = new JFXButton();

            mButton.setOnAction(event -> {
                make(finalC);
            });

            mButton.setText(c);

            mButton.setStyle("\t-fx-background-color: #fb8c00;\n" +
                    "\t-fx-text-fill: white;\n" +
                    "\t-jfx-rippler-fill: #ffba62;\n" +
                    "\t-jfx-button-type: RAISED;\n" +
                    "\t-fx-font-family: \"Roboto\";\n" +
                    "    -fx-font-size: 15.0;");

            makeClassesVBox.getChildren().add(mButton);
        }



        mainHBox.getChildren().add(1, new GridPane());
        show();
    }

    protected void show() {
        GridPane newPane = new GridPane();
        newPane.setPrefSize(800, 800);
        newPane.setMaxSize(800, 800);
        newPane.setMinSize(800, 800);
        newPane.setGridLinesVisible(true);
        int numCols = Params.world_width;
        int numRows = Params.world_height;

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            newPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            newPane.getRowConstraints().add(rowConst);
        }

        List<GridCritter> critters = Critter.displayWorld();
        for (GridCritter c : critters) {
            Shape s = getIcon(c.getCritter().viewShape().ordinal()); // convert the index to an icon.
            s.setFill(c.getCritter().viewFillColor());
            s.setStroke(c.getCritter().viewOutlineColor());
            newPane.add(s, c.getX(), c.getY()); // add the shape to the grid.
            Node node = newPane.getChildren().get(newPane.getChildren().size()-1);
            GridPane.setHalignment(node, HPos.CENTER);
            GridPane.setValignment(node, VPos.CENTER);
        }
        mainHBox.getChildren().remove(1);
        mainHBox.getChildren().add(1, newPane);
    }

    @FXML
    protected void make(String c) {

        try {
            int makeNum = (int)Math.round(makeSlider.getValue());
            for (int i = 0; i < makeNum; i++) {
                Critter.makeCritter(c);
            }
        } catch (Exception e) {
            System.out.println("error processing: " + c);
        }
        show();
        updateStats();
    }


    @FXML
    protected void step() {
        int steps = (int)Math.round(stepSlider.getValue());
        for (int i = 0; i < steps; i++) {
            Critter.worldTimeStep();
        }
        show();
        updateStats();
    }


    @FXML
    protected void animate() {
        playing = !playing;

        if (!playing) {
            int fps = (int) Math.round(animateSlider.getValue());
            double sleep = 1.0 / fps;
            Timeline tl = new Timeline();
            playingTimeline = tl;
            tl.setCycleCount(Animation.INDEFINITE);
            KeyFrame moveBall = new KeyFrame(Duration.seconds(sleep),
                    event -> {
                        Critter.worldTimeStep();
                        show();
                        updateStats();
                    });

            tl.getKeyFrames().add(moveBall);
            tl.play();
        } else {

            playingTimeline.stop();
        }


    }

    @FXML
    protected void stats(String c) {
        Main.getInstance().lastClickedStats = c;
        Main.getInstance().popUpStats();
    }

    private void updateStats() {
        for (StatsController statsController : Main.getInstance().statsControllers) {
            statsController.update();
        }
    }


    private static Shape getIcon(int shapeIndex) {
        Shape s = null;
        int smaller = Math.max(Params.world_height, Params.world_width);
        double size = 800/smaller;

        switch(shapeIndex) {
            //Circle
            case 0: s = new Circle(size/2);
                s.setFill(javafx.scene.paint.Color.GREEN); break;

            //Square
            case 1: s = new Rectangle(size, size);
                s.setFill(javafx.scene.paint.Color.RED); break;

            //Triangle
            case 2: Polygon triangle = new Polygon();
                triangle.getPoints().setAll(
                        0.0, size,   size, size,   size/2, 0.0
                );
                triangle.setFill(Color.BROWN);
                s = triangle;
                break;

            //Diamond
            case 3: Polygon diamond = new Polygon();
                diamond.getPoints().setAll(
                        size/2, 0.0,   0.0, size/2,   size/2, size,    size, size/2
                );
                diamond.setFill(Color.ORANGE);
                s = diamond;
                break;

            //Star
            case 4: Polygon star = new Polygon();
            size /= 6;
                star.getPoints().setAll(
                        0.0, size * 3,
                        size * 2, size * 2,
                        size * 3, 0.0,
                        size * 4, size * 2,
                        size * 6, size * 3,
                        size * 4, size * 4,
                        size * 3, size * 6,
                        size * 2, size * 4
                );
                s = star;

        }
        // set the outline of the shape
        //try {
        //    s.setStroke(javafx.scene.paint.Color.BLUE); // outline
        //} catch (Exception e){}
        return s;
    }

}
