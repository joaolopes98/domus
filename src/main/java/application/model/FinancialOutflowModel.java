package application.model;

import application.controller.object.FinancialOutflow;

public abstract class FinancialOutflowModel {
    public static boolean create (FinancialOutflow fo){
        return GenericModel.create(fo);
    }
}
