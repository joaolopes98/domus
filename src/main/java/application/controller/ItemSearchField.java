package application.controller;

import application.controller.object.Product;
import application.view.auxiliary.Mask;

import java.text.DecimalFormat;

public class ItemSearchField {
    private Product product;

    private String name;
    private String price;

    public ItemSearchField(Product product) {
        this.product = product;

        this.name = product.getName();
        this.price = Mask.formatDoubleToMoney(product.getPrice());
    }

    public Product getProduct() {
        return product;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
