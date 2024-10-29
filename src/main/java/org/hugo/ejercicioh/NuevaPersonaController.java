package org.hugo.ejercicioh;

import Dao.DaoPersonas;
import Model.Personas;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;


public class NuevaPersonaController {

    @FXML
    private TextField txt_Nombre;

    @FXML
    private TextField txt_Apellidos;

    @FXML
    private TextField txt_Edad;

    private ObservableList<Personas> personasList; // Lista de personas a modificar o agregar
    private Personas personaAEditar = null; // Referencia a la persona a editar, si existe
    private DaoPersonas daoPersona; // Objeto DAO para realizar operaciones de base de datos
    private boolean esModificacion = false; // Indica si se está modificando una persona existente


    public void setPersonasList(ObservableList<Personas> personasList) {
        this.personasList = personasList;
    }

    public void setDaoPersona(DaoPersonas daoPersona) {
        this.daoPersona = daoPersona;
    }

    public void setPersonaAEditar(Personas persona) {
        this.personaAEditar = persona;
        this.esModificacion = true; // Indicador de que se está en modo edición
        rellenarCampos(persona); // Rellenar los campos con los datos de la persona a editar
    }

    public void rellenarCampos(Personas persona) {
        txt_Nombre.setText(persona.getNombre());
        txt_Apellidos.setText(persona.getApellido());
        txt_Edad.setText(String.valueOf(persona.getEdad()));
    }

    @FXML
    private void guardar() {
        String nombre = txt_Nombre.getText().trim();
        String apellidos = txt_Apellidos.getText().trim();
        String edadText = txt_Edad.getText().trim();
        StringBuilder errores = new StringBuilder(); // Acumula los mensajes de error de validación

        // Validaciones de entrada
        if (nombre.isEmpty()) {
            errores.append("El campo 'Nombre' no puede estar vacío.\n");
        }
        if (apellidos.isEmpty()) {
            errores.append("El campo 'Apellidos' no puede estar vacío.\n");
        }

        int edad = -1;
        try {
            edad = Integer.parseInt(edadText);
            if (edad < 0) {
                errores.append("La edad debe ser un número positivo.\n");
            }
        } catch (NumberFormatException e) {
            errores.append("El campo 'Edad' debe ser un número entero válido.\n");
        }

        // Si hay errores, se muestran y se aborta la operación
        if (errores.length() > 0) {
            mostrarError(errores.toString());
            return;
        }

        try {
            if (esModificacion && personaAEditar != null) {
                // Si estamos modificando, actualizamos los datos de la persona existente en la base de datos
                personaAEditar.setNombre(nombre);
                personaAEditar.setApellido(apellidos);
                personaAEditar.setEdad(edad);
                daoPersona.modificar(personaAEditar);

                // Mostrar el mensaje de éxito
                mostrarInformacion("Persona modificada con éxito.");
            } else {
                // Verificar que la nueva persona no sea duplicada antes de agregarla
                Personas nuevaPersona = new Personas(0, nombre, apellidos, edad);
                for (Personas persona : personasList) {
                    if (persona.equals(nuevaPersona)) {
                        mostrarError("Persona duplicada: Ya existe una persona con los mismos datos.");
                        return;
                    }
                }

                // Agregar la nueva persona a la base de datos
                daoPersona.agregar(nuevaPersona);
                // Agregar la nueva persona a la lista observable
                personasList.add(nuevaPersona);
                mostrarInformacion("Persona agregada con éxito.");
            }
        } catch (SQLException e) {
            mostrarError("Error al interactuar con la base de datos: " + e.getMessage());
        }

        // Cerrar la ventana modal después de completar la operación
        cancelar();
    }


    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre
    }


    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error en los datos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); // Muestra la alerta y espera a que el usuario la cierre
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) txt_Nombre.getScene().getWindow(); // Obtiene la ventana actual
        stage.close(); // Cierra la ventana
    }
}
