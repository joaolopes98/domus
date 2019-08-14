package application.view.auxiliary;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public abstract class Mask {
    public static void upperCase(TextInputControl textField) {
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

    public static void phone(TextField textField){
        textField.setAlignment(Pos.CENTER_LEFT);
        textField.setText("");
        textField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {

            String numeros = newValue;
            numeros = numeros.replaceAll("\\D", "");

            switch (numeros.length()) {
                case 0:
                    textField.setText("");
                    break;

                case 1:
                case 2:
                    textField.setText("(" + numeros);
                    break;

                case 3:
                case 4:
                case 5:
                case 6:
                    textField.setText("(" + numeros.substring(0,2) + ")" + numeros.substring(2));
                    break;

                case 7:
                case 8:
                case 9:
                case 10:
                    textField.setText("(" + numeros.substring(0,2) + ")" + numeros.substring(2,6) + "-"
                            + numeros.substring(6));
                    break;

                case 11:
                    textField.setText("(" + numeros.substring(0,2) + ")" + numeros.substring(2,7) + "-"
                            + numeros.substring(7));
                    break;

                default:
                    textField.setText("(" + numeros.substring(0,2) + ")" + numeros.substring(2,7) + "-"
                            + numeros.substring(7, 11));
                    break;
            }
            textField.positionCaret(textField.getText().length());
        });


        textField.setOnMouseClicked((EventHandler) -> {
            textField.positionCaret(textField.getText().length());
        });
    }

    public static void document(TextField textField){
        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {

            String text = newValue.replaceAll("\\D", "");

            if(text.length() <= 6) text = text.replaceAll("(\\d{3})(\\d+)", "$1.$2");
            else if(text.length() <= 9) text = text.replaceAll("(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3");
            else if(text.length() <= 11) text = text.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3-$4");
            else text = oldValue;

            textField.setText(text);
            textField.positionCaret(textField.getText().length());
        });

        textField.setOnMouseClicked((EventHandler) -> textField.positionCaret(textField.getText().length()));
    }
}
