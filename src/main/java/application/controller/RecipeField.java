package application.controller;

import application.controller.object.Recipe;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Window;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RecipeField {

    private Recipe recipe;

    private String user;
    private String animal;
    private String date;
    private Button action = new Button();

    public RecipeField(Recipe recipe, Stage stage, Controller controller) {
        this.recipe = recipe;

        this.user = recipe.getUser().getName();
        this.animal = recipe.getAnimal().getName();
        this.date = Formatter.formatDateHour(recipe.getDate());

        ArrayList<MedicineItem> medicineItems = new ArrayList<>();
        recipe.getRecipeItems().forEach( item -> medicineItems.add(new MedicineItem(item)));

        this.action.getStyleClass().add("btnBlueLight");
        this.action.setOnAction(e -> {
            Window.changeScene(stage, "loading", controller,
                    "GERANDO RECEITA", GenerateFunction.veterinary(recipe.getUser(),
                            recipe.getAnimal(), medicineItems));
        });
        this.action.setMinSize(30, 30);
        this.action.setMaxSize(30, 30);
        ImageView imageEdit = new ImageView(new Image("/view/img/edit.png"));
        imageEdit.setFitHeight(20);
        imageEdit.setFitWidth(20);
        this.action.setGraphic(imageEdit);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }
}
