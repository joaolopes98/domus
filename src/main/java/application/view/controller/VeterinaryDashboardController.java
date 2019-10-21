package application.view.controller;

import application.controller.MedicineField;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class VeterinaryDashboardController extends Controller {

    @FXML private Button btnLinkAnimal;

    @FXML private TableView<MedicineField> tableMedicine;
    @FXML private TableColumn<MedicineField, TextField> medName;
    @FXML private TableColumn<MedicineField, HBox> medQuantity;
    @FXML private TableColumn<MedicineField, HBox> medTime;
    @FXML private TableColumn<MedicineField, Button> medDelete;
    private ObservableList<MedicineField> obsMedicine = FXCollections.observableArrayList();

    @FXML private AnchorPane waitScreen;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.F11){
                stage.setFullScreen(!stage.isFullScreen());
                e.consume();
            }
        });

        this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.stage.setTitle("DOMUS PDV");
        this.stage.setMinWidth(1000);
        this.stage.setMinHeight(720);

        super.initialize(oldStage, scene, oldController, objects);

        this.stage.setMaximized(true);
        this.stage.setFullScreen(true);
        this.stage.setOnCloseRequest( e -> logout());

        setupTable();
    }

    private void setupTable() {
        medName.setCellValueFactory(new PropertyValueFactory<>("txtName"));
        medQuantity.setCellValueFactory(new PropertyValueFactory<>("paneQuantity"));
        medTime.setCellValueFactory(new PropertyValueFactory<>("paneTime"));
        medDelete.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));
        tableMedicine.setItems(obsMedicine);
    }

    @FXML private void customer(){
        Window.changeScene(this.stage, "customers", this);
    }

    @FXML private void animal(){
        Window.changeScene(this.stage, "animals", this);
    }

    @FXML private void linkAnimal(){

    }

    @FXML private void addMedicine(){
        obsMedicine.add(new MedicineField(obsMedicine));
    }

    @FXML private void print(){
        obsMedicine.clear();
    }

    @FXML private void logout(){
        this.stage.close();
        Window.changeScene(this.stage, "initial", null);
    }

    @Override
    public void activeWaitScreen(boolean wait) {
        waitScreen.setVisible(wait);
    }
}
