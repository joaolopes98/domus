package application.view.controller;

import application.controller.object.Access;
import application.controller.object.User;
import application.model.AccessModel;
import application.view.auxiliary.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CreateUserController extends Controller {
    @FXML private Button btnEmployee;
    @FXML private Button btnVeterinary;
    @FXML private Button btnManager;

    @FXML private TextField txtName;
    @FXML private TextField txtDocument;
    @FXML private TextField txtPhone;
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPassword;

    private ArrayList<Button> listRole = new ArrayList<>();

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
        Mask.document(txtDocument);
        Mask.phone(txtPhone);
        Mask.upperCase(txtUser);
        Mask.upperCase(txtPassword);
    }

    @FXML private void create(){
        Access user = new Access();
        if(!Validator.isName(txtName.getText())) {
            Window.changeScene(this.stage, "error", this,
                    "É Nescessario Nome e Sobrenome do Usuario");
        } else if(!Validator.isDocument(txtDocument.getText())) {
            Window.changeScene(this.stage, "error", this,
                    "CPF invalido");
        } else if(!Validator.isPhone(txtPhone.getText())) {
            Window.changeScene(this.stage, "error", this,
                    "Telefone invalido");
        } else if(txtUser.getText().length() < 4 || txtUser.getText().contains(" ")) {
            Window.changeScene(this.stage, "error", this,
                    "O usuario precisa conter 4 ou mais digitos " +
                            "e possuir apenas caracteres alfanumericos sem espaço em branco");
        } else if(txtPassword.getText().length() < 6 || txtPassword.getText().contains(" ")){
            Window.changeScene(this.stage, "error", this,
                    "A senha precisa conter 6 ou mais digitos " +
                            "e possuir apenas caracteres alfanumericos sem espaço em branco");
        } else {

            int role = 1;
            if(btnVeterinary.getStyleClass().contains("btnBlueLight")) role = 2;
            if(btnManager.getStyleClass().contains("btnBlueLight")) role = 3;

            user.setName(txtName.getText());
            user.setDocument(Formatter.unmaskOnlyNumber(txtDocument.getText()));
            user.setPhone(Formatter.unmaskOnlyNumber(txtPhone.getText()));
            user.setUser(txtUser.getText());
            user.setPassword(Formatter.formatSHA512(txtPassword.getText()));
            user.setRole(role);
            user.setStatus(true);

            if(AccessModel.create(user)){
                cancel();
            } else {
                Window.changeScene(this.stage, "error", this,
                        "Não foi possivel criar o usuario");
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
