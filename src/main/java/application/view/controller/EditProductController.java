package application.view.controller;

import application.controller.object.Product;
import application.model.ProductModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditProductController extends Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtEan;
    @FXML private JFXDatePicker txtDate;
    @FXML private TextField txtPrice;
    @FXML private TextField txtCost;

    @FXML private AnchorPane waitScreen;
    private Product product;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.product = (Product) objects[0];
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
        Mask.upperCase(txtName);
        Mask.upperCase(txtEan);
        Mask.money(txtPrice);
        Mask.money(txtCost);

        txtName.setText(this.product.getName());
        if(this.product.getEan() != null) txtEan.setText(this.product.getEan());
        if(this.product.getShelf_date() != null){
            txtDate.getEditor().setText(Formatter.formatDate(this.product.getShelf_date()));
        }
        txtDate.getEditor().setOnMouseClicked( e -> txtDate.getEditor().setText(""));
        txtPrice.setText(Formatter.formatMoney(this.product.getPrice()));
        txtCost.setText(Formatter.formatMoney(this.product.getCost()));
    }

    @FXML private void save(){
        double price = Formatter.unmaskMoney(txtPrice.getText());
        if(!txtName.getText().isEmpty()) {
            if (price > 0) {
                product.setName(txtName.getText());
                if(!txtEan.getText().isEmpty()){
                    product.setEan(txtEan.getText());
                } else {
                    product.setEan(null);
                }
                if(!txtDate.getEditor().getText().isEmpty()){
                    product.setShelf_date(Formatter.toDate(txtDate.getEditor().getText()));
                } else {
                    product.setShelf_date(null);
                }
                product.setPrice(price);
                product.setCost(Formatter.unmaskMoney(txtCost.getText()));
                if (ProductModel.update(product)) {
                    this.stage.close();
                } else {
                    Window.changeScene(this.stage, "error", this,
                            "Não foi possivel salvar produto");
                }
            } else {
                Window.changeScene(this.stage, "error", this,
                        "Não é possivel salvar um produto com o preço zerado");
            }
        } else {
            Window.changeScene(this.stage, "error", this,
                    "Não é possivel salvar um produto sem nome");
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
