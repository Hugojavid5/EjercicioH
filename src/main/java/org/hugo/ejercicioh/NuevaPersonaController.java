package org.hugo.ejercicioh;
import Model.Personas;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para gestionar la ventana de agregar o editar una persona.
 * Permite al usuario ingresar y guardar la información de una nueva persona o modificar una existente.
 */
public class NuevaPersonaController {

    @FXML
    private TextField txt_Apellidos;

    @FXML
    private TextField txt_Edad;

    @FXML
    private TextField txt_Nombre;

    /** Lista observable de personas a la cual se añadirá o modificará una persona. */
    private ObservableList<Personas> personasList;

    /** Persona seleccionada para edición; null si se está agregando una nueva persona. */
    private Personas personaAEditar;

    /**
     * Establece la lista observable de personas.
     * @param personasList Lista observable donde se almacenan las personas.
     */
    public void setPersonasList(ObservableList<Personas> personasList) {
        this.personasList = personasList;
    }

    /**
     * Establece la persona a editar y carga sus datos en los campos de texto.
     * Si la persona es null, se asume que se va a crear una nueva persona.
     * @param persona Persona seleccionada para editar, o null si es una nueva persona.
     */
    public void setPersonaAEditar(Personas persona) {
        this.personaAEditar = persona;
        if (persona != null) {
            txt_Nombre.setText(persona.getNombre());
            txt_Apellidos.setText(persona.getApellidos());
            txt_Edad.setText(String.valueOf(persona.getEdad()));
        }
    }

    /**
     * Guarda la nueva persona o actualiza la información de la persona existente.
     * Realiza validación de los campos y muestra mensajes de error si hay entradas inválidas.
     * @param event Evento de acción que dispara el método al hacer clic en el botón de guardar.
     */
    @FXML
    void guardar(ActionEvent event) {
        String error = "";

        if (txt_Nombre.getText().isEmpty()) {
            error += "Introduce un Nombre\n";
        }
        if (txt_Apellidos.getText().isEmpty()) {
            error += "Introduce un Apellido\n";
        }
        if (txt_Edad.getText().isEmpty()) {
            error += "Introduce una Edad\n";
        } else {
            try {
                int edad = Integer.parseInt(txt_Edad.getText());
                if (edad <= 0) {
                    error += "Introduce un número superior a 0\n";
                }
            } catch (NumberFormatException e) {
                error += "Introduce un entero como Edad\n";
            }
        }

        if (error.isEmpty()) {
            if (personaAEditar != null) {
                // Editar persona existente
                personaAEditar.setNombre(txt_Nombre.getText());
                personaAEditar.setApellidos(txt_Apellidos.getText());
                personaAEditar.setEdad(Integer.parseInt(txt_Edad.getText()));
                mostrarInfo("Persona editada correctamente");
            } else {
                // Agregar nueva persona
                Personas nuevaPersona = new Personas(txt_Nombre.getText(), txt_Apellidos.getText(), Integer.parseInt(txt_Edad.getText()));
                if (!personasList.contains(nuevaPersona)) {
                    personasList.add(nuevaPersona);
                    mostrarInfo("Persona añadida correctamente");
                } else {
                    mostrarError("Esa persona ya existe en la lista");
                }
            }
            cerrarVentana();
        } else {
            mostrarError(error);
        }
    }

    /**
     * Cancela la operación y cierra la ventana sin guardar los cambios.
     * @param event Evento de acción que dispara el método al hacer clic en el botón de cancelar.
     */
    @FXML
    void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) txt_Nombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta de error con un mensaje especificado.
     * @param error Mensaje de error a mostrar en la alerta.
     */
    private void mostrarError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText(error);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de información con un mensaje especificado.
     * @param info Mensaje informativo a mostrar en la alerta.
     */
    private void mostrarInfo(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(info);
        alert.showAndWait();
    }
}