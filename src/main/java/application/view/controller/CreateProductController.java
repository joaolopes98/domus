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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CreateProductController extends Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtEan;
    @FXML private JFXDatePicker txtDate;
    @FXML private TextField txtPrice;
    @FXML private TextField txtQuantity;

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
        Mask.toUpperCase(txtName);
        Mask.toUpperCase(txtEan);
        Mask.money(txtPrice);
        Mask.zeroTo(txtQuantity, 999);

        txtDate.getEditor().setOnMouseClicked( e -> txtDate.getEditor().setText(""));
    }

    @FXML private void create (){
        double price = Formatter.unmaskMoney(txtPrice.getText());
        if(!txtName.getText().isEmpty()) {
            if (price > 0) {
                ArrayList<Product> products = new ArrayList<>(
                        ProductModel.getAll("WHERE ean LIKE '" + txtEan.getText() + "'"));
                if(products.isEmpty()) {
                    Product product = new Product();
                    product.setName(txtName.getText());
                    if(!txtEan.getText().isEmpty()) product.setEan(txtEan.getText());
                    if(!txtDate.getEditor().getText().isEmpty()){
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            product.setShelf_date(formatter.parse(txtDate.getEditor().getText()));
                        } catch (ParseException e){
                            e.printStackTrace();
                            Window.changeScene(this.stage, "error", this,
                                    "Erro na data de validade");
                            return;
                        }
                    }
                    product.setPrice(price);
                    product.setQuantity(Formatter.unmaskInteger(txtQuantity.getText()));
                    product.setStatus(true);

                    if (ProductModel.create(product)) {
                        this.stage.close();
                    } else {
                        Window.changeScene(this.stage, "error", this,
                                "Não foi possivel criar produto");
                    }
                } else {
                    Window.changeScene(this.stage, "error", this,
                            "EAN já existente");
                }
            } else {
                Window.changeScene(this.stage, "error", this,
                        "Não é possivel criar um produto com o preço zerado");
            }
        } else {
            Window.changeScene(this.stage, "error", this,
                    "Não é possivel criar um produto sem nome");
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
