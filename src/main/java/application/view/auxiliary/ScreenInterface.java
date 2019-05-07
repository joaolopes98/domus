package application.view.auxiliary;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface ScreenInterface {
    void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects);
}
