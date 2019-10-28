package application.view.controller;

import application.view.auxiliary.Controller;
import application.view.auxiliary.Formatter;
import application.view.auxiliary.Function;
import application.view.auxiliary.Window;
import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.Date;

public class ChoseDateController extends Controller {

    @FXML private JFXDatePicker txtFrom;
    @FXML private JFXDatePicker txtTo;

    private Function function;
    @Override
    public void initialize(Stage oldStage, Scene scene, Controller oldController, Object... objects) {
        this.function = (Function) objects[0];
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if( e.getCode() == KeyCode.ESCAPE){
                cancel();
                e.consume();
            }
        });
        Window.setModal(this.stage, oldStage, oldController);
        super.initialize(oldStage, scene, oldController, objects);
    }


    @FXML private void create(){
        Date dateFrom = Formatter.toDate(txtFrom.getEditor().getText());
        Date dateTo = Formatter.toDate(txtTo.getEditor().getText());

        long from = Formatter.resetDate(dateFrom).getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Formatter.resetDate(dateTo));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        long to = calendar.getTime().getTime();

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        long now = calendar.getTime().getTime();

        if(from >= to){
            Window.changeScene(this.stage, "error", this,
                    "Data inicial maior que a data final");
        } else if (from > now || to > now){
            Window.changeScene(this.stage, "error", this,
                    "Data maior que hoje");
        } else {
            cancel();
            Window.changeScene(this.oldStage, "loading", this,
                    "GERANDO RELATORIO", this.function);
        }

    }

    @FXML private void cancel(){
        this.stage.close();
    }
}
