package application.view.controller;

import application.controller.GenerateFunction;
import application.controller.MedicineField;
import application.controller.object.Animal;
import application.controller.MedicineItem;
import application.controller.object.Recipe;
import application.controller.object.RecipeItem;
import application.controller.object.User;
import application.model.RecipeModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Function;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class VeterinaryDashboardController extends Controller {

    @FXML private Button btnLinkAnimal;
    private Animal linkedAnimal;

    @FXML private TableView<MedicineField> tableMedicine;
    @FXML private TableColumn<MedicineField, TextField> medName;
    @FXML private TableColumn<MedicineField, HBox> medQuantity;
    @FXML private TableColumn<MedicineField, HBox> medTime;
    @FXML private TableColumn<MedicineField, Button> medDelete;
    private ObservableList<MedicineField> obsMedicine = FXCollections.observableArrayList();

    @FXML private AnchorPane waitScreen;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.F11){
                stage.setFullScreen(!stage.isFullScreen());
                e.consume();
            }
        });

        this.stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.stage.setTitle("DOMUS PDV");
        this.stage.setMinWidth(1000);
        this.stage.setMinHeight(720);

        super.initialize(oldStage, scene, oldController, objects);

        this.stage.setMaximized(true);
        this.stage.setFullScreen(true);
        this.stage.setOnCloseRequest( e -> logout());

        setupTable();
    }

    private void setupTable() {
        medName.setCellValueFactory(new PropertyValueFactory<>("txtName"));
        medQuantity.setCellValueFactory(new PropertyValueFactory<>("paneQuantity"));
        medTime.setCellValueFactory(new PropertyValueFactory<>("paneTime"));
        medDelete.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));
        tableMedicine.setItems(obsMedicine);
    }

    @FXML private void customer(){
        Window.changeScene(this.stage, "customers", this);
    }

    @FXML private void animal(){
        Window.changeScene(this.stage, "animals", this);
    }

    @FXML private void linkAnimal(){
        if(linkedAnimal == null) {
            Window.changeScene(this.stage, "linkAnimal", this);
        } else {
            setLinkedAnimal(null);
        }
    }

    @FXML private void addMedicine(){
        obsMedicine.add(new MedicineField(obsMedicine));
    }

    @FXML private void print(){
        if(linkedAnimal == null){
            Window.changeScene(this.stage, "error", this,
                    "Vincule um animal a receita");
        } else {
            ArrayList<MedicineItem> medicineItems = new ArrayList<>();
            obsMedicine.forEach( medicineField -> medicineItems.add(new MedicineItem(medicineField)));

            boolean valid = true;
            Recipe recipe = new Recipe();
            recipe.setUser(User.getUser());
            recipe.setAnimal(linkedAnimal);
            recipe.setDate(new Timestamp(new Date().getTime()));
            Set<RecipeItem> recipeItems = new HashSet<>();
            for(MedicineItem item : medicineItems) {
                if (item.getName().isEmpty() ||
                        item.getQuantity() == 0 ||
                        item.getQuantityUnity().isEmpty() ||
                        item.getTime() == 0 ||
                        item.getTimeMetric() == 0 ||
                        item.getTimeUnity().isEmpty()) {
                    valid = false;
                    break;
                } else {
                    RecipeItem recipeItem = new RecipeItem();
                    recipeItem.setName(item.getName());
                    recipeItem.setQuantity(item.getQuantity());
                    recipeItem.setQuantityUnity(item.getQuantityUnity());
                    recipeItem.setTime(item.getTime());
                    recipeItem.setTimeMetric(item.getTimeMetric());
                    recipeItem.setTimeUnity(item.getTimeUnity());
                    recipeItem.setRecipe(recipe);
                    recipeItems.add(recipeItem);
                }
            }

            if(valid){
                recipe.setRecipeItems(recipeItems);

                if(RecipeModel.create(recipe)){
                    Window.changeScene(this.stage, "loading", this,
                            "GERANDO RECEITA",
                            GenerateFunction.veterinary(User.getUser(), linkedAnimal, medicineItems));
                    obsMedicine.clear();
                } else {
                    Window.changeScene(this.stage, "error", this,
                            "Não foi possivel salvar a receita");
                }

            } else {
                Window.changeScene(this.stage, "error", this,
                        "Campo zerado ou vazio dentro da tabela de medicamentos");
            }


        }
    }

    @FXML private void logout(){
        this.stage.close();
        Window.changeScene(this.stage, "initial", null);
    }

    @Override
    public void activeWaitScreen(boolean wait) {
        waitScreen.setVisible(wait);
    }

    public void setLinkedAnimal(Animal linkedAnimal) {
        if(linkedAnimal != null) {
            this.linkedAnimal = linkedAnimal;
            btnLinkAnimal.setText(linkedAnimal.getName());
            if(btnLinkAnimal.getStyleClass().contains("btnBlue")) {
                btnLinkAnimal.getStyleClass().remove("btnBlue");
                btnLinkAnimal.getStyleClass().add("btnYellow");
            }
        } else {
            btnLinkAnimal.setText("VINCULAR ANIMAL");
            this.linkedAnimal = null;
            if(btnLinkAnimal.getStyleClass().contains("btnYellow")) {
                btnLinkAnimal.getStyleClass().remove("btnYellow");
                btnLinkAnimal.getStyleClass().add("btnBlue");
            }
        }
    }

    @FXML private void changePDV() {
        this.stage.close();
        Window.changeScene(new Stage(), "pdv", oldController);
    }

    @FXML private void openModalVeterinaryHistoric() {
        Window.changeScene(this.stage, "veterinaryHistory", this);
    }
}
