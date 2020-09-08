package application.controller;

import application.controller.object.CashMovement;
import application.controller.object.Sale;
import application.view.auxiliary.Formatter;

public class CashItem {

    private String cashOpen;
    private String cashClose;
    private String initialValue;
    private String finalValue;
    private String sales;
    private double avgTicket;
    private String averageTicket;
    private String userCash;
    private double total;

    public CashItem(CashMovement cashMovement) {
        this.cashOpen = Formatter.formatDateHour(cashMovement.getDate());
        this.userCash = cashMovement.getOpened_by().getName();
        this.initialValue = Formatter.formatMoney(cashMovement.getValue());
        if(cashMovement.isClosed()) {
            this.finalValue = Formatter.formatMoney(cashMovement.getValue_closed_system());
            this.cashClose = Formatter.formatDate(cashMovement.getClosed_at());
        } else {
            this.finalValue = " - ";
            this.cashClose = " - ";
        }

        this.sales = String.valueOf(cashMovement.getSales().size());
        this.total = 0;
        for (Sale sale : cashMovement.getSales()) {
            this.total += sale.getValue() - sale.getDiscount();
        }

        if(cashMovement.getSales().size() > 0) {
            this.avgTicket = total / cashMovement.getSales().size();
        } else {
            this.avgTicket = 0;
        }
        this.averageTicket = Formatter.formatMoney(this.avgTicket);
    }

    public String getCashOpen() {
        return cashOpen;
    }

    public void setCashOpen(String cashOpen) {
        this.cashOpen = cashOpen;
    }

    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

    public String getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(String finalValue) {
        this.finalValue = finalValue;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getAverageTicket() {
        return averageTicket;
    }

    public void setAverageTicket(String averageTicket) {
        this.averageTicket = averageTicket;
    }

    public String getUserCash() {
        return userCash;
    }

    public void setUserCash(String userCash) {
        this.userCash = userCash;
    }

    public String getCashClose() {
        return cashClose;
    }

    public void setCashClose(String cashClose) {
        this.cashClose = cashClose;
    }

    public double getAvgTicket() {
        return avgTicket;
    }

    public void setAvgTicket(double avgTicket) {
        this.avgTicket = avgTicket;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
