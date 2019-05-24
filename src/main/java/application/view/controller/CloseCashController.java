package application.view.controller;

import application.controller.CashCalc;
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

public class CloseCashController extends Controller {

    @FXML private TextField txtValue;

    private PDVController pdvController;


    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        pdvController = (PDVController) oldController;
        pdvController.activeWaitScreen(true);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                e.consume();
                cancel();
            } else if ( e.getCode() == KeyCode.ENTER){
                e.consume();
                close();
            }
        });

        Window.setModal(this.stage, oldStage);
        super.initialize(oldStage, scene, oldController, objects);

        this.stage.showingProperty().addListener( e -> pdvController.activeWaitScreen(false));
        Mask.money(txtValue);
    }

    @FXML public void cancel() {
        stage.close();
    }

    @FXML public void close() {
        CashMovement cashMovement = CashMovementModel.getOpened();
        cashMovement.setClosed(true);
        cashMovement.setClosed_at(new Timestamp(new Date().getTime()));
        cashMovement.setValue_closed_system(CashCalc.money(cashMovement));
        cashMovement.setValue_closed_input(Mask.unmaskMoney(txtValue.getText()));
        cashMovement.setClosed_by(User.getUser());

        if(CashMovementModel.update(cashMovement)){
            stage.close();
            pdvController.logout();
        } else {
            System.out.println("ERRO - Fechar Caixa");
        }
    }
}
