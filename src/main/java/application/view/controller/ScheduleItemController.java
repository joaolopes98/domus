package application.view.controller;

import application.controller.ItemSaleField;
import application.controller.ScheduleItemField;
import application.controller.object.Schedule;
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

public class ScheduleItemController extends Controller {

    private Schedule schedule;

    @FXML private TableView<ScheduleItemField> tableItem;
    @FXML private TableColumn<ScheduleItemField, String> itemName;
    @FXML private TableColumn<ScheduleItemField, Integer> itemQuantity;
    private ObservableList<ScheduleItemField> itemFields = FXCollections.observableArrayList();

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.schedule = (Schedule) objects[0];
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
        itemName.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableItem.setItems(itemFields);

        schedule.getScheduleItems().forEach( item -> {
            itemFields.add(new ScheduleItemField(item));
        });
    }

    @FXML private void cancel(){
        this.stage.close();
    }

}
