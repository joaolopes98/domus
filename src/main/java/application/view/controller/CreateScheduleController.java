package application.view.controller;

import application.controller.ItemSaleField;
import application.controller.ItemSearchField;
import application.controller.object.Customer;
import application.controller.object.Schedule;
import application.controller.object.ScheduleItems;
import application.controller.object.Service;
import application.model.ScheduleModel;
import application.model.ServiceModel;
import application.view.auxiliary.*;
import application.view.auxiliary.Formatter;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.*;

public class CreateScheduleController extends Controller {

    @FXML private Button btnLinkCustomer;
    @FXML private TextField txtSearch;
    @FXML private TextField txtQuantity;
    @FXML private JFXTimePicker txtTime;

    private Customer linkedCustomer;

    @FXML private TableView<ItemSearchField> tableSearch;
    @FXML private TableColumn<ItemSearchField, String> searchProduct;
    @FXML private TableColumn<ItemSearchField, String> searchValue;
    private ObservableList<ItemSearchField> obsSearchFields = FXCollections.observableArrayList();
    private ObservableList<Service> obsSearchItems = FXCollections.observableArrayList();
    private ItemSearchField selectedSearch;

    @FXML private TableView<ItemSaleField> tableItem;
    @FXML private TableColumn<ItemSaleField, String> itemProduct;
    @FXML private TableColumn<ItemSaleField, Integer> itemQuantity;
    @FXML private TableColumn<ItemSaleField, Button> itemAction;
    private ObservableList<ItemSaleField> obsSaleFields = FXCollections.observableArrayList();

