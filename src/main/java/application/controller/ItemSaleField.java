package application.controller;

import application.controller.object.Animal;
import application.controller.object.Product;
import application.controller.object.Service;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Mask;
import application.view.controller.CreateScheduleController;
import application.view.controller.PDVController;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemSaleField {
    private String code;
    private String name;
    private String price;
    private int quantity;
    private TextField discount = new TextField();
    private String subtotal;

    private Product product;
    private Service service;
    private boolean typeProduct;

    private Animal linkedAnimal;

    private Button btnDelete = new Button();

    public ItemSaleField(int code, ItemSearchField itemSearch, int quantity, Controller controller) {
        if(controller instanceof PDVController) {
            PDVController pdvController = (PDVController) controller;
            this.code = Formatter.formatStringCode(code);

            this.typeProduct = itemSearch.isTypeProduct();
            if (this.typeProduct) {
                this.product = itemSearch.getProduct();
                this.name = product.getName();
                this.price = Formatter.formatMoney(product.getPrice());
            } else {
                this.service = itemSearch.getService();
                this.name = service.getName();
                this.price = Formatter.formatMoney(service.getPrice());
            }

            if (quantity != 0) this.quantity = quantity;
            else this.quantity = 1;

            Mask.money(discount);
            discount.getStyleClass().add("inputDiscount");
            discount.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    pdvController.getTableSale().getSelectionModel().select(this);
                }
            });
            discount.textProperty().addListener(e -> {
                double discount = Formatter.unmaskMoney(this.discount.getText());
                double subtotal;
                if (this.typeProduct) {
                    subtotal = product.getPrice() * this.quantity;
                } else {
                    subtotal = service.getPrice() * this.quantity;
                }
                if (discount > subtotal) {
                    this.discount.setText(Formatter.formatMoney(subtotal));
                }

                subtotal -= discount;
                this.subtotal = Formatter.formatMoney(subtotal);
                pdvController.updateValues();
            });
            if (typeProduct) {
                this.subtotal = Formatter.formatMoney(product.getPrice() * this.quantity);
            } else {
                this.subtotal = Formatter.formatMoney(service.getPrice() * this.quantity);
            }
        } else if(controller instanceof CreateScheduleController){
            CreateScheduleController csController = (CreateScheduleController) controller;

            this.service = itemSearch.getService();
            this.name = service.getName();
            this.price = Formatter.formatMoney(service.getPrice());

            if (quantity != 0) this.quantity = quantity;
            else this.quantity = 1;

            this.subtotal = Formatter.formatMoney(service.getPrice() * this.quantity);

            ImageView imageRemove = new ImageView(new Image("/view/img/trash.png"));
            imageRemove.setFitHeight(25);
            imageRemove.setFitWidth(25);
            this.btnDelete.setGraphic(imageRemove);
            this.btnDelete.getStyleClass().add("btnRed");
            this.btnDelete.setOnAction( e -> csController.getObsSaleFields().remove(this));
            this.btnDelete.setMinSize(35, 35);
            this.btnDelete.setMaxSize(35, 35);
        }
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public boolean isTypeProduct() {
        return typeProduct;
    }

    public Animal getLinkedAnimal() {
        return linkedAnimal;
    }

    public void setLinkedAnimal(Animal linkedAnimal) {
        this.linkedAnimal = linkedAnimal;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }
}
