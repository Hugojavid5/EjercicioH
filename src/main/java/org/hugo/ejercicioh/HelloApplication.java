package org.hugo.ejercicioh;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de la aplicación JavaFX que inicia la interfaz de usuario.
 * Carga la escena desde un archivo FXML y configura la ventana principal.
 */
public class HelloApplication extends Application {

    /**
     * Método que se llama al iniciar la aplicación.
     * Carga el archivo FXML, configura la escena y muestra la ventana principal.
     *
     * @param stage El escenario principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("EjercicioH.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        Image icon = new Image(getClass().getResourceAsStream("/Imagenes/agenda.png"));
        stage.getIcons().add(icon);
        stage.setMaxWidth(840);
        stage.setMinWidth(565);
        stage.setMinHeight(325);
        stage.setTitle("Personas");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método principal que se utiliza para lanzar la aplicación.
     *
     * @param args Argumentos de línea de comandos pasados a la aplicación.
     */
    public static void main(String[] args) {
        launch();
    }
}
