package com.crimevariavel;
import javafx.application.Application;
import javafx.stage.Stage;
import com.crimevariavel.util.SceneManager;


public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage);
        SceneManager.navegar("menu");
        stage.setTitle("Crime Variável");
        stage.setMaximized(true);
        stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
 