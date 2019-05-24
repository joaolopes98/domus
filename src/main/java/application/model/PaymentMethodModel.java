package application.model;

import application.controller.object.PaymentMethod;

import java.util.List;

public abstract class PaymentMethodModel {
    public static List<PaymentMethod> getAll(){
        return GenericModel.getAll("FROM PaymentMethod WHERE status = TRUE");
    }
}
