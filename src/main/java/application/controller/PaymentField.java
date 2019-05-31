package application.controller;

import application.controller.object.PaymentMethod;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PaymentField {

    private PaymentMethod payment;
    private String name;
    private String value;
    private Button delete = new Button();

    public PaymentField(PaymentMethod payment, String value, ObservableList<PaymentField> obsPayments) {
        this.payment = payment;
        this.name = payment.getName();
        this.value = value;

        this.delete.getStyleClass().add("btnRed");
        this.delete.setOnAction( e -> obsPayments.remove(this));
        this.delete.setMinSize(30, 30);
        this.delete.setMaxSize(30, 30);
        ImageView imageRemove = new ImageView(new Image("/view/img/trash.png"));
        imageRemove.setFitHeight(20);
        imageRemove.setFitWidth(20);
        this.delete.setGraphic(imageRemove);
    }

    public PaymentMethod getPayment() {
        return payment;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Button getDelete() {
        return delete;
    }
}
