package application.controller;

import application.controller.object.Animal;
import application.model.AnimalModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import application.view.controller.AnimalsController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class AnimalField {
    private Animal animal;

    private String code;
    private String name;
    private String specie;
    private String customer;
    private HBox action = new HBox();

    public AnimalField(Animal animal, Controller controller) {
        this.animal = animal;

        this.code = Formatter.formatStringCode(animal.getId());
        this.name = animal.getName();
        this.specie = animal.getSpecie();
        this.customer = animal.getCustomer().getName();

        if(controller instanceof  AnimalsController) {
            AnimalsController animalsController = (AnimalsController) controller;

            Button status = new Button();
            if (this.animal.isStatus()) {
                Button edit = new Button();
                edit.getStyleClass().add("btnBlueLight");
                edit.setOnAction(e ->
                        Window.changeScene(controller.getStage(), "editAnimal", controller, this.animal));
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
                    this.animal.setStatus(false);
                    if (AnimalModel.update(this.animal)) {
                        animalsController.updateTable();
                    } else {
                        Window.changeScene(controller.getStage(), "error", controller,
                                "Erro ao habilitar animal");
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
                    this.animal.setStatus(true);
                    if (AnimalModel.update(this.animal)) {
                        animalsController.updateTable();
                    } else {
                        Window.changeScene(controller.getStage(), "error", controller,
                                "Erro ao habilitar animal");
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

    public Animal getAnimal() {
        return animal;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSpecie() {
        return specie;
    }

    public String getCustomer() {
        return customer;
    }

    public HBox getAction() {
        return action;
    }
}
