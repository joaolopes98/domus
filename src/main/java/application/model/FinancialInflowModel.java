package application.model;

import application.controller.object.FinancialInflow;

public abstract class FinancialInflowModel {
    public static boolean create(FinancialInflow financialInflow){
        return GenericModel.create(financialInflow);
    }
}