    private Date dateSchedule;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.dateSchedule = (Date) objects[0];
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        setupInput();
        setupSearch();
        setupSale();
    }

    private void setupInput() {
        txtTime.getEditor().setText(Formatter.formatHour(new Date()));
    }

    private void setupSearch(){
        tableSearch.visibleProperty().addListener((observable, oldValue, newValue) -> {
            obsSearchFields.clear();
            if(newValue){
                String search = txtSearch.getText();
                obsSearchItems.forEach(service -> {
                    if (service.getName().contains(search)){
                        obsSearchFields.add(new ItemSearchField(service));
                    }
                });
            }
            tableSearch.getSelectionModel().select(0);
        });

        //Configurações dos Inputs
        txtSearch.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB){
                e.consume();
                txtQuantity.requestFocus();
            } else if (e.getCode() == KeyCode.UP){
                e.consume();
                if(tableSearch.isVisible()){
                    int posSelected = tableSearch.getSelectionModel().getSelectedIndex();
                    posSelected --;
                    tableSearch.getSelectionModel().select(posSelected);
                }
            } else if (e.getCode() == KeyCode.DOWN){
                e.consume();
                if(tableSearch.isVisible()){
                    int posSelected = tableSearch.getSelectionModel().getSelectedIndex();
                    posSelected++;
                    tableSearch.getSelectionModel().select(posSelected);
                }
            } else if (e.getCode() == KeyCode.ENTER){
                e.consume();

                if(selectedSearch == null) {
                    if (tableSearch.isVisible()) {
                        selectedSearch = tableSearch.getSelectionModel().getSelectedItem();
                        txtSearch.setText(selectedSearch.getName());
                        Mask.toLastPosition(txtSearch);
                        txtQuantity.requestFocus();
                        tableSearch.setVisible(false);
                    } else {
                        selectedSearch = null;
                        if(!txtSearch.getText().isEmpty()) {
                            tableSearch.setVisible(true);
                            tableSearch.getSelectionModel().select(0);
                        }
                    }
                } else {
                    if(txtSearch.getText().equals(selectedSearch.getName())) {
                        int quantity = Formatter.unmaskInteger(txtQuantity.getText());
                        obsSaleFields.add(new ItemSaleField(1, selectedSearch, quantity, this));
                        resetSearch();
                    } else {
                        selectedSearch = null;
                        tableSearch.setVisible(true);
                        tableSearch.getSelectionModel().select(0);
                    }
                }
            }
        });
        txtSearch.textProperty().addListener( e -> {
            if(txtSearch.getText().isEmpty()){
                tableSearch.setVisible(false);
            } else {
                obsSearchFields.clear();
                String search = txtSearch.getText();
                obsSearchItems.forEach(service -> {
                    if (service.getName().contains(search)){
                        obsSearchFields.add(new ItemSearchField(service));
                    }
                });
                tableSearch.getSelectionModel().select(0);
            }
        });
        txtQuantity.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB){
                txtSearch.requestFocus();
                e.consume();
            } else if(e.getCode() == KeyCode.ENTER){
                if(selectedSearch == null){
                    Mask.toLastPosition(txtSearch);
                    selectedSearch = null;
                    if(!txtSearch.getText().isEmpty()) {
                        tableSearch.setVisible(true);
                        tableSearch.getSelectionModel().select(0);
                    }
                } else {
                    if(txtSearch.getText().equals(selectedSearch.getName())) {
                        int quantity = Formatter.unmaskInteger(txtQuantity.getText());
                        obsSaleFields.add(new ItemSaleField(1, selectedSearch, quantity, this));
                        resetSearch();
                    } else {
                        selectedSearch = null;
                        if(!txtSearch.getText().isEmpty()) {
                            tableSearch.setVisible(true);
                            tableSearch.getSelectionModel().select(0);
                        }
                    }
                }
            }
        });

        Mask.upperCase(txtSearch);
        Mask.zeroTo(txtQuantity, 999);
        //Configurações da Tabela de Busca

        searchProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        searchValue.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableSearch.setItems(obsSearchFields);

        tableSearch.setRowFactory( e -> {
            TableRow<ItemSearchField> row = new TableRow<>();
            row.setOnMouseClicked( f -> {
                if(f.getClickCount() == 1 && !row.isEmpty()){
                    Mask.toLastPosition(txtSearch);
                    f.consume();
                } else if(f.getClickCount() == 2 && !row.isEmpty()){
                    selectedSearch = row.getItem();
                    int quantity = Formatter.unmaskInteger(txtQuantity.getText());
                    obsSaleFields.add(new ItemSaleField(1, selectedSearch, quantity, this));
                    resetSearch();
                    f.consume();
                }
            });
            return row;
        });

        //Configurando Items de Busca
        obsSearchItems.clear();
        obsSearchItems.addAll(ServiceModel.getAllSellable());
    }

    private void setupSale(){
        itemProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemAction.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));
        tableItem.setItems(obsSaleFields);
    }

    public ObservableList<ItemSaleField> getObsSaleFields() {
        return obsSaleFields;
    }

    private void resetSearch(){
        txtSearch.setText("");
        txtQuantity.setText("1");
        selectedSearch = null;
        Mask.toLastPosition(txtSearch);
    }

    @FXML private void linkCustomer(){
        if(linkedCustomer == null) {
            Window.changeScene(this.stage, "linkCustomer", this);
        } else {
            setLinkedCustomer(null);
        }
    }

    public void setLinkedCustomer(Customer linkedCustomer) {
        if(linkedCustomer != null) {
            this.linkedCustomer = linkedCustomer;
            btnLinkCustomer.setText(linkedCustomer.getName());
            btnLinkCustomer.getStyleClass().remove("btnBlue");
            btnLinkCustomer.getStyleClass().add("btnYellow");
        } else {
            btnLinkCustomer.setText("VINCULAR CLIENTE");
            this.linkedCustomer = null;
            btnLinkCustomer.getStyleClass().remove("btnYellow");
            btnLinkCustomer.getStyleClass().add("btnBlue");
        }
    }

    @FXML private void create(){

        if(linkedCustomer == null){
            Window.changeScene(this.stage, "error", this,
                    "Vincule um cliente ao agendamento");
        } else if (!Validator.isHour(txtTime.getEditor().getText())){
            Window.changeScene(this.stage, "error", this,
                    "Esta hora não é valida");
        } else {
            String[] hour = txtTime.getEditor().getText().split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateSchedule);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hour[1]));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Schedule schedule = new Schedule();
            schedule.setCustomer(linkedCustomer);
            schedule.setStatus(null);
            schedule.setDate(new Timestamp(calendar.getTime().getTime()));

            Set<ScheduleItems> scheduleItems = new HashSet<>();
            for(ItemSaleField item : obsSaleFields){
                ScheduleItems items = new ScheduleItems();
                items.setSchedule(schedule);
                items.setService(item.getService());
                items.setQuantity(item.getQuantity());
                scheduleItems.add(items);
            }
            schedule.setScheduleItems(scheduleItems);

            if(ScheduleModel.create(schedule)){
                ScheduleController scheduleController = (ScheduleController) oldController;
                scheduleController.searchSchedule();
                this.stage.close();
            } else {
                Window.changeScene(this.stage, "error", this,
                        "Não foi possivel cadastrar o agendamento");
            }
        }
    }

    @FXML private void cancel(){
        this.stage.close();
    }
}
