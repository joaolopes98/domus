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

    @FXML private AnchorPane paneLogin;

    @FXML private VBox paneLoading;
    @FXML private Label lblLoading;
    @FXML private ProgressBar progressBar;

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
//        startTransition();
    }

    private void startTransition(){
        paneMain.setOpacity(0);
        paneLogin.setOpacity(0);

        FadeTransition fadeInLogin = new FadeTransition(Duration.seconds(1), paneLogin);
        fadeInLogin.setFromValue(0);
        fadeInLogin.setToValue(1);
        fadeInLogin.setCycleCount(1);

        FadeTransition fadeOutLoading = new FadeTransition(Duration.seconds(1), paneLoading);
        fadeOutLoading.setFromValue(1);
        fadeOutLoading.setToValue(0);
        fadeOutLoading.setCycleCount(1);
        fadeOutLoading.setOnFinished( e -> fadeInLogin.play());

        progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
            if(progressBar.getProgress() == 1) fadeOutLoading.play();
        });

        FadeTransition fadeInMain = new FadeTransition(Duration.seconds(1), paneMain);
        fadeInMain.setFromValue(0);
        fadeInMain.setToValue(1);
        fadeInMain.setCycleCount(1);
        fadeInMain.setOnFinished( e -> setupProgression());


        FadeTransition fadeInImg = new FadeTransition(Duration.seconds(1), imgBackground);
        fadeInImg.setFromValue(0);
        fadeInImg.setToValue(1);
        fadeInImg.setCycleCount(1);
        fadeInImg.setOnFinished( e -> fadeInMain.play());
        fadeInImg.play();
    }

    private void setupProgression(){
        Service service = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        updateProgress(0, 5);

                        updateMessage("Carregando ...");
                        updateProgress(1,4);
                        Thread.sleep(1000);



                        updateMessage("Conectando Banco ...");
                        updateProgress(2, 4);
                        Thread.sleep(500);

                        updateMessage("Abrindo Login ...");
                        updateProgress(3, 4);
                        Thread.sleep(2000);

                        updateProgress(4,4);
                        return null;
                    }
                };
            }
        };

        lblLoading.textProperty().bind(service.messageProperty());
        progressBar.progressProperty().bind(service.progressProperty());
        service.restart();
    }
}
