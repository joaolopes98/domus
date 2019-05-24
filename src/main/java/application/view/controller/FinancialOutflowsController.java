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
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.Date;

public class FinancialOutflowsController extends Controller {

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
                removeValue();
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
                    System.out.println("ERRO - Erro ao Inserir Saida Financeira");
                }
            } else {
                System.out.println("ERRO - Caixa Não Possui Dinheiro Necessario");
            }
        } else {
            System.out.println("ERRO - Saida Financeira Zerada");
        }
    }
}
