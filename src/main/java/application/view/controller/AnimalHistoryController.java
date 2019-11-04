package application.view.controller;

import application.controller.AnimalItemField;
import application.controller.object.Animal;
import application.controller.object.SaleItem;
import application.model.SaleItemModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AnimalHistoryController extends Controller {

    private Animal animal;

    @FXML private TableView<AnimalItemField> tableItem;
    @FXML private TableColumn<AnimalItemField, String> itemUser;
    @FXML private TableColumn<AnimalItemField, String> itemName;
    @FXML private TableColumn<AnimalItemField, String> itemDate;
    private ObservableList<AnimalItemField> itemFields = FXCollections.observableArrayList();

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.animal = (Animal) objects[0];
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        setupTable();
    }

    private void setupTable() {
        itemUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableItem.setItems(itemFields);

        ArrayList<SaleItem> saleItems = new ArrayList<>();
        SaleItemModel.getAll("WHERE animal = " + animal.getId()).forEach( saleItem -> {
            itemFields.add(new AnimalItemField(saleItem));
        });
    }

    @FXML private void cancel(){
        this.stage.close();
    }
}
