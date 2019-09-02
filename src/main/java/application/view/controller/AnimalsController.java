package application.view.controller;

import application.controller.AnimalField;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

public class AnimalsController extends Controller{
    @FXML
    private TextField txtSearch;
    @FXML private TableView<AnimalField> tableAnimals;
    @FXML private TableColumn<AnimalField, String> animalCode;
    @FXML private TableColumn<AnimalField, String> animalName;
    @FXML private TableColumn<AnimalField, String> animalSpecie;
    @FXML private TableColumn<AnimalField, String> animalCustomer;
    @FXML private TableColumn<AnimalField, HBox> animalAction;
    private ObservableList<AnimalField> obsAnimal = FXCollections.observableArrayList();

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
        setupTableAnimals();
    }

    private void setupTxtSearch(){
        Mask.upperCase(txtSearch);
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            updateTable();
        }));
    }

    private void setupTableAnimals() {
        animalCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        animalName.setCellValueFactory(new PropertyValueFactory<>("name"));
        animalSpecie.setCellValueFactory(new PropertyValueFactory<>("specie"));
        animalCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        animalAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        tableAnimals.setItems(obsAnimal);

        tableAnimals.setRowFactory( new Callback<TableView<AnimalField>, TableRow<AnimalField>>() {
            @Override
            public TableRow<AnimalField> call(TableView<AnimalField> tableView) {
                return new TableRow<AnimalField>() {
                    @Override
                    protected void updateItem(AnimalField animalField, boolean empty) {
                        super.updateItem(animalField, empty);
                        if(!empty) {
                            getStyleClass().clear();
                            if (animalField.getAnimal().isStatus()) {
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
        obsAnimal.clear();
        ArrayList<Animal> animals = new ArrayList<>();
        if(txtSearch.getText().isEmpty()){
            animals.addAll(AnimalModel.getAll(""));
        } else {
            if(txtSearch.getText().matches("\\D+")) {
                animals.addAll(AnimalModel.getAll("WHERE name LIKE '%" + txtSearch.getText() + "%' " +
                        "OR specie LIKE '%" + txtSearch.getText() + "%'"));

                ArrayList<Customer> customers = new ArrayList<>(
                        CustomerModel.getAll(
                                "WHERE name LIKE '%" + txtSearch.getText() + "%'"));
                customers.forEach( customer -> animals.addAll(customer.getAnimals()));
            } else {
                ArrayList<Customer> customers = new ArrayList<>(
                        CustomerModel.getAll(
                                "WHERE document LIKE '%" +
                                        Formatter.unmaskOnlyNumber(txtSearch.getText()) + "%'"));
                customers.forEach( customer -> animals.addAll(customer.getAnimals()));
            }
        }
        animals.forEach( animal -> obsAnimal.add(new AnimalField(animal, this)));
        this.tableAnimals.refresh();
    }

    @FXML private void createAnimal(){
        Window.changeScene(this.stage, "createAnimal", this);
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
