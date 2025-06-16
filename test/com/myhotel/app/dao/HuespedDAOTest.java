package com.myhotel.app.dao;

import dao.DatabaseConnection;
import dao.HuespedDAO;
import model.Huesped;
import org.junit.jupiter.api.AfterEach; // Import para AfterEach
import org.junit.jupiter.api.BeforeEach; // Import para BeforeEach
import org.junit.jupiter.api.Test;
import java.sql.Connection; // Necesario si usas getConnection en BeforeEach/AfterEach
import java.sql.SQLException; // Necesario para manejar excepciones SQL
import java.sql.Statement; // Necesario para crear Statement en BeforeEach/AfterEach
import java.util.List; // Necesario para el test de obtenerTodosLosHuespedes

import static org.junit.jupiter.api.Assertions.*; // Para assertNotNull, assertTrue, assertEquals, etc.

public class HuespedDAOTest {

    private HuespedDAO huespedDAO;
    private Connection connection; // Para gestionar la conexión a la DB si es necesario

    // Este método se ejecuta ANTES de cada prueba
    @BeforeEach
    void setUp() {
        huespedDAO = new HuespedDAO();
        try {
            connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            // Importante: Eliminar primero de Reservas si tienes Foreign Key Constraints
            // Esto es vital para evitar errores de restricción de clave externa
            stmt.executeUpdate("DELETE FROM Reservas");
            stmt.executeUpdate("DELETE FROM Huespedes"); // Limpiar la tabla Huespedes
            DatabaseConnection.closeConnection(stmt); // Cerrar el statement
        } catch (SQLException e) {
            // Aquí, preferimos que la prueba falle si setUp no puede limpiar
            fail("Fallo en setUp: No se pudo limpiar la base de datos para la prueba: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection); // Cerrar la conexión
        }
    }

    // Este método se ejecuta DESPUÉS de cada prueba
    @AfterEach
    void tearDown() {
        // Se puede limpiar de nuevo para asegurarse, aunque BeforeEach ya lo haga
        try {
            connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM Reservas");
            stmt.executeUpdate("DELETE FROM Huespedes");
            DatabaseConnection.closeConnection(stmt);
        } catch (SQLException e) {
            System.err.println("Advertencia en tearDown: No se pudo limpiar la base de datos después de la prueba: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
    }

    @Test
    void testInsertarHuesped() {
        System.out.println("Ejecutando testInsertarHuesped...");
        Huesped nuevoHuesped = new Huesped(0, "Juan", "Perez", "12345678A", "60010020", "juan.perez@example.com");
        boolean insertado = huespedDAO.insertarHuesped(nuevoHuesped);

        assertTrue(insertado, "El huésped debería haber sido insertado correctamente.");
        assertNotEquals(0, nuevoHuesped.getHuespedID(), "El ID del huésped no debería ser 0 después de la inserción.");

        // Verificar que el huésped realmente existe en la DB
        Huesped huespedRecuperado = huespedDAO.obtenerHuespedPorID(nuevoHuesped.getHuespedID());
        assertNotNull(huespedRecuperado, "El huésped debería ser recuperado por su ID.");
        assertEquals("Juan", huespedRecuperado.getNombre());
        assertEquals("Perez", huespedRecuperado.getApellido());
        System.out.println("testInsertarHuesped completado.");
    }

    @Test
    void testObtenerHuespedPorID() {
        System.out.println("Ejecutando testObtenerHuespedPorID...");
        // Primero insertamos un huésped para poder obtenerlo
        Huesped huespedParaObtener = new Huesped(0, "Maria", "Gomez", "87654321B", "60030040", "maria.gomez@example.com");
        huespedDAO.insertarHuesped(huespedParaObtener);
        assertNotEquals(0, huespedParaObtener.getHuespedID(), "El huésped debe tener un ID generado.");

        Huesped huespedRecuperado = huespedDAO.obtenerHuespedPorID(huespedParaObtener.getHuespedID());
        assertNotNull(huespedRecuperado, "Debería recuperarse un huésped por ID.");
        assertEquals(huespedParaObtener.getNombre(), huespedRecuperado.getNombre());
        assertEquals(huespedParaObtener.getDni(), huespedRecuperado.getDni());
        System.out.println("testObtenerHuespedPorID completado.");
    }

    @Test
    void testActualizarHuesped() {
        System.out.println("Ejecutando testActualizarHuesped...");
        // Insertar un huésped
        Huesped huespedOriginal = new Huesped(0, "Carlos", "Ruiz", "11223344C", "60050060", "carlos.ruiz@example.com");
        huespedDAO.insertarHuesped(huespedOriginal);
        assertNotEquals(0, huespedOriginal.getHuespedID());

        // Actualizar su información
        huespedOriginal.setTelefono("70001111");
        huespedOriginal.setEmail("carlos.nuevo@example.com");
        boolean actualizado = huespedDAO.actualizarHuesped(huespedOriginal);

        assertTrue(actualizado, "El huésped debería haber sido actualizado.");

        // Verificar que la actualización se refleje
        Huesped huespedActualizado = huespedDAO.obtenerHuespedPorID(huespedOriginal.getHuespedID());
        assertNotNull(huespedActualizado);
        assertEquals("70001111", huespedActualizado.getTelefono());
        assertEquals("carlos.nuevo@example.com", huespedActualizado.getEmail());
        System.out.println("testActualizarHuesped completado.");
    }

    @Test
    void testEliminarHuesped() {
        System.out.println("Ejecutando testEliminarHuesped...");
        // Insertar un huésped para eliminar
        Huesped huespedParaEliminar = new Huesped(0, "Ana", "Torres", "99887766D", "60070080", "ana.torres@example.com");
        huespedDAO.insertarHuesped(huespedParaEliminar);
        assertNotEquals(0, huespedParaEliminar.getHuespedID());

        boolean eliminado = huespedDAO.eliminarHuesped(huespedParaEliminar.getHuespedID());
        assertTrue(eliminado, "El huésped debería haber sido eliminado.");

        // Verificar que ya no existe
        Huesped huespedEliminado = huespedDAO.obtenerHuespedPorID(huespedParaEliminar.getHuespedID());
        assertNull(huespedEliminado, "El huésped no debería existir después de la eliminación.");
        System.out.println("testEliminarHuesped completado.");
    }
    @Test
    void testObtenerTodosLosHuespedes() {
        System.out.println("Ejecutando testObtenerTodosLosHuespedes...");
        // Asegurarse de que la tabla esté vacía al inicio de la prueba (gracias a setUp)
        List<Huesped> huespedesIniciales = huespedDAO.obtenerTodosLosHuespedes();
        assertTrue(huespedesIniciales.isEmpty(), "La lista de huéspedes debería estar vacía al inicio del test.");

        // Insertar varios huéspedes
        huespedDAO.insertarHuesped(new Huesped(0, "Pedro", "Gomez", "11111111E", "61111111", "pedro@example.com"));
        huespedDAO.insertarHuesped(new Huesped(0, "Laura", "Diaz", "22222222F", "62222222", "laura@example.com"));

        List<Huesped> todosLosHuespedes = huespedDAO.obtenerTodosLosHuespedes();
        assertFalse(todosLosHuespedes.isEmpty(), "La lista de huéspedes no debería estar vacía.");
        assertEquals(2, todosLosHuespedes.size(), "Debería haber 2 huéspedes en la lista.");

        // Opcional: Verificar contenido específico de la lista
        boolean pedroFound = todosLosHuespedes.stream().anyMatch(h -> h.getNombre().equals("Pedro") && h.getApellido().equals("Gomez"));
        assertTrue(pedroFound, "Pedro Gomez debería estar en la lista.");
        System.out.println("testObtenerTodosLosHuespedes completado.");
    }
}