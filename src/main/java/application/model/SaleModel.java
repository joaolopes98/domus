package application.model;

import application.controller.object.Sale;

import java.util.Collection;
import java.util.List;

public abstract class SaleModel {
    public static boolean create (Sale sale){
        return GenericModel.create(sale);
    }

    public static boolean update(Sale sale) {
        return GenericModel.update(sale);
    }

    public static List<Sale> getAll(String search) {
        return (List<Sale>) GenericModel.getAll("FROM Sale " + search);
    }
}
