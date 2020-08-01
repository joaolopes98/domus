package application.view.controller;

import application.controller.object.Sale;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.text.Format;

public class SaleHistoryController extends Controller {

    @FXML private Label lblSale;
    @FXML private Label lblUser;
    @FXML private Label lblCustomer;
    @FXML private Label lblDate;
    @FXML private Label lblDiscount;
    @FXML private Label lblTotal;

    private Sale sale;
    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.sale = (Sale) objects[0];
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        setupSaleInfo();
    }

    private void setupSaleInfo() {
        lblSale.setText(String.valueOf(sale.getId()));

        lblUser.setText(sale.getAccess().getName());
        if(sale.getCustomer() != null) {
            lblCustomer.setText(sale.getCustomer().getName());
        } else {
            lblCustomer.setText(" - ");
        }
        lblDate.setText(Formatter.formatDateHour(sale.getDate()));
        lblDiscount.setText(Formatter.formatMoney(sale.getDiscount()));
        lblTotal.setText(Formatter.formatMoney(sale.getValue()));
    }

    @FXML
    private void cancel(){
        this.stage.close();
    }
}
