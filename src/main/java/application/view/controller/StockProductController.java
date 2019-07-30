package application.view.controller;

import application.controller.object.Product;
import application.model.ProductModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class StockProductController extends Controller {

    @FXML private Label lblQuantity;
    @FXML private TextField txtQuantity;
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

        setup();
    }

    private void setup() {
        Mask.zeroTo(txtQuantity, 999);

        lblQuantity.setText(String.valueOf(this.product.getQuantity()));
    }

    @FXML private void upQuantity(){
        int quantity = Mask.unmaskInteger(lblQuantity.getText());
        quantity += Mask.unmaskInteger(txtQuantity.getText());

        lblQuantity.setText(String.valueOf(quantity));
        txtQuantity.setText("0");
    }

    @FXML private void downQuantity(){
        int quantity = Mask.unmaskInteger(lblQuantity.getText());
        quantity -= Mask.unmaskInteger(txtQuantity.getText());

        if(quantity >= 0) {
            lblQuantity.setText(String.valueOf(quantity));
            txtQuantity.setText("0");
        } else {
            Window.changeScene(this.stage, "error", this,
                    "Impossivel remover essa quantidade do estoque");
            txtQuantity.setText("0");
        }
    }

    @FXML private void save(){
        this.product.setQuantity(Mask.unmaskInteger(lblQuantity.getText()));

        if(ProductModel.update(product)){
            this.stage.close();
        } else{
            Window.changeScene(this.stage, "error", this,
                    "Não foi possivel atualizar o estoque deste produto");
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
