package application.view.controller;

import application.controller.object.Customer;
import application.model.CustomerModel;
import application.view.auxiliary.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateAnimalController extends Controller {
    @FXML
    private TextField txtName;
    @FXML private TextField txtSpecie;
    @FXML private TextField txtCustomer;

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

        setupInputs();
    }

    private void setupInputs() {
        Mask.onlyLetters(txtName);
        Mask.onlyLetters(txtSpecie);
        txtCustomer.setDisable(true);
    }

    @FXML private void selectCustomer(){

    }

    @FXML private void create (){

    }

    @FXML private void cancel(){
        this.stage.close();
    }

    @Override
    public void activeWaitScreen(boolean wait) {
        waitScreen.setVisible(wait);
    }
}
