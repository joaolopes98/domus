package application.view.controller;

import application.controller.RecipeField;
import application.controller.object.Recipe;
import application.model.AccessModel;
import application.model.AnimalModel;
import application.model.RecipeModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class VeterinaryHistoryController extends Controller {

    @FXML private TextField txtSearch;
    @FXML private TableView<RecipeField> tableRecipe;
    @FXML private TableColumn<RecipeField, String> recipeUser;
    @FXML private TableColumn<RecipeField, String> recipeAnimal;
    @FXML private TableColumn<RecipeField, String> recipeDate;
    @FXML private TableColumn<RecipeField, Button> recipeAction;
    private ObservableList<RecipeField> obsRecipe = FXCollections.observableArrayList();

    @FXML private AnchorPane waitScreen;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        setupTxtSearch();
        setupTableRecipe();
    }

    private void setupTxtSearch(){
        Mask.upperCase(txtSearch);
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> updateTable()));
    }

    private void updateTable() {
        obsRecipe.clear();
        ArrayList<Recipe> recipes = new ArrayList<>();
        String search = txtSearch.getText();
        if(txtSearch.getText().isEmpty()){
            recipes.addAll(RecipeModel.getAll(""));
        } else {
            AnimalModel.getAll("WHERE name LIKE '%" + search + "%'" ).forEach( animal -> {
                recipes.addAll(animal.getRecipes());
            });
            AccessModel.getAll("WHERE name LIKE '%" + search + "%'").forEach( access -> {
                recipes.addAll(access.getRecipes());
            });
        }
        recipes.forEach(recipe -> obsRecipe.add(new RecipeField(recipe, this.stage, this)));
        tableRecipe.refresh();
    }

    private void setupTableRecipe(){
        recipeUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        recipeAnimal.setCellValueFactory(new PropertyValueFactory<>("animal"));
        recipeDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        recipeAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        tableRecipe.setItems(obsRecipe);
        updateTable();
    }

    @FXML private void cancel(){
        this.stage.close();
    }

    @Override
    public void activeWaitScreen(boolean wait) {
        waitScreen.setVisible(wait);
    }
}
