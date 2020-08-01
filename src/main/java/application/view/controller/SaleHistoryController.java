package application.view.controller;

import application.controller.ItemSaleField;
import application.controller.PaymentField;
import application.controller.object.Sale;
import application.controller.object.SaleItem;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SaleHistoryController extends Controller {

    @FXML private Label lblSale;
    @FXML private Label lblUser;
    @FXML private Label lblCustomer;
    @FXML private Label lblDate;
    @FXML private Label lblDiscount;
    @FXML private Label lblTotal;

    @FXML private TableView<PaymentField> tablePayments;
    @FXML private TableColumn<PaymentField, String> paymentName;
    @FXML private TableColumn<PaymentField, String> paymentValue;
    private ObservableList<PaymentField> obsPayment = FXCollections.observableArrayList();

    @FXML private TableView<ItemSaleField> tableItems;
    @FXML private TableColumn<ItemSaleField, String> itemCode;
    @FXML private TableColumn<ItemSaleField, String> itemName;
    @FXML private TableColumn<ItemSaleField, String> itemPrice;
    @FXML private TableColumn<ItemSaleField, String> itemQuantity;
    @FXML private TableColumn<ItemSaleField, String> itemDiscount;
    @FXML private TableColumn<ItemSaleField, String> itemValue;
    private ObservableList<ItemSaleField> obsItemSale = FXCollections.observableArrayList();

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
        setupPayments();
        setupItemsSale();
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

    private void setupPayments(){
        paymentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        paymentValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        tablePayments.setItems(obsPayment);

        sale.getFinancialInflows().forEach(financialInflow -> {
            obsPayment.add(new PaymentField(financialInflow.getPaymentMethod(),
                    Formatter.formatMoney(financialInflow.getValue())
                    ,obsPayment));
        });
    }

    private void setupItemsSale(){
        itemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        itemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemDiscount.setCellValueFactory(new PropertyValueFactory<>("discountString"));
        itemValue.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tableItems.setItems(obsItemSale);

        int i = 0;
        for (SaleItem saleItem : sale.getSaleItems()) {
            obsItemSale.add(new ItemSaleField(++i, saleItem));
        }
    }

    @FXML
    private void cancel(){
        this.stage.close();
    }
}
