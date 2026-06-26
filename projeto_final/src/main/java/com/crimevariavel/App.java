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
        stage.setWidth(1280);
        stage.setHeight(700);
        stage.setResizable(false);  // trava o tamanho
        stage.centerOnScreen();
    }
    public static void main(String[] args) { launch(args); }
}
 