package application.controller.object;

import application.model.Model;

import java.sql.Timestamp;

public class Sale implements Model {
    private int id;
    private double value;
    private double discount;
    private Timestamp date;
    private CashMovement cashMovement;
    private Access access;
    private Customer customer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public CashMovement getCashMovement() {
        return cashMovement;
    }

    public void setCashMovement(CashMovement cashMovement) {
        this.cashMovement = cashMovement;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
