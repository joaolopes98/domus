package application.controller;

import application.controller.object.Sale;
import application.model.AnimalModel;
import application.model.SaleModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import application.view.controller.SalesController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SaleField {
    private Sale sale;

    private String code;
    private String user;
    private String customer;
    private String value;
    private HBox action = new HBox();

    public SaleField(Sale sale, Controller controller){
        this.sale = sale;

        this.code = Formatter.formatStringCode(sale.getId());
        this.user = sale.getAccess().getName();
        this.customer = sale.getCustomer().getName();
        this.value = Formatter.formatMoney(sale.getValue());

        SalesController salesController = (SalesController) controller;
        Button status = new Button();
        if(sale.isActive()) {
            Button history = new Button();
            history.getStyleClass().add("btnYellow");
//        history.setOnAction(e ->
//                Window.changeScene(controller.getStage(), "animalHistory", controller, this.animal));
            history.setMinSize(30, 30);
            history.setMaxSize(30, 30);
            ImageView imageHistory = new ImageView(new Image("/view/img/history.png"));
            imageHistory.setFitHeight(20);
            imageHistory.setFitWidth(20);
            history.setGraphic(imageHistory);


            status.setMinSize(30, 30);
            status.setMaxSize(30, 30);
            status.getStyleClass().add("btnRed");
            status.setOnAction(e -> {
                this.sale.setActive(false);
                if (SaleModel.update(this.sale)) {
                    salesController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller,
                            "Erro ao desabilitar venda");
                }
            });
            ImageView imageRemove = new ImageView(new Image("/view/img/active.png"));
            imageRemove.setFitHeight(20);
            imageRemove.setFitWidth(20);
            status.setGraphic(imageRemove);

            this.action.getChildren().addAll(history, status);
        } else {
            status.setMinSize(30, 30);
            status.setMaxSize(30, 30);
            status.getStyleClass().add("btnGreen");
            status.setOnAction(e -> {
                this.sale.setActive(true);
                if (SaleModel.update(this.sale)) {
                    salesController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller,
                            "Erro ao habilitar venda");
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

    public Sale getSale() {
        return sale;
    }

    public String getCode() {
        return code;
    }

    public String getUser() {
        return user;
    }

    public String getCustomer() {
        return customer;
    }

    public String getValue() {
        return value;
    }

    public HBox getAction() {
        return action;
    }
}
