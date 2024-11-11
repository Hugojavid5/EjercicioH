package BBDD;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * La clase {@code ConexionBBDD} gestiona la conexión a una base de datos.
 * Se encarga de cargar las propiedades de conexión desde un archivo
 * y proporcionar un metodo para obtener la conexion.
 */
public class ConexionBBDD {
    private final Connection connection;

    /**
     * Crea una nueva conexión a la base de datos utilizando las propiedades
     * definidas en el archivo "db.properties".
     *
     * @throws SQLException si ocurre un error al establecer la conexión a la base de datos.
     * @throws FileNotFoundException si el archivo de propiedades no se encuentra.
     */
    public ConexionBBDD() throws SQLException, FileNotFoundException {
        Properties props = loadProperties();
        String url = props.getProperty("dburl");
        connection = DriverManager.getConnection(url, props);

        // Obtiene información sobre la base de datos.
        DatabaseMetaData databaseMetaData = connection.getMetaData();
    }

    /**
     * Devuelve la conexión a la base de datos.
     *
     * @return la conexión activa.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos.
     *
     * @throws SQLException si ocurre un error al cerrar la conexión.
     * @return la conexión cerrada (null si se ha cerrado correctamente).
     */
    public Connection closeConexion() throws SQLException {
        connection.close();
        return connection;
    }

    /**
     * Carga las propiedades de conexión desde el archivo "db.properties".
     *
     * @return un objeto {@code Properties} que contiene las propiedades de conexión.
     * @throws FileNotFoundException si el archivo de propiedades no se encuentra.
     */
    public static Properties loadProperties() throws FileNotFoundException {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
