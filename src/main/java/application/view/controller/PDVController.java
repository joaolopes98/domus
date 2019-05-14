package application.view.controller;

import application.controller.object.User;
import application.model.CashMovementModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PDVController extends Controller {

    @FXML private AnchorPane waitScreen;

    @FXML private Label lblUser;
    @FXML private Label lblDate;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.F11){
                stage.setFullScreen(!stage.isFullScreen());
                e.consume();
            }
        });

        this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
//        this.stage.setFullScreenExitHint("Pressione F11 para sair do modo FullScreen");
        this.stage.setTitle("DOMUS PDV");
        this.stage.setMinWidth(1110);
        this.stage.setMinHeight(705);
        super.initialize(oldStage, scene, oldController, objects);
//        this.stage.setMaximized(true);
//        Window.setFullScreen(this.stage);
        this.stage.setFullScreen(true);

        setupInfo();

        if(CashMovementModel.getOpened() == null) {
            Window.changeScene(this.stage, "openCash", this);
        }
    }

    private void setupInfo() {
        lblUser.setText(User.getUser().getName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        lblDate.setText(sdf.format(new Date()));
    }

    @FXML private void openFinalizeSale(){
        Window.changeScene(this.stage, "finalizeSale", this);
    }

    @FXML public void logout(){
        this.stage.close();
        Window.changeScene(this.stage, "initial", null);
    }

    void activeWaitScreen(boolean wait){
        waitScreen.setVisible(wait);
    }
}
