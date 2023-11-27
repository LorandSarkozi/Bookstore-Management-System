package launcher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    Stage window;
    Scene login, menu;

    public static void main(String[] args){
        launch(args);
    }

    // Iterative Programming
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        ComponentFactory componentFactory = ComponentFactory.getInstance(false, window);
    }
}