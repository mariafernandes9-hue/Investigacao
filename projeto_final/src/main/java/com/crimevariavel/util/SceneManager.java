
package com.crimevariavel.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    
    private static Stage stage;
    
    public static void setStage(Stage s){ 
        stage = s; }
    
    
    public static void navegar(String nomeTela) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/com/crimevariavel/" + nomeTela + ".fxml"));
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            System.err.println("Erro ao carregar tela: " + nomeTela);
            e.printStackTrace();
        }
    }
}
