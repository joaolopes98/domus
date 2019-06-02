package application.view.controller;

import application.controller.CashCalc;
import application.controller.object.FinancialOutflow;
import application.controller.object.User;
import application.model.CashMovementModel;
import application.model.FinancialOutflowModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.Date;

public class FinancialOutflowsController extends Controller {

    @FXML private TextField txtValue;

    @FXML private AnchorPane waitScreen;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                e.consume();
                cancel();
            } else if ( e.getCode() == KeyCode.ENTER){
                e.consume();
                removeValue();
            }
        });

        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        Mask.money(txtValue);
    }

    @FXML public void cancel() {
        stage.close();
    }

    @FXML public void removeValue() {
        double value = Mask.unmaskMoney(txtValue.getText());
        if(value != 0) {
            if(CashCalc.money(CashMovementModel.getOpened()) >= value) {
                FinancialOutflow fo = new FinancialOutflow();
                fo.setValue(value);
                fo.setDate(new Timestamp(new Date().getTime()));
                fo.setCashMovement(CashMovementModel.getOpened());
                fo.setAccess(User.getUser());


                if (FinancialOutflowModel.create(fo)) {
                    stage.close();
                } else {
                    Window.changeScene(this.stage, "error", this, "Erro ao Inserir Saida Financeira");
                }
            } else {
                Window.changeScene(this.stage, "error", this, "Caixa não possui dinheiro necessario");
            }
        } else {
            Window.changeScene(this.stage, "error", this, "Não é possivel inserir saida financeira zerada");
        }
    }

    @Override
    public void activeWaitScreen(boolean wait){
        waitScreen.setVisible(wait);
    }
}
