package application.controller;

import application.controller.object.Product;
import application.model.ProductModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import application.view.controller.ProductsController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ProductField {
    private Product product;

    private int code;
    private String name;
    private String ean;
    private String price;
    private int quantity;

    private HBox action = new HBox();
    private Button stock = new Button();
    private Button edit = new Button();
    private Button status = new Button();

    public ProductField(Product product, Controller controller) {
        this.product = product;

        this.code = product.getId();
        this.name = product.getName();
        this.ean = product.getEan();
        this.price = Mask.formatDoubleToMoney(product.getPrice());
        this.quantity = product.getQuantity();

        ProductsController productsController = (ProductsController) controller;

        if(product.isStatus()) {

            this.stock.getStyleClass().add("btnYellow");
            this.stock.setOnAction( e -> Window.changeScene(controller.getStage(), "stockProduct", controller, this.product));
            this.stock.setMinSize(30, 30);
            this.stock.setMaxSize(30, 30);
            ImageView imageStock = new ImageView(new Image("/view/img/stock.png"));
            imageStock.setFitHeight(20);
            imageStock.setFitWidth(20);
            this.stock.setGraphic(imageStock);

            this.edit.getStyleClass().add("btnBlueLight");
            this.edit.setOnAction( e -> Window.changeScene(controller.getStage(), "editProduct", controller, this.product));
            this.edit.setMinSize(30, 30);
            this.edit.setMaxSize(30, 30);
            ImageView imageEdit = new ImageView(new Image("/view/img/edit.png"));
            imageEdit.setFitHeight(20);
            imageEdit.setFitWidth(20);
            this.edit.setGraphic(imageEdit);

            this.status.setMinSize(30, 30);
            this.status.setMaxSize(30, 30);
            this.status.getStyleClass().add("btnRed");
            this.status.setOnAction(e -> {
                this.product.setStatus(false);
                if (ProductModel.update(product)) {
                    productsController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller, "Erro ao desabilitar produto");
                }
            });
            ImageView imageRemove = new ImageView(new Image("/view/img/active.png"));
            imageRemove.setFitHeight(20);
            imageRemove.setFitWidth(20);
            this.status.setGraphic(imageRemove);

            this.action.getChildren().addAll(this.stock, this.edit, this.status);
        } else {
            this.status.setMinSize(30, 30);
            this.status.setMaxSize(30, 30);
            this.status.getStyleClass().add("btnGreen");
            this.status.setOnAction(e -> {
                this.product.setStatus(true);
                if (ProductModel.update(product)) {
                    productsController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller, "Erro ao habilitar produto");
                }
            });
            ImageView imageRemove = new ImageView(new Image("/view/img/active.png"));
            imageRemove.setFitHeight(20);
            imageRemove.setFitWidth(20);
            this.status.setGraphic(imageRemove);

            this.action.getChildren().add(this.status);
        }

        this.action.setAlignment(Pos.CENTER_LEFT);
        this.action.setSpacing(10);

    }

    public Product getProduct() {
        return product;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEan() {
        return ean;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public HBox getAction() {
        return action;
    }
}
