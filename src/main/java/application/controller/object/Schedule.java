package application.controller.object;

import application.model.Model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Schedule implements Model {

    private int id;
    private Customer customer;
    private Timestamp from_date;
    private Boolean status;
    private Timestamp to_date;
    private Access access;

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

    public Timestamp getFrom_date() {
        return from_date;
    }

    public void setFrom_date(Timestamp from_date) {
        this.from_date = from_date;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Timestamp getTo_date() {
        return to_date;
    }

    public void setTo_date(Timestamp to_date) {
        this.to_date = to_date;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Set<ScheduleItems> getScheduleItems() {
        return scheduleItems;
    }

    public void setScheduleItems(Set<ScheduleItems> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }
}
