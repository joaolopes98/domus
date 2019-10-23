package application.controller;

import application.controller.object.ScheduleItems;

public class ScheduleItemField {

    private ScheduleItems scheduleItems;

    private String name;
    private int quantity;

    public ScheduleItemField(ScheduleItems scheduleItems){
        this.scheduleItems = scheduleItems;
        this.name = scheduleItems.getService().getName();
        this.quantity = scheduleItems.getQuantity();
    }

    public ScheduleItems getScheduleItems() {
        return scheduleItems;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
