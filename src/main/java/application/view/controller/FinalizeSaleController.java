package application.view.controller;

import application.controller.ItemSaleField;
import application.controller.PaymentField;
import application.controller.object.*;
import application.model.*;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
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
    @FXML private Button btnAddPayment;

    @FXML private Label lblInfo;
    @FXML private Label lblTotal;

    @FXML private Button btnFinalize;

    private double totalRoot;
    private double payments;

    private double total;
    private double discount;

    private ArrayList<Product> changeStock = new ArrayList<>();

    private PDVController pdv;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.pdv = (PDVController) oldController;
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
        setupButton();
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
                e.consume();
            }
        });
        txtDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
            updateValues();
        });

        Mask.money(txtPaymentValue);
        txtPaymentValue.setText(Formatter.formatMoney(this.totalRoot));
        txtPaymentValue.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB){
                Mask.toLastPosition(txtDiscount);
                e.consume();
            } else if (e.getCode() == KeyCode.ENTER){
                addPayment();
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
        comboPayment.valueProperty().addListener((observable -> Mask.toLastPosition(txtPaymentValue)));
    }

    private void setupButton(){
        btnAddPayment.disableProperty().bind(txtPaymentValue.disabledProperty());
        btnAddPayment.setOnAction( e -> addPayment());
    }

    private void setupInfos(){
        lblTotal.setText(Formatter.formatMoney(this.totalRoot));
    }

    private void updateValues(){
        double discount = Formatter.unmaskMoney(this.txtDiscount.getText());

        if(discount > this.totalRoot){
            this.txtDiscount.setText(Formatter.formatMoney(this.totalRoot));
        }

        this.discount = Formatter.unmaskMoney(this.txtDiscount.getText());
        this.total = this.totalRoot - this.discount;

        this.payments = 0;
        obsPayments.forEach( payment -> this.payments += Formatter.unmaskMoney(payment.getValue()));

        this.total -= this.payments;
        if(this.total <= 0){
            lblInfo.setText("TROCO");
            lblTotal.setText(Formatter.formatMoney(this.total));
            txtPaymentValue.setText(Formatter.formatMoney(0));
            txtPaymentValue.setDisable(true);
            btnFinalize.setDisable(false);
        } else {
            lblInfo.setText("TOTAL");
            lblTotal.setText(Formatter.formatMoney(this.total));
            txtPaymentValue.setText(Formatter.formatMoney(this.total));
            txtPaymentValue.setDisable(false);
            btnFinalize.setDisable(true);
        }
    }

    private void addPayment(){
        PaymentMethod paymentSelected = comboPayment.getSelectionModel().getSelectedItem();
        String value = txtPaymentValue.getText();
        PaymentField paymentField = new PaymentField(paymentSelected, value, this.obsPayments);

        obsPayments.add(paymentField);
        updateValues();
    }

    @FXML private void finalizeSale(){
        Timestamp now = new Timestamp(new Date().getTime());
        CashMovement cashMovement = CashMovementModel.getOpened();

        Sale sale = new Sale();
        sale.setValue(this.totalRoot);
        sale.setDiscount(Formatter.unmaskMoney(txtDiscount.getText()));
        sale.setDate(now);
        sale.setCashMovement(cashMovement);
        sale.setAccess(User.getUser());
        sale.setCustomer(this.pdv.getLinkedCustomer());
        sale.setActive(true);

        for(ItemSaleField item: obsSale){
            SaleItem saleItem = new SaleItem();
            saleItem.setPrice(Formatter.unmaskMoney(item.getPrice()));
            saleItem.setQuantity(item.getQuantity());
            saleItem.setDiscount(Formatter.unmaskMoney(item.getDiscount().getText()));
            saleItem.setSubtotal(Formatter.unmaskMoney(item.getSubtotal()));
            saleItem.setSale(sale);

            if(item.isTypeProduct()) {
                saleItem.setProduct(item.getProduct());
                if(!changeStock.contains(item.getProduct())){
                    changeStock.add(item.getProduct());
                }
                // Diminuir Estoque
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() - item.getQuantity());
            } else {
                saleItem.setService(item.getService());
                saleItem.setAnimal(item.getLinkedAnimal());
            }

            sale.getSaleItems().add(saleItem);
        }

        for(PaymentField payment : obsPayments){
            FinancialInflow financialInflow = new FinancialInflow();
            financialInflow.setValue(Formatter.unmaskMoney(payment.getValue()));
            financialInflow.setDate(now);
            financialInflow.setCashMovement(cashMovement);
            financialInflow.setSale(sale);
            financialInflow.setPaymentMethod(payment.getPayment());
            financialInflow.setAccess(User.getUser());

            sale.getFinancialInflows().add(financialInflow);
        }

        Schedule schedule = pdv.getLinkedSchedule();
        if(schedule != null){
            sale.setSchedule(schedule);
            schedule.setStatus(true);
            ScheduleModel.update(schedule);
        }

        if(SaleModel.create(sale)){

            changeStock.forEach(ProductModel::update);
            this.obsSale.clear();
            this.cancel();
        } else {
            this.cancel();
            Window.changeScene(this.oldStage, "error", this,
                    "Não foi possivel finalizar venda");
        }
    }

    @FXML private void cancel(){
        this.stage.close();
    }
}
