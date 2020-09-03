package application.controller;

import application.controller.object.Product;
import application.controller.object.Service;
import application.model.ProductModel;
import application.model.ServiceModel;

public class QueryReportItem {

    private Service service;
    private Product product;
    private int quantity;
    private boolean typeProduct;

    public QueryReportItem(Object[] object) {
        typeProduct = (boolean) object[3];
        if (typeProduct) {
            product = ProductModel.get("WHERE id = " + object[1]);
        } else {
            service = ServiceModel.get("WHERE id = " + object[0]);
        }
        quantity = (int) object[2];
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isTypeProduct() {
        return typeProduct;
    }

    public void setTypeProduct(boolean typeProduct) {
        this.typeProduct = typeProduct;
    }
}
