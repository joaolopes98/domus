package application.view.controller;

import application.controller.GenerateFunction;
import application.controller.object.Animal;
import application.view.auxiliary.Controller;
import application.view.auxiliary.Window;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class ChoseAnimalController extends Controller {

    @FXML private Button linkAnimal;
    private Animal linkedAnimal;

    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if( e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);
    }

    @FXML private void linkAnimal(){
        if(linkedAnimal == null) {
            Window.changeScene(this.stage, "linkAnimal", this);
        } else {
            setLinkedAnimal(null);
        }
    }

    @FXML private void create(){
        if(linkedAnimal == null){
            Window.changeScene(this.stage, "error", this,
                    "Selecione um Animal");
        } else {
            cancel();
            Window.changeScene(this.oldStage, "loading", oldController,
                    "GERANDO RELATORIO DE CAIXA", GenerateFunction.reportAnimalHistory(linkedAnimal));
        }
    }

    @FXML private void cancel(){
        this.stage.close();
    }

    public void setLinkedAnimal(Animal linkedAnimal) {
        if(linkedAnimal != null) {
            this.linkedAnimal = linkedAnimal;
            linkAnimal.setText(linkedAnimal.getName());
            if(linkAnimal.getStyleClass().contains("btnBlue")) {
                linkAnimal.getStyleClass().remove("btnBlue");
                linkAnimal.getStyleClass().add("btnYellow");
            }
        } else {
            linkAnimal.setText("VINCULAR ANIMAL");
            this.linkedAnimal = null;
            if(linkAnimal.getStyleClass().contains("btnYellow")) {
                linkAnimal.getStyleClass().remove("btnYellow");
                linkAnimal.getStyleClass().add("btnBlue");
            }
        }
    }
}
