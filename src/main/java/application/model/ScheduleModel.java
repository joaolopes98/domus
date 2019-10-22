package application.model;

import application.controller.object.Schedule;

import java.util.List;

public abstract class ScheduleModel {
    private static boolean create(Schedule schedule){
        return GenericModel.create(schedule);
    }

    private static boolean update(Schedule schedule){
        return GenericModel.update(schedule);
    }

    public static List<Schedule> getAll(String search) {
        return GenericModel.getAll("FROM Schedule " + search);
    }
}
