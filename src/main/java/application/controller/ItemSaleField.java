package application.controller;

import application.controller.object.Product;
import application.view.auxiliary.Mask;
import application.view.controller.PDVController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ItemSaleField {
    private String code;
    private String name;
    private String price;
    private int quantity;
    private TextField discount = new TextField();
    private String subtotal;

    private Product product;

    public ItemSaleField(int code, Product product, int quantity, PDVController pdvController) {
        this.product = product;

        this.code = Mask.formatStringCode(code);
        this.name = product.getName();
        this.price = Mask.formatDoubleToMoney(product.getPrice());
        if(quantity != 0) this.quantity = quantity;
        else this.quantity = 1;
        Mask.money(discount);
        discount.getStyleClass().add("inputDiscount");
        discount.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                pdvController.getTableSale().getSelectionModel().select(this);
            }
        });
        discount.textProperty().addListener( e -> {
            double discount = Mask.unmaskMoney(this.discount.getText());
            double subtotal = product.getPrice() * this.quantity;
            if(discount > subtotal){
                this.discount.setText(Mask.formatDoubleToMoney(subtotal));
            }

            subtotal -= discount;

            this.subtotal = Mask.formatDoubleToMoney(subtotal);

            pdvController.updateValues();
        });
        this.subtotal = Mask.formatDoubleToMoney(product.getPrice() * this.quantity);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public TextField getDiscount() {
        return discount;
    }

    public void setDiscount(TextField discount) {
        this.discount = discount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
}
