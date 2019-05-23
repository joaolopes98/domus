package application.controller.object;

import application.model.Model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class CashMovement implements Model {
    private int id;
    private double value;
    private Timestamp date;
    private boolean closed;
    private Timestamp closed_at;
    private double value_closed_input;
    private double value_closed_system;
    private Access opened_by;
    private Access closed_by;

    private Set<Sale> sales = new HashSet<>();
    private Set<FinancialInflow> financialInflows = new HashSet<>();
    private Set<FinancialOutflow> financialOutflows = new HashSet<>();

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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Timestamp getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(Timestamp closed_at) {
        this.closed_at = closed_at;
    }

    public double getValue_closed_input() {
        return value_closed_input;
    }

    public void setValue_closed_input(double value_closed_input) {
        this.value_closed_input = value_closed_input;
    }

    public double getValue_closed_system() {
        return value_closed_system;
    }

    public void setValue_closed_system(double value_closed_system) {
        this.value_closed_system = value_closed_system;
    }

    public Access getOpened_by() {
        return opened_by;
    }

    public void setOpened_by(Access opened_by) {
        this.opened_by = opened_by;
    }

    public Access getClosed_by() {
        return closed_by;
    }

    public void setClosed_by(Access closed_by) {
        this.closed_by = closed_by;
    }

    public Set<Sale> getSales() {
        return sales;
    }

    public void setSales(Set<Sale> sales) {
        this.sales = sales;
    }

    public Set<FinancialInflow> getFinancialInflows() {
        return financialInflows;
    }

    public void setFinancialInflows(Set<FinancialInflow> financialInflows) {
        this.financialInflows = financialInflows;
    }

    public Set<FinancialOutflow> getFinancialOutflows() {
        return financialOutflows;
    }

    public void setFinancialOutflows(Set<FinancialOutflow> financialOutflows) {
        this.financialOutflows = financialOutflows;
    }
}
