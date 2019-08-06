package application.view.controller;

import application.controller.object.Product;
import application.model.ProductModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EditProductController extends Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtEan;
    @FXML private TextField txtPrice;

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
        Mask.toUpperCase(txtName);
        Mask.toUpperCase(txtEan);
        Mask.money(txtPrice);
        txtPrice.setAlignment(Pos.CENTER_LEFT);

        txtName.setText(this.product.getName());
        txtEan.setText(this.product.getEan());
        txtPrice.setText(Mask.formatDoubleToMoney(this.product.getPrice()));
    }

    @FXML private void save(){
        double price = Mask.unmaskMoney(txtPrice.getText());
        if(!txtName.getText().isEmpty()) {
            if (price > 0) {
                product.setName(txtName.getText());
                product.setEan(txtEan.getText());
                product.setPrice(price);

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