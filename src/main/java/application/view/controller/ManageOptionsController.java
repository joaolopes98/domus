package application.view.controller;

import application.view.auxiliary.Controller;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ManageOptionsController extends Controller {

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);
    }

    @FXML private void products(){

    }

    @FXML private void users(){

    }

    @FXML private void cancel(){
        this.stage.close();
    }
}
