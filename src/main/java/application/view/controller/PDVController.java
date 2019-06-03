package application.view.controller;

import application.controller.ItemSaleField;
import application.controller.ItemSearchField;
import application.controller.object.Product;
import application.controller.object.User;
import application.model.CashMovementModel;
import application.model.ProductModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PDVController extends Controller {


    @FXML private TextField txtSearchItem;
    @FXML private TextField txtQuantityItem;

    private ItemSearchField selectedSearch;
    @FXML private VBox paneSearchMenu;
    @FXML private TableView<ItemSearchField> tableSearch;
    @FXML private TableColumn<ItemSearchField, String> nameSearch;
    @FXML private TableColumn<ItemSearchField, String> priceSearch;
    private ObservableList<ItemSearchField> obsSearchProduct = FXCollections.observableArrayList();
    private ObservableList<ItemSearchField> obsSearchFields = FXCollections.observableArrayList();
    @FXML private Label lblQuantitySearch;


    @FXML private TableView<ItemSaleField> tableSale;
    @FXML private TableColumn<ItemSaleField, String> codeSale;
    @FXML private TableColumn<ItemSaleField, String> nameSale;
    @FXML private TableColumn<ItemSaleField, String> priceSale;
    @FXML private TableColumn<ItemSaleField, String> quantitySale;
    @FXML private TableColumn<ItemSaleField, String> discountSale;
    @FXML private TableColumn<ItemSaleField, String> subtotalSale;
    private ObservableList<ItemSaleField> obsSaleFields = FXCollections.observableArrayList();

    @FXML private Label lblTotal;
    @FXML private Label lblDiscount;
    private double total= 0;
    private double discount = 0;
    private int code = 1;

    @FXML private Button btnFinalizeSale;
    @FXML private Button btnCancelItem;

    @FXML private Label lblUser;
    @FXML private Label lblDate;

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
//        this.stage.setFullScreenExitHint("Pressione F11 para sair do modo FullScreen");
        this.stage.setTitle("DOMUS PDV");
        this.stage.setMinWidth(1000);
        this.stage.setMinHeight(720);
        super.initialize(oldStage, scene, oldController, objects);
//        this.stage.setMaximized(true);
//        Window.setFullScreen(this.stage);
        this.stage.setFullScreen(true);
        this.stage.setOnCloseRequest( e -> logout());

        setupSearch();
        setupSale();
        setupInfo();

        if(CashMovementModel.getOpened() == null) {
            Window.changeScene(this.stage, "openCash", this);
        }
    }

    private void setupSearch() {
        paneSearchMenu.visibleProperty().addListener((observable, oldValue, newValue) -> {
            obsSearchFields.clear();
            if(newValue){
                String search = txtSearchItem.getText();
                obsSearchProduct.forEach( product -> {
                    if (product.getName().contains(search)){
                        obsSearchFields.add(product);
                    }
                });
            }
            tableSearch.getSelectionModel().select(0);
        });

        //Configurações dos Inputs
        txtSearchItem.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB){
                e.consume();
                txtQuantityItem.requestFocus();
            } else if (e.getCode() == KeyCode.UP){
                e.consume();
                if(paneSearchMenu.isVisible()){
                    int posSelected = tableSearch.getSelectionModel().getSelectedIndex();
                    posSelected --;
                    System.out.println(posSelected);
                    tableSearch.getSelectionModel().select(posSelected);
                }
            } else if (e.getCode() == KeyCode.DOWN){
                e.consume();
                if(paneSearchMenu.isVisible()){
                    int posSelected = tableSearch.getSelectionModel().getSelectedIndex();
                    posSelected++;
                    System.out.println(posSelected);
                    tableSearch.getSelectionModel().select(posSelected);
                }
            } else if (e.getCode() == KeyCode.ENTER){
                e.consume();

                if(selectedSearch == null) {
                    if (paneSearchMenu.isVisible()) {
                        selectedSearch = tableSearch.getSelectionModel().getSelectedItem();
                        txtSearchItem.setText(selectedSearch.getName());
                        Mask.toLastPosition(txtSearchItem);
                        txtQuantityItem.requestFocus();
                        paneSearchMenu.setVisible(false);
                    } else {
                        openMenuSearch();
                    }
                } else {
                    if(txtSearchItem.getText().equals(selectedSearch.getName())) {
                        addItemToTableSale();
                    } else {
                        selectedSearch = null;
                        paneSearchMenu.setVisible(true);
                        tableSearch.getSelectionModel().select(0);
                    }
                }
            }
        });
        txtSearchItem.textProperty().addListener( e -> {
            if(txtSearchItem.getText().isEmpty()){
                paneSearchMenu.setVisible(false);
            } else {
                obsSearchFields.clear();
                String search = txtSearchItem.getText();
                obsSearchProduct.forEach( product -> {
                    if (product.getName().contains(search)){
                        obsSearchFields.add(product);
                    }
                });
                tableSearch.getSelectionModel().select(0);
            }
        });
        txtQuantityItem.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.TAB){
                txtSearchItem.requestFocus();
                e.consume();
            } else if(e.getCode() == KeyCode.ENTER){
                if(selectedSearch == null){
                    Mask.toLastPosition(txtSearchItem);
                    openMenuSearch();
                } else {
                    if(txtSearchItem.getText().equals(selectedSearch.getName())) {
                        addItemToTableSale();
                    } else {
                        openMenuSearch();
                    }
                }
            }
        });

        Mask.toUpperCase(txtSearchItem);
        Mask.zeroTo(txtQuantityItem, 999);

        //Configurações da Tabela de Busca
        nameSearch.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceSearch.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableSearch.setItems(obsSearchFields);

        tableSearch.setRowFactory( e -> {
            TableRow<ItemSearchField> row = new TableRow<>();
            row.setOnMouseClicked( f -> {
                if(f.getClickCount() == 1 && !row.isEmpty()){
                    Mask.toLastPosition(txtSearchItem);
                    f.consume();
                } else if(f.getClickCount() == 2 && !row.isEmpty()){
                    selectedSearch = row.getItem();
                    addItemToTableSale();
                    f.consume();
                }
            });
            return row;
        });

        //Configurando Items de Busca
        obsSearchFields.addListener((ListChangeListener<ItemSearchField>) c -> {
            lblQuantitySearch.setText(String.valueOf(obsSearchFields.size()));
        });
        updateProducts();
    }

    private void updateProducts() {
        obsSearchProduct.clear();
        ArrayList<Product> obsProducts = new ArrayList<>(ProductModel.getAllSellable());
        obsProducts.forEach( product -> obsSearchProduct.add(new ItemSearchField(product)));
    }

    private void setupSale(){
        codeSale.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameSale.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceSale.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceSale.setStyle("-fx-alignment: center-right");
        quantitySale.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantitySale.setStyle("-fx-alignment: center-right");
        discountSale.setCellValueFactory(new PropertyValueFactory<>("discount"));
        subtotalSale.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        subtotalSale.setStyle("-fx-alignment: center-right");
        tableSale.setItems(obsSaleFields);
        tableSale.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnCancelItem.setDisable(newValue == null);
        });

        obsSaleFields.addListener((ListChangeListener<ItemSaleField>) e -> {
            updateValues();
            btnFinalizeSale.setDisable(obsSaleFields.size() == 0);

            DecimalFormat codeFormat = new DecimalFormat("000");
            this.code = 1;
            obsSaleFields.forEach( item -> {
                item.setCode(codeFormat.format(this.code));
                this.code++;
            });
        });
    }

    private void setupInfo() {
        lblUser.setText(User.getUser().getName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        lblDate.setText(sdf.format(new Date()));
    }

    private void addItemToTableSale() {
        int code = obsSaleFields.size() + 1;
        Product product = selectedSearch.getProduct();
        int quantity = Mask.unmaskInteger(txtQuantityItem.getText());
        obsSaleFields.add(new ItemSaleField(code, product, quantity, this));

        txtSearchItem.setText("");
        txtQuantityItem.setText("1");
        selectedSearch = null;
        Mask.toLastPosition(txtSearchItem);
        tableSale.getSelectionModel().clearSelection();
    }

    private void openMenuSearch(){
        selectedSearch = null;
        if(!txtSearchItem.getText().isEmpty()) {
            paneSearchMenu.setVisible(true);
            tableSearch.getSelectionModel().select(0);
        }
    }

    public void updateValues(){
        this.total = 0;
        this.discount = 0;
        for(ItemSaleField itemSaleField : obsSaleFields){
            this.total += Mask.unmaskMoney(itemSaleField.getSubtotal());
            this.discount += Mask.unmaskMoney(itemSaleField.getDiscount().getText());
        }

        lblTotal.setText(Mask.formatDoubleToMoney(this.total));
        lblDiscount.setText(Mask.formatDoubleToMoney(this.discount));
    }

    @FXML private void insertInflows(){
        Window.changeScene(this.stage, "financialInflows", this);
    }

    @FXML private void insertOutflows(){
        Window.changeScene(this.stage, "financialOutflows", this);
    }

    @FXML private void linkCustomer(){
        Window.changeScene(this.stage, "error", this, "Funcionalidade não disponivel no momento");
    }

    @FXML private void linkAnimal(){
        Window.changeScene(this.stage, "error", this, "Funcionalidade não disponivel no momento");
    }

    @FXML private void openFinalizeSale(){
        Window.changeScene(this.stage, "finalizeSale", this);
    }

    @FXML private void cancelItem(){
        obsSaleFields.remove(tableSale.getSelectionModel().getSelectedItem());
        tableSale.getSelectionModel().clearSelection();
    }

    @FXML private void manageOptions(){
        Window.changeScene(this.stage, "manageOptions", this);
    }

    @FXML public void closeCash(){
        Window.changeScene(this.stage, "closeCash", this);
    }

    @FXML public void logout(){
        this.stage.close();
        Window.changeScene(this.stage, "initial", null);
    }

    @Override
    public void activeWaitScreen(boolean wait){
        waitScreen.setVisible(wait);

        if(!wait){
            updateProducts();
            Mask.toLastPosition(txtSearchItem);
        }
    }

    public TableView<ItemSaleField> getTableSale() {
        return tableSale;
    }

    public ObservableList<ItemSaleField> getObsSaleFields() {
        return obsSaleFields;
    }

    public double getTotal() {
        return total;
    }

    public double getDiscount() {
        return discount;
    }
}
