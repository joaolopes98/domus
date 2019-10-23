package application.controller.object;

import application.model.Model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Schedule implements Model {

    private int id;
    private Customer customer;
    private Timestamp date;
    private Boolean status;

    private Set<ScheduleItems> scheduleItems = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<ScheduleItems> getScheduleItems() {
        return scheduleItems;
    }

    public void setScheduleItems(Set<ScheduleItems> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }
}
