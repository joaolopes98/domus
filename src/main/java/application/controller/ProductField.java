package application.controller;

import application.controller.object.Product;
import application.model.ProductModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
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

    public ProductField(Product product, Controller controller) {
        this.product = product;

        this.code = product.getId();
        this.name = product.getName();
        this.ean = product.getEan();
        this.price = Formatter.formatMoney(product.getPrice());
        this.quantity = product.getQuantity();

        ProductsController productsController = (ProductsController) controller;

        Button status = new Button();
        if(product.isStatus()) {

            Button stock = new Button();
            stock.getStyleClass().add("btnYellow");
            stock.setOnAction(e ->
                    Window.changeScene(controller.getStage(), "stockProduct", controller, this.product));
            stock.setMinSize(30, 30);
            stock.setMaxSize(30, 30);
            ImageView imageStock = new ImageView(new Image("/view/img/stock.png"));
            imageStock.setFitHeight(20);
            imageStock.setFitWidth(20);
            stock.setGraphic(imageStock);

            Button edit = new Button();
            edit.getStyleClass().add("btnBlueLight");
            edit.setOnAction(e ->
                    Window.changeScene(controller.getStage(), "editProduct", controller, this.product));
            edit.setMinSize(30, 30);
            edit.setMaxSize(30, 30);
            ImageView imageEdit = new ImageView(new Image("/view/img/edit.png"));
            imageEdit.setFitHeight(20);
            imageEdit.setFitWidth(20);
            edit.setGraphic(imageEdit);

            status.setMinSize(30, 30);
            status.setMaxSize(30, 30);
            status.getStyleClass().add("btnRed");
            status.setOnAction(e -> {
                this.product.setStatus(false);
                if (ProductModel.update(product)) {
                    productsController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller,
                            "Erro ao desabilitar produto");
                }
            });
            ImageView imageRemove = new ImageView(new Image("/view/img/active.png"));
            imageRemove.setFitHeight(20);
            imageRemove.setFitWidth(20);
            status.setGraphic(imageRemove);

            this.action.getChildren().addAll(stock, edit, status);
        } else {
            status.setMinSize(30, 30);
            status.setMaxSize(30, 30);
            status.getStyleClass().add("btnGreen");
            status.setOnAction(e -> {
                this.product.setStatus(true);
                if (ProductModel.update(product)) {
                    productsController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller,
                            "Erro ao habilitar produto");
                }
            });
            ImageView imageRemove = new ImageView(new Image("/view/img/active.png"));
            imageRemove.setFitHeight(20);
            imageRemove.setFitWidth(20);
            status.setGraphic(imageRemove);

            this.action.getChildren().add(status);
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
