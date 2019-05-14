package application.view.auxiliary;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;

public abstract class Mask {
    public static void maskMoney(TextField textField){
        textField.setText("R$ 0,00");
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String text = newValue.replaceAll("\\D", "");
            text = text.replaceAll("[0]*(\\d+)", "$1");

            if(text.length() == 0){
                textField.setText("R$ 0,00");
            } else if (text.length() == 1){
                textField.setText("R$ 0,0" + text);
            } else if(text.length() == 2){
                textField.setText("R$ 0,"+ text);
            } else if (text.length() < 6){
                textField.setText("R$ " + text.replaceAll("(\\d+)(\\d{2})", "$1,$2"));
            } else if(text.length()  < 9){
                textField.setText("R$ " + text.replaceAll("(\\d+)(\\d{3})(\\d{2})", "$1.$2,$3"));
            } else {
                textField.setText(oldValue);
            }

            textField.positionCaret(textField.getText().length()+1);
        });

        textField.setOnMouseClicked( e -> textField.positionCaret(textField.getText().length()+1));
        textField.setOnKeyPressed( e -> textField.positionCaret(textField.getText().length()+1));
    }

    public static double unmaskMoney(String value){
        String text = value.replace("R$", "")
                .replace(".","")
                .replace(",",".");

        return Double.parseDouble(text);
    }
}
