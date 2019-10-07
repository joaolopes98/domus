package application.view.controller;

import application.controller.object.Access;
import application.model.AccessModel;
import application.view.auxiliary.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditUserController extends Controller {

    @FXML private Button btnEmployee;
    @FXML private Button btnVeterinary;
    @FXML private Button btnManager;

    @FXML private TextField txtName;
    @FXML private TextField txtDocument;
    @FXML private TextField txtPhone;

    private ArrayList<Button> listRole = new ArrayList<>();

    @FXML private AnchorPane waitScreen;
    private Access user;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        user = (Access) objects[0];
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);


        setupButtons();
        setupInputs();
    }

    private void setupButtons() {
        listRole.add(btnEmployee);
        listRole.add(btnVeterinary);
        listRole.add(btnManager);

        listRole.forEach( role -> {
            role.setOnAction( e -> {
                resetRoles();
                if(role.getStyleClass().contains("btnBlue")) {
                    role.getStyleClass().remove("btnBlue");
                    role.getStyleClass().add("btnBlueLight");
                }
            });
        });

        switch (user.getRole()){
            case 1:
                btnEmployee.getStyleClass().remove("btnBlue");
                btnEmployee.getStyleClass().add("btnBlueLight");
                break;
            case 2:
                btnVeterinary.getStyleClass().remove("btnBlue");
                btnVeterinary.getStyleClass().add("btnBlueLight");
                break;
            case 3:
                btnManager.getStyleClass().remove("btnBlue");
                btnManager.getStyleClass().add("btnBlueLight");
                break;
        }
    }

    private void resetRoles() {
        listRole.forEach(role -> {
            if(role.getStyleClass().contains("btnBlueLight")){
                role.getStyleClass().remove("btnBlueLight");
                role.getStyleClass().add("btnBlue");
            }
        });
    }

    private void setupInputs() {
        Mask.onlyLetters(txtName);
        txtName.setText(user.getName());

        Mask.document(txtDocument);
        txtDocument.setText(user.getDocument());

        Mask.phone(txtPhone);
        txtPhone.setText(user.getPhone());
    }

    @FXML
    private void save(){
        if(!Validator.isName(txtName.getText())) {
            Window.changeScene(this.stage, "error", this,
                    "É Nescessario Nome e Sobrenome do Usuario");
        } else if(!Validator.isDocument(txtDocument.getText())) {
            Window.changeScene(this.stage, "error", this,
                    "CPF invalido");
        } else if(!Validator.isPhone(txtPhone.getText())) {
            Window.changeScene(this.stage, "error", this,
                    "Telefone invalido");
        } else {

            int role = 1;
            if(btnVeterinary.getStyleClass().contains("btnBlueLight")) role = 2;
            if(btnManager.getStyleClass().contains("btnBlueLight")) role = 3;

            user.setName(txtName.getText());
            user.setDocument(Formatter.unmaskOnlyNumber(txtDocument.getText()));
            user.setPhone(Formatter.unmaskOnlyNumber(txtPhone.getText()));
            user.setRole(role);
            user.setStatus(true);

            if(AccessModel.update(user)){
                cancel();
            } else {
                Window.changeScene(this.stage, "error", this,
                        "Não foi possivel editar o usuario");
            }
        }

    }

    @FXML private void cancel(){
        this.stage.close();
    }

    @Override
    public void activeWaitScreen(boolean wait) {
        waitScreen.setVisible(wait);
    }
}
