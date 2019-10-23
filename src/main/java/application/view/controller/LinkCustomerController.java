package application.view.controller;

import application.controller.CustomerField;
import application.controller.ItemSearchField;
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
import javafx.stage.Stage;

import java.util.ArrayList;

public class LinkCustomerController extends Controller {

    @FXML private TextField txtSearch;
    @FXML private TableView<CustomerField> tableCustomers;
    @FXML private TableColumn<CustomerField, String> customerCode;
    @FXML private TableColumn<CustomerField, String> customerName;
    @FXML private TableColumn<CustomerField, String> customerDocument;
    @FXML private TableColumn<CustomerField, String> customerPhone;
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
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> updateTable()));
    }

    private void setupTableCustomers() {
        customerCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerDocument.setCellValueFactory(new PropertyValueFactory<>("document"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tableCustomers.setItems(obsCustomer);

        tableCustomers.setRowFactory( e -> {
            TableRow<CustomerField> row = new TableRow<>();
            row.setOnMouseClicked( f -> {
                if(f.getClickCount() == 2 && !row.isEmpty()){
                    linkCustomer();
                }
            });
            return row;
        });

        updateTable();
    }

    private void updateTable() {
        obsCustomer.clear();
        ArrayList<Customer> customers = new ArrayList<>();
        if(txtSearch.getText().isEmpty()){
            customers.addAll(CustomerModel.getAll("WHERE status = TRUE"));
        } else {
            if(txtSearch.getText().matches("\\D+")) {
                customers.addAll(CustomerModel.getAll("WHERE name LIKE '%" + txtSearch.getText() + "%' " +
                        "AND status = TRUE"));
            } else {
                customers.addAll(CustomerModel.getAll(
                        "WHERE (document LIKE '%" + Formatter.unmaskOnlyNumber(txtSearch.getText()) + "%' " +
                                "OR phone LIKE '%" + Formatter.unmaskOnlyNumber(txtSearch.getText()) + "%') " +
                                "AND status = TRUE"));
            }
        }
        customers.forEach( customer -> obsCustomer.add(new CustomerField(customer, this)));
        this.tableCustomers.refresh();
        this.tableCustomers.getSelectionModel().clearSelection();
    }

    @FXML private void linkCustomer(){
        Customer customer = tableCustomers.getSelectionModel().getSelectedItem().getCustomer();

        if(customer != null){
            if(oldController instanceof PDVController) {
                PDVController pdv = (PDVController) oldController;
                pdv.setLinkedCustomer(customer);
            } else if (oldController instanceof CreateScheduleController){
                CreateScheduleController pdv = (CreateScheduleController) oldController;
                pdv.setLinkedCustomer(customer);
            }
            this.stage.close();
        } else {
            Window.changeScene(this.stage, "error", this,
                    "Selecione um Cliente para Vincula-lo");
        }
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
