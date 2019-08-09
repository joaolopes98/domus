package application.view.controller;

import application.view.auxiliary.Controller;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ErrorController extends Controller {

    @FXML private Label lblInfo;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                close();
                e.consume();
            } else if (e.getCode() == KeyCode.ENTER){
                close();
                e.consume();
            }
        });

        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        lblInfo.setText((String) objects[0]);
    }

    @FXML private void close (){
        this.stage.close();
    }
}
