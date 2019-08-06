package application.view.auxiliary;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public abstract class Window {
    static final String ICON = "/View/img/logoGrande.png";

    public static void changeScene(Stage stage, String sceneName, Controller oldController, Object... objects){
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(Window.class.getResource("/View/fxml/" + sceneName + ".fxml").openStream());
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/View/css/" + sceneName + ".css");

            ScreenInterface screenInterface = loader.getController();
            screenInterface.initialize(stage, scene, oldController, objects);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void setModal(Stage stage, Stage oldStage, Controller controller) {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(oldStage);

        stage.showingProperty().addListener( e -> controller.activeWaitScreen(stage.isShowing()));
    }
}
