package application.view.controller;

import application.controller.CustomerField;
import application.controller.object.Customer;
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

public class CustomersController extends Controller {

    @FXML private TextField txtSearch;
    @FXML private TableView<CustomerField> tableCustomers;
    @FXML private TableColumn<CustomerField, Integer> customerCode;
    @FXML private TableColumn<CustomerField, String> customerName;
    @FXML private TableColumn<CustomerField, String> customerDocument;
    @FXML private TableColumn<CustomerField, String> customerPhone;
    @FXML private TableColumn<CustomerField, HBox> customerAction;
    private ObservableList<CustomerField> obsCustomer = FXCollections.observableArrayList();

    @FXML private AnchorPane waitScreen;

    @Override
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
        setupTableCustomers();
    }

    private void setupTxtSearch(){
        Mask.upperCase(txtSearch);
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            updateTable();
            tableCustomers.refresh();
        }));
    }

    private void setupTableCustomers() {
        customerCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerDocument.setCellValueFactory(new PropertyValueFactory<>("document"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        tableCustomers.setItems(obsCustomer);

        tableCustomers.setRowFactory( new Callback<TableView<CustomerField>, TableRow<CustomerField>>() {
            @Override
            public TableRow<CustomerField> call(TableView<CustomerField> tableView) {
                return new TableRow<CustomerField>() {
                    @Override
                    protected void updateItem(CustomerField productField, boolean empty) {
                        super.updateItem(productField, empty);
                        if(!empty) {
                            getStyleClass().clear();
                            if (productField.getCustomer().isStatus()) {
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
        obsCustomer.clear();
        ArrayList<Customer> customers = new ArrayList<>();
        if(txtSearch.getText().isEmpty()){
            customers.addAll(CustomerModel.getAll(""));
        } else {
            customers.addAll(CustomerModel.getAll("WHERE name LIKE '%" + txtSearch.getText() + "%' " +
                    "OR document LIKE '%" + Formatter.unmaskOnlyNumber(txtSearch.getText()) + "%' " +
                    "OR phone LIKE '%" + Formatter.unmaskOnlyNumber(txtSearch.getText()) + "%'"));
        }
        customers.forEach( customer -> obsCustomer.add(new CustomerField(customer, this)));
    }

    @FXML private void createCustomer(){
        Window.changeScene(this.stage, "createCustomer", this);
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
