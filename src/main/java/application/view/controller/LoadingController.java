package application.view.controller;

import application.view.auxiliary.Controller;
import application.view.auxiliary.Function;
import application.view.auxiliary.Window;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class LoadingController extends Controller {

    @FXML private Label lblText;
    @FXML private ProgressIndicator progress;

    private Function function;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        lblText.setText((String) objects[0]);
        this.function = (Function) objects[1];

        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        setupProgress();
    }

    private void setupProgress() {
        Service service = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        function.run();
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded( e -> this.stage.close());
        progress.progressProperty().bind(service.progressProperty());
        service.start();
    }
}
