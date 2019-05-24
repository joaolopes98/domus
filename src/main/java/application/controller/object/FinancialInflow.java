package application.controller.object;

import application.model.Model;

import java.sql.Timestamp;

public class FinancialInflow implements Model {
    private int id;
    private double value;
    private Timestamp date;
    private CashMovement cashMovement;
    private Sale sale;
    private PaymentMethod paymentMethod;
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }
}
