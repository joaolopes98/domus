package application;

import application.view.auxiliary.Window;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main (String[] args){
        launch();
    }

    public void start(Stage primaryStage) {
//        Window.changeScene(primaryStage, "initial", null);
        Window.changeScene(primaryStage, "pdv", null);
    }
}
