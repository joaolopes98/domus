package application.view.controller;

import application.controller.object.Access;
import application.controller.object.User;
import application.model.AccessModel;
import application.model.HibernateUtilities;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.animation.FadeTransition;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


@SuppressWarnings("ALL")
public class InitialController extends Controller {
    @FXML private AnchorPane imgBackground;
    @FXML private AnchorPane paneMain;

    @FXML private HBox paneLogin;

    @FXML private VBox paneConfig;
    @FXML private Label lblConfig;
    @FXML private ProgressBar progressBar;

    @FXML private Label lblError;

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
        Mask.upperCase(inputUser);
        inputUser.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB){
                inputPassword.requestFocus();
                e.consume();
            } else if (e.getCode() == KeyCode.ENTER){
                login();
                e.consume();
            }
        });

        Mask.upperCase(inputPassword);
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
        fadeInMain.setOnFinished( e -> setupProgressBar());


        FadeTransition fadeInImg = new FadeTransition(Duration.seconds(2), imgBackground);
        fadeInImg.setFromValue(0);
        fadeInImg.setToValue(1);
        fadeInImg.setCycleCount(1);
        fadeInImg.setOnFinished( e -> fadeInMain.play());
        fadeInImg.play();
    }

    private void setupProgressBar(){
        progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
            if(progressBar.getProgress() == 1){
                setVisibleLogin();
            }
        });

        Service service = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        updateMessage("Configurando...");
                        updateProgress(1,4);
                        Thread.sleep(1000);

                        updateProgress(2,4);
                        updateMessage("Conectando ao Banco de Dados ..");
                        HibernateUtilities.load();

                        updateProgress(3,4);
                        updateMessage("Abrindo Login ..");
                        Thread.sleep(500);

                        updateProgress(4,4);
                        return null;
                    }
                };
            }
        };

        lblConfig.textProperty().bind(service.messageProperty());
        progressBar.progressProperty().bind(service.progressProperty());
        service.start();
    }

    private void setVisibleLogin() {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), paneLogin);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), paneConfig);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);
        fadeOut.setOnFinished( e -> {
            paneLogin.setOpacity(0);
            paneLogin.setVisible(true);
            fadeIn.play();
            paneConfig.setVisible(false);
        });
        fadeOut.play();
    }

    @FXML private void login(){

        if(AccessModel.isLogin(inputUser.getText(), inputPassword.getText())){
            Access user = User.getUser();
            if(user.getRole() != 2) {
                Window.changeScene(this.stage, "pdv", this);
                this.stage.close();
            } else {
                Window.changeScene(this.stage, "error", this,
                        "Funcionalidade Não Disponivel");
            }
        } else {
            lblError.setText("Usuário ou senha incorretos");
            lblError.setVisible(true);
        }
    }

    @FXML private void out(){
        stage.close();
        System.exit(0);
    }
}