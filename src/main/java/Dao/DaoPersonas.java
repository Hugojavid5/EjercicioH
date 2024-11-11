package Dao;

import BBDD.ConexionBBDD;
import Model.Personas;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Data Access Object (DAO) para gestionar las operaciones de la tabla 'Persona'
 * en la base de datos. Proporciona métodos para agregar, modificar, eliminar y listar personas.
 */
public class DaoPersonas {

    /**
     * Recupera una lista de todas las personas en la base de datos.
     *
     * @return Una lista de objetos {@link Personas} que representan todas las personas en la base de datos.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public List<Personas> lstPersonas() throws SQLException, FileNotFoundException {
        List<Personas> personas = new ArrayList<>();
        String query = "SELECT * FROM Persona";

        try (Connection conexion = new ConexionBBDD().getConnection();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Personas persona = new Personas(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellidos"),
                        resultSet.getInt("edad")
                );
                personas.add(persona);
            }
        }
        return personas;
    }

    /**
     * Agrega una nueva persona a la base de datos.
     *
     * @param persona El objeto {@link Personas} que se va a agregar.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void agregar(Personas persona) throws SQLException, FileNotFoundException {
        String query = "INSERT INTO Persona (nombre, apellidos, edad) VALUES (?, ?, ?)";

        try (Connection conexion = new ConexionBBDD().getConnection();
             PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setInt(3, persona.getEdad());
            statement.executeUpdate();
        }
    }

    /**
     * Modifica los datos de una persona existente en la base de datos.
     *
     * @param persona El objeto {@link Personas} que contiene los datos actualizados.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void modificar(Personas persona) throws SQLException, FileNotFoundException {
        String query = "UPDATE Persona SET nombre = ?, apellidos = ?, edad = ? WHERE id = ?";

        try (Connection conexion = new ConexionBBDD().getConnection();
             PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setInt(3, persona.getEdad());
            statement.setInt(4, persona.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Elimina una persona de la base de datos por su ID.
     *
     * @param id El ID de la persona que se va a eliminar.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM Persona WHERE id = ?";

        try (Connection conexion = new ConexionBBDD().getConnection();
             PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
