package application.controller;

import application.controller.object.Access;
import application.model.AccessModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import application.view.controller.UsersController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class UserField {
    private Access access;

    private String code;
    private String name;
    private String document;
    private String phone;
    private HBox action = new HBox();

    public UserField(Access access, Controller controller){
        this.access = access;

        this.code = Formatter.formatStringCode(access.getId());
        this.name = access.getName();
        this.document = Formatter.formatDocument(access.getDocument());
        this.phone = Formatter.formatPhone(access.getPhone());

        if(controller instanceof UsersController){
            UsersController usersController = (UsersController) controller;
            Button status = new Button();
            if (this.access.isStatus()) {
                Button edit = new Button();
                edit.getStyleClass().add("btnBlueLight");
                edit.setOnAction(e ->
                        Window.changeScene(controller.getStage(), "editUser", controller, this.access));
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
                    this.access.setStatus(false);
                    if (AccessModel.update(this.access)) {
                        usersController.updateTable();
                    } else {
                        Window.changeScene(controller.getStage(), "error", controller,
                                "Erro ao desabilitar usuario");
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
                    this.access.setStatus(true);
                    if (AccessModel.update(this.access)) {
                        usersController.updateTable();
                    } else {
                        Window.changeScene(controller.getStage(), "error", controller,
                                "Erro ao habilitar usuario");
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
    }

    public Access getAccess() {
        return access;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDocument() {
        return document;
    }

    public String getPhone() {
        return phone;
    }

    public HBox getAction() {
        return action;
    }
}
