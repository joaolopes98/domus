package application.model;

import application.controller.object.Sale;

public abstract class SaleModel {
    public static boolean create (Sale sale){
        return GenericModel.create(sale);
    }
}
