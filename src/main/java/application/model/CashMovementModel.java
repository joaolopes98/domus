package application.model;

import application.controller.object.CashMovement;

import java.util.List;

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

    public static List<CashMovement> getAll(String search){
        return GenericModel.getAll("FROM CashMovement " + search);
    }
}
