package application.view.auxiliary;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class Controller implements Screen{
    protected Stage stage = new Stage();
    protected Stage oldStage;
    protected Controller oldController;
    protected Object[] objects;

    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.objects = objects;

        this.oldStage = oldStage;
        this.oldController = oldController;

        this.stage.setScene(scene);
        this.stage.show();
    }
}
