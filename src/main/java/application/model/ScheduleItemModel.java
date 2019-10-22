package application.model;

import application.controller.object.Schedule;
import application.controller.object.ScheduleItems;

import java.util.List;

public abstract class ScheduleItemModel {
    private static boolean create(ScheduleItems scheduleItems){
        return GenericModel.create(scheduleItems);
    }

    private static boolean update(ScheduleItems scheduleItems){
        return GenericModel.update(scheduleItems);
    }

    public static List<Schedule> getAll(String search) {
        return GenericModel.getAll("FROM ScheduleItems " + search);
    }
}
