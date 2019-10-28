package application.controller;

import application.controller.object.CashMovement;
import application.controller.object.Sale;
import application.view.auxiliary.Formatter;

public class CashItem {

    private String cashOpen;
    private String userCash;
    private String initialValue;
    private String finalValue;
    private String calcValue;
    private String profit;
    private int sales;
    private String averageTicket;

    private boolean closed;

    public CashItem(CashMovement cashMovement) {
        this.cashOpen = Formatter.formatDate(cashMovement.getDate());
        this.userCash = cashMovement.getOpened_by().getName();
        this.initialValue = Formatter.formatMoney(cashMovement.getValue());
        if(cashMovement.isClosed()) {
            this.finalValue = Formatter.formatMoney(cashMovement.getValue_closed_input());
            this.calcValue = Formatter.formatMoney(cashMovement.getValue_closed_system());
            double profitDouble = cashMovement.getValue() - cashMovement.getValue_closed_system();
            if (profitDouble >= 0) {
                this.profit = Formatter.formatMoney(profitDouble);
            } else {
                this.profit = " - " + Formatter.formatMoney(profitDouble);
            }
        } else {
            this.finalValue = " - ";
            this.calcValue = " - ";
            this.profit = " - ";
        }

        this.sales = cashMovement.getSales().size();
        double total = 0;
        for (Sale sale : cashMovement.getSales()) {
            total += sale.getValue() - sale.getDiscount();
        }

        this.averageTicket = Formatter.formatMoney(total / this.sales);
        this.closed = cashMovement.isClosed();
    }

    public String getCashOpen() {
        return cashOpen;
    }

    public String getUserCash() {
        return userCash;
    }

    public String getInitialValue() {
        return initialValue;
    }

    public String getFinalValue() {
        return finalValue;
    }

    public String getCalcValue() {
        return calcValue;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getProfit() {
        return profit;
    }

    public int getSales() {
        return sales;
    }

    public String getAverageTicket() {
        return averageTicket;
    }

    @Override
    public String toString() {
        return this.cashOpen + " " +
                this.userCash + " " +
                this.initialValue + " " +
                this.finalValue + " " +
                this.calcValue + " " +
                this.profit + " " +
                this.sales + " " +
                this.averageTicket;
    }
}
