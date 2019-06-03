package application.view.auxiliary;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class Controller implements ScreenInterface {
    protected Stage stage = new Stage();
    protected Stage oldStage;
    protected Controller oldController;
    protected Object[] objects;

    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.objects = objects;

        this.oldStage = oldStage;
        this.oldController = oldController;

        this.stage.getIcons().add(new Image(Window.ICON));
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void activeWaitScreen(boolean wait){
    }

    public Stage getStage() {
        return stage;
    }
}
