package application.view.controller;

import application.controller.ItemSaleField;
import application.controller.object.PaymentMethod;
import application.model.PaymentMethodModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FinalizeSaleController extends Controller {

    @FXML private TableView<ItemSaleField> tableItem;
    @FXML private TableColumn<ItemSaleField, String> codeItem;
    @FXML private TableColumn<ItemSaleField, String> nameItem;
    @FXML private TableColumn<ItemSaleField, String> subtotalItem;

    @FXML private TextField txtDiscount;
    @FXML private TextField txtPaymentValue;
    @FXML private ComboBox<PaymentMethod> comboPayment;

    @FXML private Label lblTotal;

    private ObservableList<ItemSaleField> obsSale;
    private double totalRoot;

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
                stage.close();
                e.consume();
            } else if (e.getCode() == KeyCode.F11){
                stage.setFullScreen(!stage.isFullScreen());
                e.consume();
            }
        });

        Window.setModal(this.stage, oldStage);
        super.initialize(oldStage, scene, oldController, objects);

        this.stage.showingProperty().addListener( e -> pdv.activeWaitScreen(false));

        setupTableItems();
        setupInputs();
        setupComboBox();
        setupInfos();
    }

    private void setupTableItems(){
        codeItem.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameItem.setCellValueFactory(new PropertyValueFactory<>("name"));
        subtotalItem.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tableItem.setItems(obsSale);
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
            double discount = Mask.unmaskMoney(this.txtDiscount.getText());

            if(discount > this.totalRoot){
                this.txtDiscount.setText(Mask.formatDoubleToMoney(this.totalRoot));
            }

            this.discount = Mask.unmaskMoney(this.txtDiscount.getText());
            this.total = this.totalRoot - this.discount;
            lblTotal.setText(Mask.formatDoubleToMoney(this.total));
        });

        Mask.money(txtPaymentValue);
        txtPaymentValue.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB){
                Mask.toLastPosition(txtDiscount);
                System.out.println("AKI 2");
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
}
