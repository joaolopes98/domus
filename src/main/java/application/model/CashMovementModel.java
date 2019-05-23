package application.model;

import application.controller.object.CashMovement;

public abstract class CashMovementModel {
    public static CashMovement getOpened(){
        return (CashMovement) GenericModel.get("FROM CashMovement WHERE closed = FALSE");
    }

    public static boolean create(CashMovement cashMovement){
        return GenericModel.create(cashMovement);
    }

    public static boolean update(CashMovement cashMovement) {
        return GenericModel.update(cashMovement);
    }
}
