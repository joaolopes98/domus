package application.view.controller;

import application.controller.ItemSaleField;
import application.controller.ItemSearchField;
import application.controller.object.*;
import application.model.CashMovementModel;
import application.model.ProductModel;
import application.model.ServiceModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PDVController extends Controller {


    @FXML private TextField txtSearchItem;
    @FXML private TextField txtQuantityItem;

    private ItemSearchField selectedSearch;
    @FXML private VBox paneSearchMenu;
    @FXML private TableView<ItemSearchField> tableSearch;
    @FXML private TableColumn<ItemSearchField, String> nameSearch;
    @FXML private TableColumn<ItemSearchField, String> priceSearch;
    private ObservableList<ItemSearchField> obsSearchItems = FXCollections.observableArrayList();
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

    @FXML private Button btnSchedule;
    private Schedule linkedSchedule;
    @FXML private Button btnLinkCustomer;
    private Customer linkedCustomer;
    @FXML private Button btnLinkAnimal;
    private Animal linkedAnimal;
    @FXML private Button btnFinalizeSale;
    @FXML private Button btnCancelItem;

    @FXML private AnchorPane btnChangeVeterinary;
    @FXML private AnchorPane btnManageOptions;

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

        Access user = User.getUser();
        if(user.getRole() != 3){
            btnManageOptions.setManaged(false);
            btnManageOptions.setVisible(false);
        }

        if(user.getRole() != 2){
            btnChangeVeterinary.setManaged(false);
            btnChangeVeterinary.setVisible(false);
        }

        super.initialize(oldStage, scene, oldController, objects);
        this.stage.setMaximized(true);
//        Window.setFullScreen(this.stage);
        this.stage.setFullScreen(true);
        this.stage.setOnCloseRequest( e -> logout());

        setupSearch();
        setupSale();
        setupInfo();

        if(CashMovementModel.getOpened() == null) {
            Window.changeScene(this.stage, "openCash", this);
        } else {
            verifyShelfDate();
        }
    }

    private void setupSearch() {
        paneSearchMenu.visibleProperty().addListener((observable, oldValue, newValue) -> {
            obsSearchFields.clear();
            if(newValue){
                String search = txtSearchItem.getText();
                obsSearchItems.forEach(product -> {
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
                    tableSearch.getSelectionModel().select(posSelected);
                }
            } else if (e.getCode() == KeyCode.DOWN){
                e.consume();
                if(paneSearchMenu.isVisible()){
                    int posSelected = tableSearch.getSelectionModel().getSelectedIndex();
                    posSelected++;
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
                obsSearchItems.forEach(product -> {
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

        Mask.upperCase(txtSearchItem);
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

    void updateProducts() {
        obsSearchItems.clear();
        ArrayList<Product> obsProducts = new ArrayList<>(ProductModel.getAllSellable());
        obsProducts.forEach( product -> obsSearchItems.add(new ItemSearchField(product)));

        ArrayList<Service> obsService = new ArrayList<>(ServiceModel.getAllSellable());
        obsService.forEach( service -> obsSearchItems.add(new ItemSearchField(service)));
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
            btnLinkAnimal.setDisable(!(newValue != null && !newValue.isTypeProduct()));
            if(!btnLinkAnimal.isDisable() && newValue != null && newValue.getLinkedAnimal() != null) {
                setLinkedAnimal(newValue.getLinkedAnimal());
            } else {
                setLinkedAnimal(null);
            }
        });

        obsSaleFields.addListener((ListChangeListener<ItemSaleField>) e -> {
            updateValues();
            btnFinalizeSale.setDisable(obsSaleFields.size() == 0);
            btnLinkCustomer.setDisable(obsSaleFields.size() == 0);
            if(obsSaleFields.size() == 0) {
                setLinkedCustomer(null);

                if(linkedSchedule != null) {
                    setLinkedSchedule(null);
                }
            }
            btnManageOptions.setDisable(obsSaleFields.size() != 0);

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
        lblDate.setText(Formatter.formatDateHour(new Date()));
    }

    private void addItemToTableSale() {
        int code = obsSaleFields.size() + 1;
        int quantity = Formatter.unmaskInteger(txtQuantityItem.getText());
        if(selectedSearch.isTypeProduct()) {
            if(selectedSearch.getQuantity() >= quantity) {
                selectedSearch.setQuantity(selectedSearch.getQuantity() - quantity);
                obsSaleFields.add(new ItemSaleField(code, selectedSearch, quantity, this));
                resetSearch();
            } else {
                Window.changeScene(this.stage, "error", this,
                        "O produto ( " + selectedSearch.getName() + " ) não possui estoque");
            }
        } else {
            obsSaleFields.add(new ItemSaleField(code, selectedSearch, quantity, this));
            resetSearch();
        }
    }

    private void resetSearch(){
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
            this.total += Formatter.unmaskMoney(itemSaleField.getSubtotal());
            this.discount += Formatter.unmaskMoney(itemSaleField.getDiscount().getText());
        }

        lblTotal.setText(Formatter.formatMoney(this.total));
        lblDiscount.setText(Formatter.formatMoney(this.discount));
    }

    @FXML private void schedule(){
        if(linkedSchedule == null) {
            Window.changeScene(this.stage, "schedule", this);
        } else {
            setLinkedSchedule(null);
        }
    }

    @FXML private void customer(){
        Window.changeScene(this.stage, "customers", this);
    }

    @FXML private void animal(){
        Window.changeScene(this.stage, "animals", this);
    }

    @FXML private void linkCustomer(){
        if(linkedCustomer == null) {
            Window.changeScene(this.stage, "linkCustomer", this);
        } else {
            setLinkedCustomer(null);
        }
    }

    @FXML private void linkAnimal(){
        if(linkedAnimal == null) {
            Window.changeScene(this.stage, "linkAnimal", this);
        } else {
            setLinkedAnimal(null);
        }
    }

    @FXML private void openFinalizeSale(){
        Window.changeScene(this.stage, "finalizeSale", this);
    }

    @FXML private void cancelItem(){
        ItemSaleField itemSaleField = tableSale.getSelectionModel().getSelectedItem();
        obsSaleFields.remove(itemSaleField);
        tableSale.getSelectionModel().clearSelection();

        if(itemSaleField.isTypeProduct()) {
            obsSearchItems.forEach(item -> {
                if (item.isTypeProduct()) {
                    if (itemSaleField.getProduct().getId() == item.getProduct().getId()) {
                        item.setQuantity(item.getQuantity() + itemSaleField.getQuantity());
                    }
                }
            });
        }

    }

    @FXML private void changeVeterinary(){
        this.stage.close();
        Window.changeScene(new Stage(), "veterinaryDashboard", oldController);
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

    public void verifyShelfDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        ArrayList<Product> shelfDateProducts = new ArrayList<>(
                ProductModel.getAll("WHERE status = TRUE AND shelf_date <= '"
                        + Formatter.resetDate(calendar.getTime()).getTime() + "' ORDER BY shelf_date"));

        if(!shelfDateProducts.isEmpty()) {
            Window.changeScene(this.stage, "shelfDate", this, shelfDateProducts);
        }
    }

    public void setLinkedSchedule(Schedule linkedSchedule){
        if(linkedSchedule != null) {
            this.linkedSchedule = linkedSchedule;
            String message = "Atendente : " + linkedSchedule.getAccess().getName() + " - " +
                    Formatter.formatDateHour(linkedSchedule.getFrom_date()) +  " - Cliente : "
                    + linkedSchedule.getCustomer().getName();
            btnSchedule.setText(message);
            btnSchedule.getStyleClass().remove("btnBlue");
            btnSchedule.getStyleClass().add("btnYellow");

            setLinkedCustomer(linkedSchedule.getCustomer());

            obsSaleFields.clear();
            linkedSchedule.getScheduleItems().forEach( item -> {
                int code = obsSaleFields.size() + 1;
                int quantity = item.getQuantity();
                obsSaleFields.add(
                        new ItemSaleField(code, new ItemSearchField(item.getService()), quantity, this));
            });
            resetSearch();

        } else {
            btnSchedule.setText("AGENDA");
            this.linkedSchedule = null;
            btnSchedule.getStyleClass().remove("btnYellow");
            btnSchedule.getStyleClass().add("btnBlue");

            setLinkedCustomer(null);
            obsSaleFields.clear();
        }
    }

    public Schedule getLinkedSchedule() {
        return linkedSchedule;
    }

    public void setLinkedCustomer(Customer linkedCustomer) {
        if(linkedCustomer != null) {
            this.linkedCustomer = linkedCustomer;
            btnLinkCustomer.setText(linkedCustomer.getName());
            btnLinkCustomer.getStyleClass().remove("btnBlue");
            btnLinkCustomer.getStyleClass().add("btnYellow");
        } else {
            btnLinkCustomer.setText("VINCULAR CLIENTE");
            this.linkedCustomer = null;
            btnLinkCustomer.getStyleClass().remove("btnYellow");
            btnLinkCustomer.getStyleClass().add("btnBlue");
        }
    }

    public Customer getLinkedCustomer() {
        return linkedCustomer;
    }

    public void setLinkedAnimal(Animal linkedAnimal) {
        if(linkedAnimal != null) {
            this.linkedAnimal = linkedAnimal;
            btnLinkAnimal.setText(linkedAnimal.getName());
            if(btnLinkAnimal.getStyleClass().contains("btnBlue")) {
                btnLinkAnimal.getStyleClass().remove("btnBlue");
                btnLinkAnimal.getStyleClass().add("btnYellow");
            }
            ItemSaleField selected = tableSale.getSelectionModel().getSelectedItem();
            selected.setLinkedAnimal(linkedAnimal);
        } else {
            btnLinkAnimal.setText("VINCULAR ANIMAL");
            ItemSaleField selected = tableSale.getSelectionModel().getSelectedItem();
            if(selected != null) selected.setLinkedAnimal(null);
            this.linkedAnimal = null;
            if(btnLinkAnimal.getStyleClass().contains("btnYellow")) {
                btnLinkAnimal.getStyleClass().remove("btnYellow");
                btnLinkAnimal.getStyleClass().add("btnBlue");
            }
        }
    }
}
