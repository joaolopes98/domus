package application.view.controller;

import application.controller.ShelfDateField;
import application.controller.object.Product;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ShelfDateController extends Controller {

    @FXML private TableView<ShelfDateField> tableProducts;
    @FXML private TableColumn<ShelfDateField, String> productName;
    @FXML private TableColumn<ShelfDateField, String> productDate;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE  ||e.getCode() == KeyCode.ENTER){
                close();
                e.consume();
            }
        });

        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        setupTable((ArrayList<Product>)objects[0]);
    }

    private void setupTable(ArrayList<Product> products) {
        ObservableList<ShelfDateField> obsProducts = FXCollections.observableArrayList();
        products.forEach( product -> obsProducts.add(new ShelfDateField(product)));

        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productDate.setCellValueFactory(new PropertyValueFactory<>("shelfDate"));
        tableProducts.setItems(obsProducts);
    }

    @FXML private void close(){
        this.stage.close();
    }
}
