package application.controller;

import application.controller.object.Schedule;
import application.model.ScheduleModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.controller.ScheduleController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Date;

public class ScheduleField {

    private Schedule schedule;

    private String hour;
    private String user;
    private String customer;
    private Boolean status;

    private HBox action = new HBox();

    public ScheduleField(Schedule schedule, Controller controller){
        this.schedule = schedule;
        this.hour = Formatter.formatHour(schedule.getFrom_date()) + " - " + Formatter.formatHour(schedule.getTo_date());
        this.user = schedule.getAccess().getName();
        this.customer = schedule.getCustomer().getName();
        this.status = schedule.getStatus();

        if(controller instanceof ScheduleController) {
            ScheduleController scheduleController = (ScheduleController) controller;
            if (status == null) {
                Button btnDelete = new Button();
                ImageView imageRemove = new ImageView(new Image("/view/img/trash.png"));
                imageRemove.setFitHeight(25);
                imageRemove.setFitWidth(25);
                btnDelete.setGraphic(imageRemove);
                btnDelete.getStyleClass().add("btnRed");
                btnDelete.setOnAction( e -> {
                    schedule.setStatus(false);
                    if(ScheduleModel.update(schedule)) {
                        scheduleController.searchSchedule();
                    }
                });
                btnDelete.setMinSize(35, 35);
                btnDelete.setMaxSize(35, 35);


                Button btnConfirm = new Button();
                ImageView imageConfirm = new ImageView(new Image("/view/img/active.png"));
                imageConfirm.setFitHeight(25);
                imageConfirm.setFitWidth(25);
                btnConfirm.setGraphic(imageConfirm);
                btnConfirm.getStyleClass().add("btnGreen");
                btnConfirm.setOnAction( e -> {
                    scheduleController.setPDVSchedule(this.schedule);
                });
                btnConfirm.setMinSize(35, 35);
                btnConfirm.setMaxSize(35, 35);

                this.action.getChildren().addAll(btnConfirm, btnDelete);
                this.action.setAlignment(Pos.CENTER_LEFT);
                this.action.setSpacing(10);
            }
        }
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getHour() {
        return hour;
    }

    public String getUser() {
        return user;
    }

    public String getCustomer() {
        return customer;
    }

    public HBox getAction() {
        return action;
    }

    public Boolean getStatus() {
        return status;
    }
}
