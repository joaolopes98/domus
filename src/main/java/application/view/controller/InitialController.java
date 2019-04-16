package application.view.controller;

import application.view.auxiliary.Controller;
import application.view.auxiliary.Window;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


@SuppressWarnings("ALL")
public class InitialController extends Controller {
    @FXML private AnchorPane imgBackground;
    @FXML private HBox paneMain;

    @FXML private TextField inputUser;
    @FXML private PasswordField inputPassword;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                out();
                e.consume();
            }
        });

        stage.initStyle(StageStyle.UNDECORATED);
        super.initialize(oldStage, scene, oldController, objects);

        startTransition();
        setupInputs();
    }

    private void setupInputs(){
        inputUser.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB){
                inputPassword.requestFocus();
                e.consume();
            } else if (e.getCode() == KeyCode.ENTER){
                login();
                e.consume();
            }
        });

        inputPassword.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.TAB){
                inputUser.requestFocus();
                e.consume();
            } else if (e.getCode() == KeyCode.ENTER){
                login();
                e.consume();
            }
        });
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

    @FXML private void login(){
        Window.changeScene(this.stage, "pdv", this);
        this.stage.close();
    }

    @FXML private void out(){
        stage.close();
        System.exit(0);
    }
}
