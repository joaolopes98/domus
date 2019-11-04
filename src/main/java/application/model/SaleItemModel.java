package application.model;

import application.controller.object.SaleItem;

import java.util.List;

public abstract class SaleItemModel {

    public static List<SaleItem> getAll(String search){
        return GenericModel.getAll("FROM SaleItem " + search);
    }
}
