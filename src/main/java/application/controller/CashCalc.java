package application.controller;

import application.controller.object.CashMovement;
import application.controller.object.FinancialInflow;
import application.controller.object.FinancialOutflow;

public class CashCalc {
    public static double money(CashMovement cashMovement){
        double value = cashMovement.getValue();

        for(FinancialInflow fi : cashMovement.getFinancialInflows()){
            if(fi.getPaymentMethod() == null || fi.getPaymentMethod().getName().endsWith("DINHEIRO")) {
                value += fi.getValue();
            }
        }

        for(FinancialOutflow fo : cashMovement.getFinancialOutflows()){
            value -= fo.getValue();
        }

        return value;
    }
}
