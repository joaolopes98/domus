package application.view.controller;

import application.view.auxiliary.Controller;
import javafx.animation.FadeTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class InitialController extends Controller {
    @FXML private AnchorPane imgBackground;
    @FXML private HBox paneMain;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.ENTER){
                System.out.println("AKI");
                e.consume();
            }
        });

        stage.initStyle(StageStyle.UNDECORATED);

        super.initialize(oldStage, scene, oldController);
        startTransition();
    }

    private void startTransition(){
        paneMain.setOpacity(0);

        FadeTransition fadeInMain = new FadeTransition(Duration.seconds(1), paneMain);
        fadeInMain.setFromValue(0);
        fadeInMain.setToValue(1);
        fadeInMain.setCycleCount(1);


        FadeTransition fadeInImg = new FadeTransition(Duration.seconds(2), imgBackground);
        fadeInImg.setFromValue(0);
        fadeInImg.setToValue(1);
        fadeInImg.setCycleCount(1);
        fadeInImg.setOnFinished( e -> fadeInMain.play());
        fadeInImg.play();
    }

}
