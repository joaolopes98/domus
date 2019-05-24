package application.model;

import application.controller.object.Product;

import java.util.List;

public abstract class ProductModel {
    public static List<Product> getAllSellable(){
        return GenericModel.getAll("FROM Product WHERE quantity > 0 AND status = TRUE");
    }
}
