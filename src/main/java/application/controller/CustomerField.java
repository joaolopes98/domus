package application.controller;

import application.controller.object.Customer;
import application.model.CustomerModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import application.view.controller.CustomersController;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomerField {
    private Customer customer;

    private String code;
    private String name;
    private String document;
    private String phone;
    private Button action = new Button();

    public CustomerField(Customer customer, Controller controller){
        this.customer = customer;

        this.code = Formatter.formatStringCode(customer.getId());
        this.name = customer.getName();
        if(customer.getDocument() != null) this.document = Formatter.formatDocument(customer.getDocument());
        this.phone = Formatter.formatPhone(customer.getPhone());

        CustomersController customersController = (CustomersController) controller;

        if(customer.isStatus()){
            this.action.setMinSize(30, 30);
            this.action.setMaxSize(30, 30);
            this.action.getStyleClass().add("btnRed");
            this.action.setOnAction(e -> {
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
            this.action.setGraphic(imageRemove);
        } else {
            this.action.setMinSize(30, 30);
            this.action.setMaxSize(30, 30);
            this.action.getStyleClass().add("btnGreen");
            this.action.setOnAction(e -> {
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
            this.action.setGraphic(imageRemove);
        }

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

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }
}
