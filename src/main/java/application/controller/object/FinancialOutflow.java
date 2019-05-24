package application.controller.object;

import application.model.Model;

import java.sql.Timestamp;

public class FinancialOutflow implements Model {
    private int id;
    private double value;
    private Timestamp date;
    private CashMovement cashMovement;
    private Access access;

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
}
