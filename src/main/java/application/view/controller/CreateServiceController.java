package application.view.controller;

import application.controller.object.Service;
import application.model.ServiceModel;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Mask;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateServiceController extends Controller {

    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtPrice;
    @FXML private TextField txtTime;

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
        Mask.upperCase(txtName);
        Mask.upperCase(txtDescription);
        Mask.money(txtPrice);
        Mask.zeroTo(txtTime, 999);
    }

    @FXML private void create (){
        if(!txtName.getText().isEmpty()){
           Service service = new Service();
           service.setName(txtName.getText());

           if(txtDescription.getText().length() >= 10){
               service.setDescription(txtDescription.getText());

               double price = Formatter.unmaskMoney(txtPrice.getText());
               if(price != 0){
                   service.setPrice(price);

                   int time = Formatter.unmaskInteger(txtTime.getText());
                   if(time != 0) {
                       service.setTime(time);
                       service.setStatus(true);

                       if (ServiceModel.create(service)) {
                           this.stage.close();
                       } else {
                           Window.changeScene(this.stage, "error", this,
                                   "Não Foi Possivel Cadastrar Serviço");
                       }
                   } else {
                       Window.changeScene(this.stage, "error", this,
                               "Defina Um Tempo Extimado Para a Execução do Serviço");
                   }

               } else {
                   Window.changeScene(this.stage, "error", this,
                           "Preço Zerado");
               }

           } else {
               Window.changeScene(this.stage, "error", this,
                       "Descrição Precisa Conter No Minimo 10 Caracteres");
           }

        } else {
            Window.changeScene(this.stage, "error", this,
                    "Nome Obrigatório");
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
