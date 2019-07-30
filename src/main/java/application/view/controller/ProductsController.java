package application.view.controller;

import application.controller.ProductField;
import application.controller.object.Product;
import application.model.ProductModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;

public class ProductsController extends Controller {

    @FXML private TextField txtSearch;
    @FXML private TableView<ProductField> tableProducts;
    @FXML private TableColumn<ProductField, Integer> productCode;
    @FXML private TableColumn<ProductField, String> productName;
    @FXML private TableColumn<ProductField, String> productEan;
    @FXML private TableColumn<ProductField, String> productPrice;
    @FXML private TableColumn<ProductField, Integer> productQuantity;
    @FXML private TableColumn<ProductField, HBox> productAction;
    private ObservableList<ProductField> obsProducts = FXCollections.observableArrayList();

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
        setupTableProducts();
    }

    private void setupTxtSearch(){
        Mask.toUpperCase(txtSearch);
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            updateTable();
            tableProducts.refresh();
        }));
    }

    private void setupTableProducts() {
        productCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productEan.setCellValueFactory(new PropertyValueFactory<>("ean"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        productQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        tableProducts.setItems(obsProducts);

        tableProducts.setRowFactory( new Callback<TableView<ProductField>, TableRow<ProductField>>() {
            @Override
            public TableRow<ProductField> call(TableView<ProductField> tableView) {
                final TableRow<ProductField> row = new TableRow<ProductField>() {
                    @Override
                    protected void updateItem(ProductField productField, boolean empty) {
                        super.updateItem(productField, empty);
                        if(!empty) {
                            getStyleClass().clear();
                            if (productField.getProduct().isStatus()) {
                                getStyleClass().add("activated");
                            } else {
                                getStyleClass().add("disabled");
                            }
                        }
                    }
                };
                return row;
            }
        });

        updateTable();
    }

    public void updateTable() {
        obsProducts.clear();
        ArrayList<Product> products = new ArrayList<>();
        if(txtSearch.getText().isEmpty()){
            products.addAll(ProductModel.getAll(""));
        } else {
            products.addAll(ProductModel.getAll("WHERE name LIKE '%" + txtSearch.getText() + "%'"));
        }
        products.forEach( product -> obsProducts.add(new ProductField(product, this)));

    }

    @FXML private void createProduct(){
        Window.changeScene(this.stage, "createProduct", this);
    }

    @FXML private void cancel(){
        this.stage.close();
    }

    @Override
    public void activeWaitScreen(boolean wait) {
        waitScreen.setVisible(wait);

        if(!wait){
            updateTable();
        }
    }
}
