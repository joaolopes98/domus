package application.view.controller;

import application.controller.ItemSaleField;
import application.controller.PaymentField;
import application.controller.object.*;
import application.model.CashMovementModel;
import application.model.PaymentMethodModel;
import application.model.SaleModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class FinalizeSaleController extends Controller {

    @FXML private TableView<ItemSaleField> tableItem;
    @FXML private TableColumn<ItemSaleField, String> codeItem;
    @FXML private TableColumn<ItemSaleField, String> nameItem;
    @FXML private TableColumn<ItemSaleField, String> subtotalItem;
    private ObservableList<ItemSaleField> obsSale;

    @FXML private TableView<PaymentField> tablePayments;
    @FXML private TableColumn<PaymentField, String> paymentName;
    @FXML private TableColumn<PaymentField, String> paymentValue;
    @FXML private TableColumn<PaymentField, Button> paymentRemove;
    private ObservableList<PaymentField> obsPayments = FXCollections.observableArrayList();


    @FXML private TextField txtDiscount;
    @FXML private TextField txtPaymentValue;
    @FXML private ComboBox<PaymentMethod> comboPayment;

    @FXML private Label lblInfo;
    @FXML private Label lblTotal;

    @FXML private Button btnFinalize;

    private double totalRoot;
    private double payments;

    private double total;
    private double discount;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        PDVController pdv = (PDVController) oldController;
        this.obsSale = pdv.getObsSaleFields();
        this.totalRoot = pdv.getTotal();
        this.total = pdv.getTotal();
        this.discount = pdv.getDiscount();
        pdv.activeWaitScreen(true);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            } else if (e.getCode() == KeyCode.F11){
                stage.setFullScreen(!stage.isFullScreen());
                e.consume();
            }
        });

        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);



        setupTableItems();
        setupTablePayments();
        setupInputs();
        setupComboBox();
        setupInfos();
        updateValues();
    }

    private void setupTableItems(){
        codeItem.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        subtotalItem.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tableItem.setItems(obsSale);
    }

    private void setupTablePayments(){
        paymentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        paymentValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        paymentRemove.setCellValueFactory(new PropertyValueFactory<>("delete"));
        tablePayments.setItems(this.obsPayments);

        obsPayments.addListener((ListChangeListener<PaymentField>) c -> updateValues());
    }

    private void setupInputs(){
        Mask.money(txtDiscount);
        txtDiscount.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB || e.getCode() == KeyCode.ENTER){
                Mask.toLastPosition(txtPaymentValue);
                System.out.println("AKI");
                e.consume();
            }
        });
        txtDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
            updateValues();
        });

        Mask.money(txtPaymentValue);
        txtPaymentValue.setText(Mask.formatDoubleToMoney(this.totalRoot));
        txtPaymentValue.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB){
                Mask.toLastPosition(txtDiscount);
                e.consume();
            } else if (e.getCode() == KeyCode.ENTER){
                addPayment();
                updateValues();
                e.consume();
            }
        });
        Mask.toLastPosition(txtPaymentValue);
    }

    private void setupComboBox() {
        ObservableList<PaymentMethod> obsPayment = FXCollections.observableArrayList(PaymentMethodModel.getAll());
        Callback<ListView<PaymentMethod>, ListCell<PaymentMethod>> cellFactoryPaymentMethod = new Callback<ListView<PaymentMethod>, ListCell<PaymentMethod>>() {
            @Override
            public ListCell<PaymentMethod> call(ListView<PaymentMethod> l) {
                return new ListCell<PaymentMethod>() {
                    @Override
                    protected void updateItem(PaymentMethod item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                } ;
            }
        };
        comboPayment.setButtonCell(cellFactoryPaymentMethod.call(null));
        comboPayment.setCellFactory(cellFactoryPaymentMethod);
        comboPayment.setItems(obsPayment);
        comboPayment.getSelectionModel().selectFirst();
    }

    private void setupInfos(){
        lblTotal.setText(Mask.formatDoubleToMoney(this.totalRoot));
    }

    private void updateValues(){
        double discount = Mask.unmaskMoney(this.txtDiscount.getText());

        if(discount > this.totalRoot){
            this.txtDiscount.setText(Mask.formatDoubleToMoney(this.totalRoot));
        }

        this.discount = Mask.unmaskMoney(this.txtDiscount.getText());
        this.total = this.totalRoot - this.discount;

        this.payments = 0;
        obsPayments.forEach( payment -> this.payments += Mask.unmaskMoney(payment.getValue()));

        this.total -= this.payments;
        if(this.total <= 0){
            lblInfo.setText("TROCO");
            lblTotal.setText(Mask.formatDoubleToMoney(this.total));
            txtPaymentValue.setText(Mask.formatDoubleToMoney(0));
            txtPaymentValue.setDisable(true);
            btnFinalize.setDisable(false);
        } else {
            lblInfo.setText("TOTAL");
            lblTotal.setText(Mask.formatDoubleToMoney(this.total));
            txtPaymentValue.setText(Mask.formatDoubleToMoney(this.total));
            txtPaymentValue.setDisable(false);
            btnFinalize.setDisable(true);
        }
    }

    private void addPayment(){
        PaymentMethod paymentSelected = comboPayment.getSelectionModel().getSelectedItem();
        String value = txtPaymentValue.getText();
        PaymentField paymentField = new PaymentField(paymentSelected, value, this.obsPayments);

        obsPayments.add(paymentField);
    }

    @FXML private void finalizeSale(){
        Timestamp now = new Timestamp(new Date().getTime());
        CashMovement cashMovement = CashMovementModel.getOpened();

        Sale sale = new Sale();
        sale.setValue(this.totalRoot);
        sale.setDiscount(Mask.unmaskMoney(txtDiscount.getText()));
        sale.setDate(now);
        sale.setCashMovement(cashMovement);
        sale.setAccess(User.getUser());

        for(ItemSaleField item: obsSale){
            SaleItem saleItem = new SaleItem();
            saleItem.setPrice(item.getProduct().getPrice());
            saleItem.setQuantity(item.getQuantity());
            saleItem.setDiscount(Mask.unmaskMoney(item.getDiscount().getText()));
            saleItem.setSubtotal(Mask.unmaskMoney(item.getSubtotal()));
            saleItem.setSale(sale);
            saleItem.setProduct(item.getProduct());


            sale.getSaleItems().add(saleItem);
        }

        for(PaymentField payment : obsPayments){
            FinancialInflow financialInflow = new FinancialInflow();
            financialInflow.setValue(Mask.unmaskMoney(payment.getValue()));
            financialInflow.setDate(now);
            financialInflow.setCashMovement(cashMovement);
            financialInflow.setSale(sale);
            financialInflow.setPaymentMethod(payment.getPayment());
            financialInflow.setAccess(User.getUser());

            sale.getFinancialInflows().add(financialInflow);
        }

        if(SaleModel.create(sale)){
            this.obsSale.clear();
            this.cancel();
        } else {
            Window.changeScene(this.stage, "error", this, "Teste de Funcionamento");
        }


    }

    @FXML private void cancel(){
        this.stage.close();
    }
}
