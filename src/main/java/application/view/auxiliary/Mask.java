package application.view.auxiliary;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.text.Normalizer;

public abstract class Mask {
    public static void toUpperCase(TextField textField) {
        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            textField.setText(Formatter.removeAccentuation(newValue.toUpperCase()));
        });
    }

    public static void money(TextField textField){
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
//        textField.setOnKeyPressed( e -> textField.positionCaret(textField.getText().length()+1));
        textField.positionCaret(textField.getText().length()+1);
    }

    public static void zeroTo(TextField textField, int max){
        textField.setText("1");
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String text = newValue.replaceAll("\\D", "");
            text = text.replaceAll("[0]*(\\d+)", "$1");

            if(text.isEmpty()){
                textField.setText("0");
            } else {
                int integer = Integer.parseInt(text);
                if(integer > max){
                    textField.setText(String.valueOf(max));
                } else {
                    textField.setText(text);
                }
            }
        });

        textField.setOnMouseClicked( e -> Mask.toLastPosition(textField));
    }

    public static void toLastPosition(TextField textField){
        textField.requestFocus();
        textField.positionCaret(textField.getText().length());
    }
}
