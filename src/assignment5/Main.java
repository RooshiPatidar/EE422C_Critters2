package assignment5;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Rooshi Patidar
 * rsp983
 * 15500
 * Spring 2018
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Main extends Application {
        private static Main instance;
        private Stage stage;
        ArrayList<StatsController> statsControllers = new ArrayList<>();

        public static int worldHeight;
        public static int worldWidth;
        String lastClickedStats;

        @Override
        public void start(Stage primaryStage) throws Exception{
            stage = primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("fxml/startupScreen.fxml"));
            stage.setTitle("Critters2");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        }

        public void goToMainScreen() {
            try {
                replaceSceneContent("\\fxml\\mainScreen.fxml", 1600, 800);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Parent replaceSceneContent(String fxml, int width, int height) {
            try {

                FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
                Parent page = loader.load();

                Scene scene = new Scene(page, width, height);
                stage.resizableProperty().setValue(Boolean.FALSE);

                stage.setTitle("Critters2");
                stage.setScene(scene);

                stage.centerOnScreen();
                stage.show();

                return page;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    public void popUpStats() {
        try {
            popUpModalWindow("fxml/stats.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popUpModalWindow(String fxml) {
        try {
            Stage modal;
            modal = new Stage();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
            Parent root = loader.load();
            statsControllers.add(loader.getController());
            modal.setScene(new Scene(root));
            modal.initModality(Modality.WINDOW_MODAL);
            modal.initOwner(modal.getOwner());
            modal.centerOnScreen();
            modal.showAndWait();
        } catch (Exception e) {e.printStackTrace();}
    }

        public static Main getInstance() {
            return instance;
        }


        public Main() {
            instance = this;
        }

        public static void main(String[] args) {
            launch(args);
        }

        public int getWorldHeight() {
            return worldHeight;
        }

        public void setWorldHeight(int worldHeight) {
            this.worldHeight = worldHeight;
        }

        public int getWorldWidth() {
            return worldWidth;
        }

        public void setWorldWidth(int worldWidth) {
            this.worldWidth = worldWidth;
        }
    }
