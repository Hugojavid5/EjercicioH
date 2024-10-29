package org.hugo.ejercicioh;

import Dao.DaoPersonas;
import Model.Personas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador para gestionar la interfaz de la tabla de personas.
 * Permite agregar, modificar y eliminar personas de la lista.
 */
public class PersonaController {

    @FXML
    private Button btt_agregar;

    @FXML
    private TableColumn<Personas, String> c_nombre;

    @FXML
    private TableColumn<Personas, String> c_apellidos;

    @FXML
    private TableColumn<Personas, Integer> c_edad;

    @FXML
    private TableView<Personas> tablaPersonas;

    @FXML
    private Button btt_modificar;

    @FXML
    private Button btt_eliminar;
    @FXML
    private TextField txt_filtrar; // Añade esta línea


    /** Lista observable de personas para mostrar en la tabla */
    private ObservableList<Personas> personasList = FXCollections.observableArrayList();
    private DaoPersonas daoPersona = new DaoPersonas();
    /**
     * Inicializa la tabla y las columnas al cargar la vista.
     * Establece los valores de las columnas para que correspondan a los atributos de la clase Persona.
     */
    @FXML
    void initialize() {
        c_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        c_apellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        c_edad.setCellValueFactory(new PropertyValueFactory<>("edad"));

        // Vincula la lista observable a la tabla
        tablaPersonas.setItems(personasList);
        cargarPersonasDesdeBD();
    }
    private void cargarPersonasDesdeBD() {
        try {
            List<Personas> personas = daoPersona.lstPersonas();
            personasList.setAll(personas); // Actualiza la lista observable con los datos obtenidos
            tablaPersonas.setItems(personasList); // Asigna la lista a la tabla
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos desde la base de datos: " + e.getMessage());
        }
    }

    /**
     * Abre una ventana para modificar la persona seleccionada en la tabla.
     * Si no hay ninguna persona seleccionada, muestra una alerta al usuario.
     * @param event Evento de acción que dispara el método
     */
    @FXML
    private void abrirVentanaModificar(ActionEvent event) {
        Personas personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) {
            mostrarAlerta("No hay ninguna persona seleccionada", "Por favor, seleccione una persona para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hugo/ejercicioh/NuevaPersona.fxml"));
            Parent modalRoot = loader.load();
            NuevaPersonaController modalController = loader.getController();
            modalController.setPersonasList(personasList);
            modalController.setPersonaAEditar(personaSeleccionada);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(btt_modificar.getScene().getWindow());
            modalStage.setTitle("Editar Persona");
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();
            cargarPersonasDesdeBD();
            tablaPersonas.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Abre una nueva ventana para agregar una persona.
     * Actualiza la lista y refresca la tabla una vez se cierra la ventana de agregar.
     * @param event Evento de acción que dispara el método
     * @throws IOException en caso de error al cargar el archivo FXML
     */
    /*
    @FXML
    void agregar(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NuevaPersona.fxml"));
        Parent root = fxmlLoader.load();

        NuevaPersonaController nuevaPersonaController = fxmlLoader.getController();
        nuevaPersonaController.setPersonasList(personasList);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Nueva Persona");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        // Refrescar la tabla después de agregar
        tablaPersonas.refresh();
    }
    */

    /**
     * Elimina la persona seleccionada en la tabla.
     * Si no hay ninguna persona seleccionada, muestra una alerta al usuario.
     * @param event Evento de acción que dispara el método
     */
   /*
    @FXML
    private void eliminar(ActionEvent event) {
        Personas personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) {
            mostrarAlerta("No hay ninguna persona seleccionada", "Por favor, seleccione una persona para eliminar.");
        } else {
            try {
                daoPersona.eliminar(personaSeleccionada.getId());
                personasList.remove(personaSeleccionada); // Actualiza la lista observable eliminando la persona
                mostrarAlerta("Persona eliminada", "La persona ha sido eliminada con éxito.");
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo eliminar la persona: " + e.getMessage());
            }
        }
    }
    */
    /**
     * Muestra una alerta informativa con un título y un mensaje específico
     * @param titulo  Título de la alerta
     * @param mensaje Mensaje de la alerta
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Filtra las personas en la tabla según el texto ingresado en el campo de filtro.
     */
    public void filtrar() {
        String textoFiltro = txt_filtrar.getText().toLowerCase();

        ObservableList<Personas> personasFiltradas = FXCollections.observableArrayList();

        for (Personas persona : personasList) {
            if (persona.getNombre().toLowerCase().contains(textoFiltro)) {
                personasFiltradas.add(persona);
            }
        }

        tablaPersonas.setItems(personasFiltradas);
    }
}