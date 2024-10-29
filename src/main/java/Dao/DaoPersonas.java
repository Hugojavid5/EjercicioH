package Dao;
import BBDD.conexionBBDD;
import Model.Personas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoPersonas {


    public List<Personas> lstPersonas() throws SQLException {
        Connection conexion = new conexionBBDD().getConnection();
        List<Personas> personas = new ArrayList<>();
        String query = "SELECT * FROM Persona";

        try (Statement statement = conexion.createStatement();
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
        } finally {
            new conexionBBDD().CloseConexion();
        }

        return personas;
    }

    public void agregar(Personas persona) throws SQLException {
        Connection conexion = new conexionBBDD().getConnection();
        String query = "INSERT INTO Persona (nombre, apellidos, edad) VALUES (?, ?, ?)";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setInt(3, persona.getEdad());
            statement.executeUpdate();
        } finally {
            new conexionBBDD().CloseConexion();
        }
    }

    public void modificar(Personas persona) throws SQLException {
        Connection conexion = new conexionBBDD().getConnection();
        String query = "UPDATE Persona SET nombre = ?, apellidos = ?, edad = ? WHERE id = ?";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido());
            statement.setInt(3, persona.getEdad());
            statement.setInt(4, persona.getId());
            statement.executeUpdate();
        } finally {
            new conexionBBDD().CloseConexion();
        }
    }

    public void eliminar(int id) throws SQLException {
        Connection conexion = new conexionBBDD().getConnection();
        String query = "DELETE FROM Persona WHERE id = ?";

        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } finally {
            new conexionBBDD().CloseConexion();
        }
    }
}