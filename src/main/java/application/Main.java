package application;

import application.model.HibernateUtilities;
import application.view.auxiliary.Window;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main (String[] args){
        launch();
    }

    public void start(Stage primaryStage) {
        Window.changeScene(primaryStage, "initial", null);
    }

    @Override
    public void stop() throws Exception {
        HibernateUtilities.close();
        super.stop();
    }
}
