package com.myhotel.app.dao;


import dao.DatabaseConnection;
import dao.HabitacionDAO;
import model.Habitacion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HabitacionDAOTest {

    private HabitacionDAO habitacionDAO;
    private Connection connection;

    @BeforeEach
    void setUp() {
        habitacionDAO = new HabitacionDAO();
        try {
            connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            // Limpiar primero Reservas si tienes Foreign Key Constraints
            // Asegúrate de que esta tabla exista y sea correcta
            stmt.executeUpdate("DELETE FROM Reservas");
            // Limpiar la tabla Habitaciones
            stmt.executeUpdate("DELETE FROM Habitaciones");
            DatabaseConnection.closeConnection(stmt);
        } catch (SQLException e) {
            fail("Fallo en setUp: No se pudo limpiar la base de datos para la prueba: " + e.getMessage());
        } finally {
            // Asegúrate de que connection se cierre SOLO si se abrió aquí
            // Si DatabaseConnection.getConnection() devuelve una conexión nueva cada vez, esto es correcto
            // Pero si es un singleton o pool, la gestión puede ser diferente.
            // Para tests, usualmente se quiere una conexión fresca o limpiar al inicio y final.
            if (connection != null) {
                DatabaseConnection.closeConnection(connection);
            }
        }
    }

    @AfterEach
    void tearDown() {
        try {
            connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM Reservas");
            stmt.executeUpdate("DELETE FROM Habitaciones");
            DatabaseConnection.closeConnection(stmt);
        } catch (SQLException e) {
            System.err.println("Advertencia en tearDown: No se pudo limpiar la base de datos después de la prueba: " + e.getMessage());
        } finally {
            if (connection != null) {
                DatabaseConnection.closeConnection(connection);
            }
        }
    }

    @Test
    void testInsertarHabitacion() {
        System.out.println("Ejecutando testInsertarHabitacion...");
        // CAMBIO: "Disponible" (String) a true (boolean)
        Habitacion nuevaHabitacion = new Habitacion(0, "101", "Doble", new BigDecimal("150.00"), true);
        boolean insertado = habitacionDAO.insertarHabitacion(nuevaHabitacion);

        assertTrue(insertado, "La habitación debería haber sido insertada correctamente.");
        assertNotEquals(0, nuevaHabitacion.getHabitacionID(), "El ID de la habitación no debería ser 0 después de la inserción.");

        Habitacion habitacionRecuperada = habitacionDAO.obtenerHabitacionPorID(nuevaHabitacion.getHabitacionID());
        assertNotNull(habitacionRecuperada, "La habitación debería ser recuperada por su ID.");
        assertEquals("101", habitacionRecuperada.getNumero());
        assertEquals("Doble", habitacionRecuperada.getTipo());
        assertEquals(new BigDecimal("150.00"), habitacionRecuperada.getPrecioPorNoche());
        // CAMBIO: Verificar isDisponible() en lugar de getEstado()
        assertTrue(habitacionRecuperada.isDisponible(), "La habitación recuperada debería estar disponible.");
        System.out.println("testInsertarHabitacion completado.");
    }

    @Test
    void testObtenerHabitacionPorID() {
        System.out.println("Ejecutando testObtenerHabitacionPorID...");
        // CAMBIO: "Ocupada" (String) a false (boolean)
        Habitacion habitacionParaObtener = new Habitacion(0, "205", "Suite", new BigDecimal("300.00"), false);
        habitacionDAO.insertarHabitacion(habitacionParaObtener);
        assertNotEquals(0, habitacionParaObtener.getHabitacionID(), "La habitación debe tener un ID generado.");

        Habitacion habitacionRecuperada = habitacionDAO.obtenerHabitacionPorID(habitacionParaObtener.getHabitacionID());
        assertNotNull(habitacionRecuperada, "Debería recuperarse una habitación por ID.");
        assertEquals(habitacionParaObtener.getNumero(), habitacionRecuperada.getNumero());
        assertEquals(habitacionParaObtener.getTipo(), habitacionRecuperada.getTipo());
        // CAMBIO: Verificar isDisponible()
        assertFalse(habitacionRecuperada.isDisponible(), "La habitación recuperada debería estar ocupada.");
        System.out.println("testObtenerHabitacionPorID completado.");
    }

    @Test
    void testActualizarHabitacion() {
        System.out.println("Ejecutando testActualizarHabitacion...");
        // CAMBIO: "Disponible" (String) a true (boolean)
        Habitacion habitacionOriginal = new Habitacion(0, "301", "Individual", new BigDecimal("80.00"), true);
        habitacionDAO.insertarHabitacion(habitacionOriginal);
        assertNotEquals(0, habitacionOriginal.getHabitacionID());

        habitacionOriginal.setPrecioPorNoche(new BigDecimal("90.00"));
        // CAMBIO: setEstado("En Limpieza") a setDisponible(false)
        habitacionOriginal.setDisponible(false);
        boolean actualizado = habitacionDAO.actualizarHabitacion(habitacionOriginal);

        assertTrue(actualizado, "La habitación debería haber sido actualizada.");

        Habitacion habitacionActualizada = habitacionDAO.obtenerHabitacionPorID(habitacionOriginal.getHabitacionID());
        assertNotNull(habitacionActualizada);
        assertEquals(new BigDecimal("90.00"), habitacionActualizada.getPrecioPorNoche());
        // CAMBIO: Verificar isDisponible() en lugar de getEstado()
        assertFalse(habitacionActualizada.isDisponible(), "La habitación actualizada debería estar no disponible.");
        System.out.println("testActualizarHabitacion completado.");
    }

    @Test
    void testEliminarHabitacion() {
        System.out.println("Ejecutando testEliminarHabitacion...");
        // CAMBIO: "Disponible" (String) a true (boolean)
        Habitacion habitacionParaEliminar = new Habitacion(0, "404", "Doble", new BigDecimal("160.00"), true);
        habitacionDAO.insertarHabitacion(habitacionParaEliminar);
        assertNotEquals(0, habitacionParaEliminar.getHabitacionID());

        boolean eliminado = habitacionDAO.eliminarHabitacion(habitacionParaEliminar.getHabitacionID());
        assertTrue(eliminado, "La habitación debería haber sido eliminada.");

        Habitacion habitacionEliminada = habitacionDAO.obtenerHabitacionPorID(habitacionParaEliminar.getHabitacionID());
        assertNull(habitacionEliminada, "La habitación no debería existir después de la eliminación.");
        System.out.println("testEliminarHabitacion completado.");
    }

    @Test
    void testObtenerTodasLasHabitaciones() {
        System.out.println("Ejecutando testObtenerTodasLasHabitaciones...");
        List<Habitacion> habitacionesIniciales = habitacionDAO.obtenerTodasLasHabitaciones();
        assertTrue(habitacionesIniciales.isEmpty(), "La lista de habitaciones debería estar vacía al inicio del test.");

        // CAMBIO: "Disponible" (String) a true (boolean)
        habitacionDAO.insertarHabitacion(new Habitacion(0, "501", "Suite Ejecutiva", new BigDecimal("400.00"), true));
        // CAMBIO: "Ocupada" (String) a false (boolean)
        habitacionDAO.insertarHabitacion(new Habitacion(0, "502", "Suite Ejecutiva", new BigDecimal("400.00"), false));

        List<Habitacion> todasLasHabitaciones = habitacionDAO.obtenerTodasLasHabitaciones();
        assertFalse(todasLasHabitaciones.isEmpty(), "La lista de habitaciones no debería estar vacía.");
        assertEquals(2, todasLasHabitaciones.size(), "Debería haber 2 habitaciones en la lista.");

        // Este test de verificación de disponibilidad no lo tenías antes, pero es bueno añadirlo
        assertTrue(todasLasHabitaciones.stream().anyMatch(h -> h.getNumero().equals("501") && h.isDisponible()), "La habitación 501 debería estar disponible.");
        assertTrue(todasLasHabitaciones.stream().anyMatch(h -> h.getNumero().equals("502") && !h.isDisponible()), "La habitación 502 debería estar no disponible.");

        boolean habitacion501Found = todasLasHabitaciones.stream().anyMatch(h -> h.getNumero().equals("501"));
        assertTrue(habitacion501Found, "La habitación 501 debería estar en la lista.");
        System.out.println("testObtenerTodasLasHabitaciones completado.");
    }
}