package application.controller;

import application.controller.object.Service;
import application.model.ServiceModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import application.view.controller.ServicesController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ServiceField {
    private Service service;

    private String code;
    private String name;
    private String description;
    private String price;
    private HBox action = new HBox();

    public ServiceField(Service service, Controller controller){
        this.service = service;

        this.code = Formatter.formatStringCode(service.getId());
        this.name = service.getName();
        this.description = service.getDescription();
        this.price = Formatter.formatMoney(service.getPrice());

        ServicesController servicesController = (ServicesController) controller;

        Button status = new Button();
        if(service.isStatus()){

            Button edit = new Button();
            edit.getStyleClass().add("btnBlueLight");
            edit.setOnAction(e ->
                    Window.changeScene(controller.getStage(), "editService", controller, this.service));
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
                this.service.setStatus(false);
                if (ServiceModel.update(this.service)) {
                    servicesController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller,
                            "Erro ao desabilitar serviço");
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
                this.service.setStatus(true);
                if (ServiceModel.update(this.service)) {
                    servicesController.updateTable();
                } else {
                    Window.changeScene(controller.getStage(), "error", controller,
                            "Erro ao habilitar servico");
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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public HBox getAction() {
        return action;
    }

    public void setAction(HBox action) {
        this.action = action;
    }
}
