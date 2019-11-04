package application.view.controller;

import application.controller.ScheduleField;
import application.controller.object.Schedule;
import application.model.ScheduleModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleController extends Controller {

    @FXML private JFXDatePicker txtDate;

    @FXML private TableView<ScheduleField> tableSchedule;
    @FXML private TableColumn<ScheduleField, String> scheduleTime;
    @FXML private TableColumn<ScheduleField, String> scheduleUser;
    @FXML private TableColumn<ScheduleField, String> scheduleCustomer;
    @FXML private TableColumn<ScheduleField, String> scheduleAction;
    private ObservableList<ScheduleField> obsSchedule = FXCollections.observableArrayList();

    @FXML private Button btnCreate;

    @FXML private AnchorPane waitScreen;
    private ScheduleController scheduleController = this;

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
        setupTable();
        searchSchedule();
        resetOlders();
    }

    private void setupInput() {
        txtDate.getEditor().setText(Formatter.formatDate(new Date()));
        txtDate.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            searchSchedule();
        });
    }

    private void setupTable(){
        scheduleTime.setCellValueFactory(new PropertyValueFactory<>("hour"));
        scheduleUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        scheduleCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        scheduleAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        tableSchedule.setRowFactory( new Callback<TableView<ScheduleField>, TableRow<ScheduleField>>() {
            @Override
            public TableRow<ScheduleField> call(TableView<ScheduleField> tableView) {
                return new TableRow<ScheduleField>() {
                    @Override
                    protected void updateItem(ScheduleField scheduleField, boolean empty) {
                        super.updateItem(scheduleField, empty);
                        if(!empty) {
                            Boolean status = scheduleField.getStatus();
                            if(status != null){
                                getStyleClass().clear();
                                if(status){
                                    getStyleClass().add("activated");
                                } else {
                                    getStyleClass().add("disabled");
                                }
                            }
                        }
                        setOnMouseClicked( e -> {
                            if(e.getClickCount() == 2 && !empty){
                                Window.changeScene(stage, "scheduleItem", scheduleController,
                                        getItem().getSchedule());
                                e.consume();
                            }
                        });
                    }
                };
            }
        });
        tableSchedule.setItems(obsSchedule);

    }

    public void searchSchedule() {
        obsSchedule.clear();
        Date date = Formatter.toDate(txtDate.getEditor().getText());
        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Formatter.resetDate(date));

            long first = Formatter.resetDate(calendar.getTime()).getTime();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 0);
            long last = calendar.getTime().getTime();

            ArrayList<Schedule> schedules = new ArrayList<>(
                    ScheduleModel.getAll("WHERE from_date >= '" + first + "' AND from_date <= '" + last + "'" +
                            " ORDER BY from_date ASC"));

            for (Schedule schedule : schedules) {
                obsSchedule.add(new ScheduleField(schedule, this));
            }

            btnCreate.setDisable(first < Formatter.resetDate(new Date()).getTime());
        }
        tableSchedule.refresh();
    }

    private void resetOlders(){
        ScheduleModel.updateAll("SET status = FALSE WHERE from_date < '" +
                Formatter.resetDate(new Date()).getTime() + "' AND status IS NULL");
    }

    public void setPDVSchedule(Schedule schedule){
        PDVController pdvController = (PDVController) oldController;
        pdvController.setLinkedSchedule(schedule);
        this.stage.close();
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

    @FXML private void create(){
        Window.changeScene(this.stage, "createSchedule", this,
                Formatter.toDate(txtDate.getEditor().getText()));
    }

    @Override
    public void activeWaitScreen(boolean wait){
        waitScreen.setVisible(wait);

        if(!wait){
            searchSchedule();
        }
    }
}
