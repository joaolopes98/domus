package application.view.controller;

import application.controller.UserField;
import application.controller.object.Access;
import application.model.AccessModel;
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

public class UsersController extends Controller {
    @FXML private TextField txtSearch;
    @FXML private TableView<UserField> tableUsers;
    @FXML private TableColumn<UserField, String> userCode;
    @FXML private TableColumn<UserField, String> userName;
    @FXML private TableColumn<UserField, String> userDocument;
    @FXML private TableColumn<UserField, String> userPhone;
    @FXML private TableColumn<UserField, HBox> userAction;
    private ObservableList<UserField> obsUser = FXCollections.observableArrayList();

    @FXML private AnchorPane waitScreen;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);

        setupTxtSearch();
        setupTableAnimals();
    }

    private void setupTxtSearch() {
        Mask.upperCase(txtSearch);
        txtSearch.textProperty().addListener(((observable, oldValue, newValue) -> updateTable()));
    }

    private void setupTableAnimals() {
        userCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        userName.setCellValueFactory(new PropertyValueFactory<>("name"));
        userDocument.setCellValueFactory(new PropertyValueFactory<>("document"));
        userPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        userAction.setCellValueFactory(new PropertyValueFactory<>("action"));
        tableUsers.setItems(obsUser);

        tableUsers.setRowFactory( new Callback<TableView<UserField>, TableRow<UserField>>() {
            @Override
            public TableRow<UserField> call(TableView<UserField> tableView) {
                return new TableRow<UserField>() {
                    @Override
                    protected void updateItem(UserField animalField, boolean empty) {
                        super.updateItem(animalField, empty);
                        if(!empty) {
                            getStyleClass().clear();
                            if (animalField.getAccess().isStatus()) {
                                getStyleClass().add("activated");
                            } else {
                                getStyleClass().add("disabled");
                            }
                        }
                    }
                };
            }
        });

        updateTable();
    }

    public void updateTable() {
        obsUser.clear();
        ArrayList<Access> users = new ArrayList<>();
        if(txtSearch.getText().isEmpty()){
            users.addAll(AccessModel.getAll(""));
        } else {
            if(txtSearch.getText().matches("\\D+")) {
                users.addAll(AccessModel.getAll("WHERE name LIKE '%" + txtSearch.getText() + "%' " +
                        "OR user LIKE '%" + txtSearch.getText() + "%'"));
            } else {
                users.addAll(AccessModel.getAll("WHERE phone LIKE '%" + txtSearch.getText() + "%' " +
                        "OR document LIKE '%" + txtSearch.getText() + "%'"));
            }
        }
        users.forEach( user -> obsUser.add(new UserField(user, this)));
        this.tableUsers.refresh();
    }

    @FXML private void createUser() {
        Window.changeScene(this.stage, "createUser", this);
    }

    @FXML private void cancel() {
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
