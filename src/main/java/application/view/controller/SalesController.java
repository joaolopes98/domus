package application.view.controller;

import application.controller.SaleField;
import application.controller.object.Sale;
import application.controller.object.Customer;
import application.model.SaleModel;
import application.model.CustomerModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

public class SalesController extends Controller {
    @FXML
    private TextField txtSearch;
    @FXML private TableView<SaleField> tableSales;
    @FXML private TableColumn<SaleField, String> saleCode;
    @FXML private TableColumn<SaleField, String> saleUser;
    @FXML private TableColumn<SaleField, String> saleCustomer;
    @FXML private TableColumn<SaleField, String> saleValue;
    @FXML private TableColumn<SaleField, HBox> saleAction;
    private ObservableList<SaleField> obsSale = FXCollections.observableArrayList();

    @FXML private AnchorPane waitScreen;

    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        setupTxtSearch();
        setupTableSales();
    }

    private void setupTxtSearch(){
        Mask.upperCase(txtSearch);
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> updateTable()));
    }

    private void setupTableSales() {
        saleCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        saleUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        saleCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        saleValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        saleAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        tableSales.setItems(obsSale);

        tableSales.setRowFactory( new Callback<TableView<SaleField>, TableRow<SaleField>>() {
            @Override
            public TableRow<SaleField> call(TableView<SaleField> tableView) {
                return new TableRow<SaleField>() {
                    @Override
                    protected void updateItem(SaleField saleField, boolean empty) {
                        super.updateItem(saleField, empty);
                        if(!empty) {
                            getStyleClass().clear();
                            if (saleField.getSale().isActive()) {
                                getStyleClass().add("activated");
                            } else {
                                getStyleClass().add("disabled");
                            }
                        }
                    }
                };
            }
        });

        updateTable();
    }

    public void updateTable() {
        obsSale.clear();
        ArrayList<Sale> sales = new ArrayList<>();
        if(txtSearch.getText().isEmpty()){
            sales.addAll(SaleModel.getAll(""));
        } else {
            if(txtSearch.getText().matches("\\D+")) {
                sales.addAll(SaleModel.getAll("WHERE name LIKE '%" + txtSearch.getText() + "%' " +
                        "OR specie LIKE '%" + txtSearch.getText() + "%'"));

                ArrayList<Customer> customers = new ArrayList<>(
                        CustomerModel.getAll(
                                "WHERE name LIKE '%" + txtSearch.getText() + "%'"));
                customers.forEach( customer -> sales.addAll(customer.getSales()));
            } else {
                ArrayList<Customer> customers = new ArrayList<>(
                        CustomerModel.getAll(
                                "WHERE document LIKE '%" +
                                        Formatter.unmaskOnlyNumber(txtSearch.getText()) + "%'"));
                customers.forEach( customer -> sales.addAll(customer.getSales()));
            }
        }
        sales.forEach( sale -> obsSale.add(new SaleField(sale, this)));
        this.tableSales.refresh();
    }

    @FXML private void cancel(){
        this.stage.close();
    }

    @Override
    public void activeWaitScreen(boolean wait) {
        waitScreen.setVisible(wait);

        if(!wait){
            updateTable();
        }
    }
}
