package com.myhotel.app.dao; // Asegúrate de que el paquete sea el correcto (com.myhotel.app.dao)

import dao.DatabaseConnection;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*; // Importa los métodos de aserción de JUnit

public class DatabaseConnectionTest {

    @Test // Esta anotación indica que este método es una prueba unitaria
    void testGetConnection() {
        Connection connection = null;
        try {
            // Intentamos obtener una conexión a la base de datos
            connection = DatabaseConnection.getConnection();

            // Afirmamos que la conexión no debe ser nula, es decir, se obtuvo correctamente
            assertNotNull(connection, "La conexión a la base de datos no debería ser nula.");

            // Afirmamos que la conexión no debe estar cerrada inmediatamente
            assertFalse(connection.isClosed(), "La conexión no debería estar cerrada.");

            System.out.println("¡Conexión a la base de datos exitosa!");

        } catch (SQLException e) {
            // Si hay una SQLException, la prueba falla
            fail("Fallo al conectar a la base de datos: " + e.getMessage());
        } finally {
            // Aseguramos que la conexión se cierre al finalizar la prueba, exitosa o fallida
            DatabaseConnection.closeConnection(connection);
        }
    }
}