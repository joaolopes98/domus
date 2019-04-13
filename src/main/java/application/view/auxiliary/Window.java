package application.view.auxiliary;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public abstract class Window {
    public static final String ICON = "/View/img/domus_icon.png";

    public static void changeScene(Stage stage, String sceneName, Controller oldController, Object... objects){
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(Window.class.getResource("/View/fxml/" + sceneName + ".fxml").openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/View/css/" + sceneName + ".css");

            Screen screen = loader.getController();
            screen.initialize(stage, scene, oldController, objects);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
