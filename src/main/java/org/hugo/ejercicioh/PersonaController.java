package org.hugo.ejercicioh;

import Dao.DaoPersonas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Model.Personas;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador para la gestión de personas en la interfaz de usuario.
 * Proporciona funcionalidades para agregar, modificar, eliminar y filtrar personas.
 */
public class PersonaController {
    @FXML
    private TableView<Personas> tablaPersonas;

    @FXML
    private TextField txt_filtrar;

    @FXML
    private TableColumn<Personas, String> c_nombre;

    @FXML
    private TableColumn<Personas, String> c_apellidos;

    @FXML
    private TableColumn<Personas, Integer> c_edad;

    @FXML
    private Button btt_agregar;

    @FXML
    private Button btt_modificar;

    @FXML
    private Button btt_eliminar;

    private ObservableList<Personas> personasList = FXCollections.observableArrayList();
    private DaoPersonas daoPersona = new DaoPersonas();

    /**
     * Inicializa el controlador y carga los datos de personas desde la base de datos.
     */
    @FXML
    public void initialize() {
        c_nombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        c_apellidos.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApellido()));
        c_edad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getEdad()).asObject());

        cargarPersonasDesdeBD();
    }

    /**
     * Carga la lista de personas desde la base de datos y actualiza la tabla.
     */
    private void cargarPersonasDesdeBD() {
        try {
            List<Personas> personas = daoPersona.lstPersonas();
            personasList.setAll(personas); // Actualiza la lista observable con los datos obtenidos
            tablaPersonas.setItems(personasList); // Asigna la lista a la tabla
        } catch (SQLException | FileNotFoundException e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos desde la base de datos: " + e.getMessage());
        }
    }

    /**
     * Maneja la acción de agregar o modificar una persona.
     * Abre un modal para ingresar los datos de la persona.
     *
     * @param event Evento de acción que desencadena la apertura del modal.
     */
    @FXML
    private void agregar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hugo/ejercicioh/NuevaPersona.fxml"));
            Parent modalRoot = loader.load();
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(btt_agregar.getScene().getWindow());

            NuevaPersonaController modalController = loader.getController();
            modalController.setPersonasList(personasList);
            modalController.setDaoPersona(daoPersona);  // Pasamos el DAO al modal

            if (event.getSource() == btt_agregar) {
                modalStage.setTitle("Agregar Persona");
            } else if (event.getSource() == btt_modificar) {
                Personas personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
                if (personaSeleccionada == null) {
                    mostrarAlerta("No hay ninguna persona seleccionada", "Por favor, seleccione una persona para editar.");
                    return;
                }
                modalStage.setTitle("Editar Persona");
                modalController.setPersonaAEditar(personaSeleccionada); // Configura la persona a editar
            }

            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

            cargarPersonasDesdeBD();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana: " + e.getMessage());
        }
    }

    /**
     * Maneja la acción de eliminar una persona seleccionada de la tabla.
     *
     * @param event Evento de acción que desencadena la eliminación de la persona.
     */
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

    /**
     * Muestra una alerta con un mensaje y título especificado.
     *
     * @param titulo  El título de la alerta.
     * @param mensaje El mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre
    }

    /**
     * Filtra la lista de personas en la tabla según el texto ingresado en el campo de filtro.
     */
    public void filtrar() {
        String textoFiltro = txt_filtrar.getText().toLowerCase();

        ObservableList<Personas> personasFiltradas = FXCollections.observableArrayList();

        // Filtrar la lista de personas según el nombre
        for (Personas persona : personasList) {
            if (persona.getNombre().toLowerCase().contains(textoFiltro)) {
                personasFiltradas.add(persona);
            }
        }

        tablaPersonas.setItems(personasFiltradas); // Actualiza la tabla con la lista filtrada
    }
}
