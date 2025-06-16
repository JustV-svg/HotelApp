package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // --- Configuración de la Conexión a la Base de Datos ---
    private static final String DB_URL = "jdbc:sqlserver://CHARLIEVIRUS\\SQLEXPRESS;databaseName=HotelDB;encrypt=false;trustServerCertificate=true;";
    private static final String USER = "HotelDB";
    private static final String PASS = "12345";

    /**
     * Establece y devuelve una conexión a la base de datos SQL Server.
     * @return Un objeto Connection si la conexión es exitosa.
     * @throws SQLException Si ocurre un error al conectar a la base de datos.
     */
    public static Connection getConnection() throws SQLException {
        // Cargar el driver JDBC (no estrictamente necesario para JDBC 4.0+ pero es buena práctica)
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC de SQL Server no encontrado. Asegúrate de haberlo añadido al proyecto.");
            throw new SQLException("Driver JDBC de SQL Server no encontrado.", e);
        }
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Cierra una conexión a la base de datos de forma segura.
     * @param connection El objeto Connection a cerrar.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
                // Considera loguear el error o lanzarlo como una RuntimeException si es crítico
            }
        }
    }
}