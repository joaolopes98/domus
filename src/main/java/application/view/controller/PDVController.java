package application.view.controller;

import application.view.auxiliary.Controller;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class PDVController extends Controller {
    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                System.out.println("AKI");
                e.consume();
            } else if (e.getCode() == KeyCode.F11){
                stage.setFullScreen(!stage.isFullScreen());
                e.consume();
            }
        });

//        this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.stage.setFullScreenExitHint("Pressione F11 para sair do modo FullScreen");
        this.stage.setTitle("DOMUS PDV");
        super.initialize(oldStage, scene, oldController, objects);
        this.stage.setMaximized(true);
        this.stage.setFullScreen(true);
    }
}
