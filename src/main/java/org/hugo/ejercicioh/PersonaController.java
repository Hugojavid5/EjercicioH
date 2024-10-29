package org.hugo.ejercicioh;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hugo/ejerciciof/NuevaPersona.fxml"));
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

    /**
     * Elimina la persona seleccionada en la tabla.
     * Si no hay ninguna persona seleccionada, muestra una alerta al usuario.
     * @param event Evento de acción que dispara el método
     */
    @FXML
    private void eliminar(ActionEvent event) {
        Personas personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) {
            mostrarAlerta("No hay ninguna persona seleccionada", "Por favor, seleccione una persona para eliminar.");
        } else {
            personasList.remove(personaSeleccionada);
            mostrarAlerta("Persona eliminada", "La persona ha sido eliminada con éxito.");
        }
    }

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
    /**
     * Método que exporta la información de la tabla a un archivo CSV.
     *
     * @param actionEvent Evento disparado al hacer clic en el botón de exportar.
     */
    public void exportar(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar a CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showSaveDialog(btt_agregar.getScene().getWindow());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Escribir cabecera
                writer.write("Nombre,Apellidos,Edad");
                writer.newLine();

                // Escribir datos
                for (Personas persona : personasList) {
                    writer.write(persona.getNombre() + "," + persona.getApellidos() + "," + persona.getEdad());
                    writer.newLine();
                }

                mostrarAlerta("Exportación Exitosa", "Los datos han sido exportados a " + file.getAbsolutePath());
            } catch (IOException e) {
                mostrarAlerta("Error de Exportación", "No se pudo exportar el archivo: " + e.getMessage());
            }
        }
    }
    /**
     * Método que importa datos de un archivo CSV a la tabla.
     *
     * @param actionEvent Evento disparado al hacer clic en el botón de importar.
     */
    public void importar(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar desde CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showOpenDialog(btt_agregar.getScene().getWindow());

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean firstLine = true;

                while ((line = reader.readLine()) != null) {
                    if (firstLine) {
                        firstLine = false; // Saltar la primera línea (cabecera)
                        continue;
                    }

                    String[] datos = line.split(",");

                    // Validar que haya suficientes datos y que no estén vacíos
                    if (datos.length == 3) {
                        String nombre = datos[0].trim();
                        String apellidos = datos[1].trim();
                        Integer edad;

                        // Validar que los campos no estén vacíos
                        if (nombre.isEmpty() || apellidos.isEmpty()) {
                            mostrarAlerta("Error de Importación", "Los campos Nombre y Apellidos no pueden estar vacíos.");
                            continue; // Saltar este registro
                        }

                        // Validar edad
                        try {
                            edad = Integer.parseInt(datos[2].trim());
                        } catch (NumberFormatException e) {
                            mostrarAlerta("Error de Importación", "La edad debe ser un número: " + datos[2]);
                            continue; // Saltar este registro
                        }

                        // Comprobar si la persona ya existe
                        boolean existe = personasList.stream()
                                .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre) && p.getApellidos().equalsIgnoreCase(apellidos) && p.getEdad() == edad);

                        if (!existe) {
                            personasList.add(new Personas(nombre, apellidos, edad));
                        } else {
                            mostrarAlerta("Registro Duplicado", "La persona " + nombre + " " + apellidos + " ya existe.");
                        }
                    } else {
                        mostrarAlerta("Error de Importación", "Línea inválida: " + line);
                    }
                }

                mostrarAlerta("Importación Exitosa", "Los datos han sido importados desde " + file.getAbsolutePath());
            } catch (IOException e) {
                mostrarAlerta("Error de Importación", "No se pudo importar el archivo: " + e.getMessage());
            }
        }
    }
}