package application.view.controller;

import application.controller.GenerateFunction;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Function;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ManageOptionsController extends Controller {

    @FXML private AnchorPane waitScreen;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });

        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);
    }

    @FXML private void insertInflows(){
        Window.changeScene(this.stage, "financialInflows", this);
    }

    @FXML private void insertOutflows(){
        Window.changeScene(this.stage, "financialOutflows", this);
    }

    @FXML private void products(){
        Window.changeScene(this.stage, "products", this);
    }

    @FXML private void services(){
        Window.changeScene(this.stage, "services", this);
    }

    @FXML private void users(){
        Window.changeScene(this.stage, "users", this);
    }

    @FXML private void reportCash(){
        Window.changeScene(this.stage, "choseDate", this,
                GenerateFunction.reportCash());
    }

    @FXML private void cancel(){
        PDVController pdv = (PDVController) this.oldController;
        pdv.updateProducts();
        this.stage.close();
    }

    @Override
    public void activeWaitScreen(boolean wait) {
        waitScreen.setVisible(wait);
    }
}
