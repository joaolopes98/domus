package application.view.controller;

import application.controller.object.CashMovement;
import application.controller.object.User;
import application.model.CashMovementModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.Date;

public class OpenCashController extends Controller {

    @FXML private TextField txtValue;

    private PDVController pdvController;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        pdvController = (PDVController) oldController;
        pdvController.activeWaitScreen(true);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                e.consume();
                cancel();
            } else if ( e.getCode() == KeyCode.ENTER){
                e.consume();
                open();
            }
        });

        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        this.stage.showingProperty().addListener( e -> pdvController.activeWaitScreen(false));
        Mask.money(txtValue);
    }

    public void cancel() {
        stage.close();
        pdvController.logout();
    }

    public void open() {

        double value = Mask.unmaskMoney(txtValue.getText());

        CashMovement cashMovement = new CashMovement();
        cashMovement.setValue(value);
        cashMovement.setDate(new Timestamp(new Date().getTime()));
        cashMovement.setOpened_by(User.getUser());

        if(CashMovementModel.create(cashMovement)){
            stage.close();
        } else {
            Window.changeScene(this.stage, "error", this,
                    "Não foi possivel criar o caixa");
        }
    }
}
