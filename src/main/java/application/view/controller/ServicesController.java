package application.view.controller;

import application.controller.ServiceField;
import application.controller.object.Service;
import application.model.ServiceModel;
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

public class ServicesController extends Controller {

    @FXML
    private TextField txtSearch;
    @FXML private TableView<ServiceField> tableServices;
    @FXML private TableColumn<ServiceField, Integer> serviceCode;
    @FXML private TableColumn<ServiceField, String> serviceName;
    @FXML private TableColumn<ServiceField, String> serviceDescription;
    @FXML private TableColumn<ServiceField, String> servicePrice;
    @FXML private TableColumn<ServiceField, HBox> serviceAction;
    private ObservableList<ServiceField> obsService = FXCollections.observableArrayList();

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
        setupTableServiceModels();
    }

    private void setupTxtSearch(){
        Mask.upperCase(txtSearch);
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            updateTable();
        }));
    }

    private void setupTableServiceModels() {
        serviceCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        serviceName.setCellValueFactory(new PropertyValueFactory<>("name"));
        serviceDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        servicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        serviceAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        tableServices.setItems(obsService);

        tableServices.setRowFactory(new Callback<TableView<ServiceField>, TableRow<ServiceField>>() {
            @Override
            public TableRow<ServiceField> call(TableView<ServiceField> tableView) {
                return new TableRow<ServiceField>() {
                    @Override
                    protected void updateItem(ServiceField serviceField, boolean empty) {
                        super.updateItem(serviceField, empty);
                        if(!empty) {
                            getStyleClass().clear();
                            if (serviceField.getService().isStatus()) {
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
        obsService.clear();
        ArrayList<Service> services = new ArrayList<>();
        if(txtSearch.getText().isEmpty()){
            services.addAll(ServiceModel.getAll(""));
        } else {
            services.addAll(ServiceModel.getAll("WHERE name LIKE '%" + txtSearch.getText() + "%'"));
        }
        services.forEach( service -> obsService.add(new ServiceField(service, this)));
        this.tableServices.refresh();
    }

    @FXML private void createService(){
        Window.changeScene(this.stage, "createService", this);
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
