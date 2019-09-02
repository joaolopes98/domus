package application.view.controller;

import application.controller.CustomerField;
import application.controller.object.Animal;
import application.controller.object.Customer;
import application.model.AnimalModel;
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

public class EditAnimalController extends Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtSpecie;
    @FXML private TextField txtCustomer;

    @FXML private AnchorPane selectCustomer;
    @FXML private TextField txtSearch;
    @FXML private TableView<CustomerField> tableCustomers;
    @FXML private TableColumn<CustomerField, String> customerName;
    @FXML private TableColumn<CustomerField, String> customerDocument;
    @FXML private TableColumn<CustomerField, String> customerPhone;
    private ObservableList<CustomerField> obsCustomer = FXCollections.observableArrayList();
    private Customer selectedCustomer;

    private Animal animal;
    @FXML private AnchorPane waitScreen;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        animal = (Animal) objects[0];
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        setupInputs();
        setupTxtSearch();
        setupTableCustomers();
    }

    private void setupInputs() {
        Mask.onlyLetters(txtName);
        Mask.onlyLetters(txtSpecie);
        txtCustomer.setOnMouseClicked( e -> {
            selectedCustomer = null;
            txtCustomer.setText("");
        });

        txtName.setText(animal.getName());
        txtSpecie.setText(animal.getSpecie());
        txtCustomer.setText(animal.getCustomer().getName());
        selectedCustomer = animal.getCustomer();
    }

    @FXML private void selectCustomer(){
        selectCustomer.setVisible(!selectCustomer.isVisible());
    }

    private void setupTxtSearch(){
        Mask.upperCase(txtSearch);
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> updateTableCustomers()));
    }

    private void setupTableCustomers() {
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerDocument.setCellValueFactory(new PropertyValueFactory<>("document"));
        customerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tableCustomers.setItems(obsCustomer);

        tableCustomers.setRowFactory( e -> {
            TableRow<CustomerField> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    selectedCustomer = row.getItem().getCustomer();
                    txtCustomer.setText(selectedCustomer.getName());
                    selectCustomer.setVisible(false);
                }
            });
            return row ;
        });

        updateTableCustomers();
    }

    private void updateTableCustomers() {
        obsCustomer.clear();
        ArrayList<Customer> customers = new ArrayList<>();
        if(txtSearch.getText().isEmpty()){
            customers.addAll(CustomerModel.getAll(""));
        } else {
            if(txtSearch.getText().matches("\\D+")) {
                customers.addAll(CustomerModel.getAll("WHERE name LIKE '%" + txtSearch.getText() + "%'"));
            } else {
                customers.addAll(CustomerModel.getAll(
                        "WHERE document LIKE '%" + Formatter.unmaskOnlyNumber(txtSearch.getText()) + "%' " +
                                "OR phone LIKE '%" + Formatter.unmaskOnlyNumber(txtSearch.getText()) + "%'"));
            }
        }
        customers.forEach( customer -> obsCustomer.add(new CustomerField(customer, this)));
        this.tableCustomers.refresh();
        this.tableCustomers.getSelectionModel().clearSelection();
    }

    @FXML private void create (){
        if(!txtName.getText().isEmpty()){
            animal.setName(txtName.getText());
            if(!txtSpecie.getText().isEmpty()){
                animal.setSpecie(txtSpecie.getText());

                if(selectedCustomer != null){
                    animal.setCustomer(selectedCustomer);
                    animal.setStatus(true);

                    if(AnimalModel.update(animal)){
                        this.stage.close();
                    } else {
                        Window.changeScene(this.stage, "error", this,
                                "Não Foi Possivel Cadastrar Animal");
                    }
                } else {
                    Window.changeScene(this.stage, "error", this,
                            "É Nescessario Vincular o Animal a um Cliente");
                }
            } else {
                Window.changeScene(this.stage, "error", this,
                        "É Nescessario Espécie do Animal");
            }
        } else {
            Window.changeScene(this.stage, "error", this,
                    "É Nescessario Nome do Animal");
        }
    }

    @FXML private void cancel(){
        this.stage.close();
    }

    @Override
    public void activeWaitScreen(boolean wait) {
        waitScreen.setVisible(wait);
    }
}
