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

public class EditCustomerController extends Controller {
    private Customer customer;

    @FXML private TextField txtName;
    @FXML private TextField txtDocument;
    @FXML private TextField txtPhone;

    @FXML private AnchorPane waitScreen;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.customer = (Customer) objects[0];

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
        Mask.document(txtDocument);
        Mask.phone(txtPhone);

        txtName.setText(this.customer.getName());
        if(this.customer.getDocument() != null) txtDocument.setText(this.customer.getDocument());
        txtPhone.setText(this.customer.getPhone());
    }

    @FXML private void save() {
        if(Validator.isName(txtName.getText())){
            this.customer.setName(txtName.getText());
            if(txtDocument.getText().isEmpty() || Validator.isDocument(txtDocument.getText())){
                if(!txtDocument.getText().isEmpty()){
                    customer.setDocument(Formatter.unmaskOnlyNumber(txtDocument.getText()));
                } else {
                    customer.setDocument(null);
                }
                if(Validator.isPhone(txtPhone.getText())){
                    customer.setPhone(Formatter.unmaskOnlyNumber(txtPhone.getText()));
                    customer.setStatus(true);
                    if(CustomerModel.update(customer)){
                        this.stage.close();
                    } else {
                        Window.changeScene(this.stage, "error", this,
                                "Não Foi Possivel Editar Cliente");
                    }
                } else {
                    Window.changeScene(this.stage, "error", this,
                            "Telefone Inválido");
                }
            } else {
                Window.changeScene(this.stage, "error", this,
                        "CPF Inválido");
            }
        } else {
            Window.changeScene(this.stage, "error", this,
                    "É Nescessario Nome e Sobrenome do Cliente");
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
