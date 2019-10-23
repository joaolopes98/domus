package application.controller;

import application.controller.object.Schedule;
import application.view.auxiliary.Formatter;
import javafx.scene.layout.HBox;

import java.util.Date;

public class ScheduleField {

    private Schedule schedule;

    private String hour;
    private String customer;
    private Boolean status;

    private HBox action;

    public ScheduleField(Schedule schedule){
        this.schedule = schedule;
        this.hour = Formatter.formatHour(schedule.getDate());
        this.customer = schedule.getCustomer().getName();
        this.status = schedule.getStatus();
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getHour() {
        return hour;
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
