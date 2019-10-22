package application.view.controller;

import application.controller.ScheduleField;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.Date;

public class ScheduleController extends Controller {

    @FXML private JFXDatePicker txtDate;

    @FXML private TableView<ScheduleField> tableSchedule;
    @FXML private TableColumn<ScheduleField, String> scheduleTime;
    @FXML private TableColumn<ScheduleField, String> scheduleCustomer;
    @FXML private TableColumn<ScheduleField, String> scheduleAction;
    private ObservableList<ScheduleField> obsSchedule = FXCollections.observableArrayList();

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

        setupInput();
        searchSchedule();
    }

    private void setupInput() {
        txtDate.getEditor().setText(Formatter.formatDate(new Date()));
        txtDate.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            searchSchedule();
        });
    }

    private void searchSchedule() {
        Date date = Formatter.toDate(txtDate.getEditor().getText());
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Formatter.resetDate(date));

            do {
                System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
                calendar.add(Calendar.HOUR_OF_DAY, 1);
            } while (calendar.get(Calendar.HOUR_OF_DAY) != 0);
        }
    }

    @FXML private void lastDate(){
        Date date = Formatter.toDate(txtDate.getEditor().getText());
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            date = calendar.getTime();
            txtDate.getEditor().setText(Formatter.formatDate(date));
        }

    }

    @FXML private void nextDate(){
        Date date = Formatter.toDate(txtDate.getEditor().getText());
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            date = calendar.getTime();
            txtDate.getEditor().setText(Formatter.formatDate(date));
        }
    }

    @FXML private void cancel(){
        this.stage.close();
    }
}
