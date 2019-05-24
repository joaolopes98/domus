package application.view.controller;

import application.controller.object.FinancialInflow;
import application.controller.object.User;
import application.model.CashMovementModel;
import application.model.FinancialInflowModel;
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

public class FinancialInflowsController extends Controller {

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
                insertValue();
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

    @FXML public void insertValue() {
        double value = Mask.unmaskMoney(txtValue.getText());
        if(value != 0) {
            FinancialInflow fi = new FinancialInflow();
            fi.setValue(value);
            fi.setDate(new Timestamp(new Date().getTime()));
            fi.setCashMovement(CashMovementModel.getOpened());
            fi.setAccess(User.getUser());

            if(FinancialInflowModel.create(fi)) {
                stage.close();
            } else {
                System.out.println("ERRO - Erro ao Inserir Entrada Financeira");
            }
        } else {
            System.out.println("ERRO - Entrada Financeira Zerada");
        }
    }
}
