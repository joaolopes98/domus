package application.controller.object;

import application.model.Model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Sale implements Model {
    private int id;
    private double value;
    private double discount;
    private Timestamp date;
    private CashMovement cashMovement;
    private Access access;
    private Customer customer;

    private Set<SaleItem> saleItems = new HashSet<>();
    private Set<FinancialInflow> financialInflows = new HashSet<>();

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

    public Set<SaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(Set<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }

    public Set<FinancialInflow> getFinancialInflows() {
        return financialInflows;
    }

    public void setFinancialInflows(Set<FinancialInflow> financialInflows) {
        this.financialInflows = financialInflows;
    }
}
