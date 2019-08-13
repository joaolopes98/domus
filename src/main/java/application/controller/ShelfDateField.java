package application.controller;

import application.controller.object.Product;
import application.view.auxiliary.Formatter;

public class ShelfDateField {
    private String name;
    private String shelfDate;

    public ShelfDateField(Product product){
        this.name = product.getName();
        this.shelfDate = Formatter.formatDate(product.getShelf_date());
    }

    public String getName() {
        return name;
    }

    public String getShelfDate() {
        return shelfDate;
    }
}
