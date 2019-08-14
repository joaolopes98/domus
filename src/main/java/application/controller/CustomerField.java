package application.controller;

import application.controller.object.Customer;
import application.model.CustomerModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import application.view.controller.CustomersController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CustomerField {
    private Customer customer;

    private String code;
    private String name;
    private String document;
    private String phone;
    private HBox action = new HBox();

    public CustomerField(Customer customer, Controller controller){
        this.customer = customer;

        this.code = Formatter.formatStringCode(customer.getId());
        this.name = customer.getName();
        if(customer.getDocument() != null) this.document = Formatter.formatDocument(customer.getDocument());
        this.phone = Formatter.formatPhone(customer.getPhone());

        CustomersController customersController = (CustomersController) controller;

        Button status = new Button();
        if(customer.isStatus()){
            Button edit = new Button();
            edit.getStyleClass().add("btnBlueLight");
            edit.setOnAction(e ->
                    Window.changeScene(controller.getStage(), "editCustomer", controller, this.customer));
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
                this.customer.setStatus(false);
                if (CustomerModel.update(this.customer)) {
                    customersController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller,
                            "Erro ao habilitar cliente");
                }
            });
            ImageView imageRemove = new ImageView(new Image("/view/img/active.png"));
            imageRemove.setFitHeight(20);
            imageRemove.setFitWidth(20);
            status.setGraphic(imageRemove);

            this.action.getChildren().addAll(edit, status);
        } else {
            status.setMinSize(30, 30);
            status.setMaxSize(30, 30);
            status.getStyleClass().add("btnGreen");
            status.setOnAction(e -> {
                this.customer.setStatus(true);
                if (CustomerModel.update(this.customer)) {
                    customersController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller,
                            "Erro ao habilitar cliente");
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public HBox getAction() {
        return action;
    }

    public void setAction(HBox action) {
        this.action = action;
    }
}
