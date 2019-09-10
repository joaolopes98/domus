package application.model;

import application.controller.object.Product;
import application.view.auxiliary.Formatter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class ProductModel {
    public static List<Product> getAllSellable(){
        return GenericModel.getAll("FROM Product WHERE status = TRUE " +
                "AND shelf_date >= '" + Formatter.resetDate(new Date()).getTime() + "'");
    }

    public static List<Product> getAll(String search) {
        return GenericModel.getAll("FROM Product " + search);
    }

    public static Product get(String search){
        return (Product) GenericModel.get("FROM Product " + search);
    }

    public static boolean update(Product product) {
        return GenericModel.update(product);
    }

    public static boolean create(Product product){
        return GenericModel.create(product);
    }
}
