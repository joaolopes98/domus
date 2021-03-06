package application.controller;

import application.controller.object.Product;
import application.controller.object.Service;
import application.view.auxiliary.Formatter;

public class ItemSearchField {
    private Product product;
    private Service service;

    private String name;
    private String price;
    private int quantity;

    private boolean typeProduct;

    public ItemSearchField(Product product) {
        this.product = product;

        this.name = product.getName();
        this.price = Formatter.formatMoney(product.getPrice());
        this.quantity = product.getQuantity();

        this.typeProduct = true;
    }

    public ItemSearchField(Service service) {
        this.service = service;

        this.name = service.getName();
        this.price = Formatter.formatMoney(service.getPrice());

        this.typeProduct = false;
    }

    public Product getProduct() {
        return product;
    }

    public Service getService() {
        return service;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
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
}
