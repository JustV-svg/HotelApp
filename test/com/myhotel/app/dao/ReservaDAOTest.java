package com.myhotel.app.dao;

import dao.DatabaseConnection;
import dao.HabitacionDAO;
import dao.HuespedDAO;
import dao.ReservaDAO;
import model.Habitacion;
import model.Huesped;
import model.Reserva;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaDAOTest {

    private ReservaDAO reservaDAO;
    private HuespedDAO huespedDAO;
    private HabitacionDAO habitacionDAO;

    private int huespedIdTemp;
    private int habitacionIdTemp;

    @BeforeEach
    void setUp() {
        reservaDAO = new ReservaDAO();
        huespedDAO = new HuespedDAO();
        habitacionDAO = new HabitacionDAO();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM Reservas");
            stmt.executeUpdate("DELETE FROM Huespedes");
            stmt.executeUpdate("DELETE FROM Habitaciones");

            // Insertar un huésped temporal para las pruebas de reserva
            Huesped tempHuesped = new Huesped(0, "HuespedTest", "ApellidoTest", "00000000Z", "111111111", "test@example.com");
            huespedDAO.insertarHuesped(tempHuesped);
            huespedIdTemp = tempHuesped.getHuespedID();
            assertNotEquals(0, huespedIdTemp, "Debe generar ID de Huesped temporal");

            // Insertar una habitación temporal para las pruebas de reserva
            // CAMBIO: "Disponible" (String) a true (boolean)
            Habitacion tempHabitacion = new Habitacion(0, "999", "Simple", new BigDecimal("100.00"), true);
            habitacionDAO.insertarHabitacion(tempHabitacion);
            habitacionIdTemp = tempHabitacion.getHabitacionID();
            assertNotEquals(0, habitacionIdTemp, "Debe generar ID de Habitacion temporal");

        } catch (SQLException e) {
            fail("Fallo en setUp: No se pudo preparar la base de datos para la prueba: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM Reservas");
            stmt.executeUpdate("DELETE FROM Huespedes");
            stmt.executeUpdate("DELETE FROM Habitaciones");
        } catch (SQLException e) {
            System.err.println("Advertencia en tearDown: No se pudo limpiar la base de datos después de la prueba: " + e.getMessage());
        }
    }

    @Test
    void testInsertarReserva() {
        System.out.println("Ejecutando testInsertarReserva...");
        LocalDate fechaEntrada = LocalDate.now();
        LocalDate fechaSalida = LocalDate.now().plusDays(3);
        BigDecimal costoTotal = new BigDecimal("450.00");
        Reserva nuevaReserva = new Reserva(0, huespedIdTemp, habitacionIdTemp, fechaEntrada, fechaSalida, costoTotal, "Activa");

        boolean insertado = reservaDAO.insertarReserva(nuevaReserva);

        assertTrue(insertado, "La reserva debería haber sido insertada correctamente.");
        assertNotEquals(0, nuevaReserva.getReservaID(), "El ID de la reserva no debería ser 0 después de la inserción.");

        Reserva reservaRecuperada = reservaDAO.obtenerReservaPorID(nuevaReserva.getReservaID());
        assertNotNull(reservaRecuperada, "La reserva debería ser recuperada por su ID.");
        assertEquals(huespedIdTemp, reservaRecuperada.getHuespedID());
        assertEquals(habitacionIdTemp, reservaRecuperada.getHabitacionID());
        assertEquals(fechaEntrada, reservaRecuperada.getFechaEntrada());
        assertEquals(fechaSalida, reservaRecuperada.getFechaSalida());
        assertEquals(costoTotal, reservaRecuperada.getCostoTotal());
        assertEquals("Activa", reservaRecuperada.getEstadoReserva()); // Verificar el estado de la reserva
        System.out.println("testInsertarReserva completado.");
    }

    @Test
    void testObtenerReservaPorID() {
        System.out.println("Ejecutando testObtenerReservaPorID...");
        LocalDate fechaEntrada = LocalDate.now().plusDays(1);
        LocalDate fechaSalida = LocalDate.now().plusDays(5);
        BigDecimal costoTotal = new BigDecimal("600.00");
        Reserva reservaParaObtener = new Reserva(0, huespedIdTemp, habitacionIdTemp, fechaEntrada, fechaSalida, costoTotal, "Pendiente");
        reservaDAO.insertarReserva(reservaParaObtener);
        assertNotEquals(0, reservaParaObtener.getReservaID(), "La reserva debe tener un ID generado.");

        Reserva reservaRecuperada = reservaDAO.obtenerReservaPorID(reservaParaObtener.getReservaID());
        assertNotNull(reservaRecuperada, "Debería recuperarse una reserva por ID.");
        assertEquals(reservaParaObtener.getHuespedID(), reservaRecuperada.getHuespedID());
        assertEquals(reservaParaObtener.getFechaEntrada(), reservaRecuperada.getFechaEntrada());
        assertEquals("Pendiente", reservaRecuperada.getEstadoReserva()); // Verificar el estado
        System.out.println("testObtenerReservaPorID completado.");
    }

    @Test
    void testActualizarReserva() {
        System.out.println("Ejecutando testActualizarReserva...");
        LocalDate fechaEntrada = LocalDate.now().plusWeeks(1);
        LocalDate fechaSalida = LocalDate.now().plusWeeks(1).plusDays(2);
        BigDecimal costoOriginal = new BigDecimal("200.00");
        Reserva reservaOriginal = new Reserva(0, huespedIdTemp, habitacionIdTemp, fechaEntrada, fechaSalida, costoOriginal, "Confirmada");
        reservaDAO.insertarReserva(reservaOriginal);
        assertNotEquals(0, reservaOriginal.getReservaID());

        reservaOriginal.setEstadoReserva("Cancelada");
        reservaOriginal.setCostoTotal(new BigDecimal("0.00")); // Por ejemplo, si se cancela

        boolean actualizado = reservaDAO.actualizarReserva(reservaOriginal);
        assertTrue(actualizado, "La reserva debería haber sido actualizada.");

        Reserva reservaActualizada = reservaDAO.obtenerReservaPorID(reservaOriginal.getReservaID());
        assertNotNull(reservaActualizada);
        assertEquals("Cancelada", reservaActualizada.getEstadoReserva());
        assertEquals(new BigDecimal("0.00"), reservaActualizada.getCostoTotal());
        System.out.println("testActualizarReserva completado.");
    }

    @Test
    void testEliminarReserva() {
        System.out.println("Ejecutando testEliminarReserva...");
        LocalDate fechaEntrada = LocalDate.now().plusMonths(1);
        LocalDate fechaSalida = LocalDate.now().plusMonths(1).plusDays(7);
        BigDecimal costo = new BigDecimal("700.00");
        Reserva reservaParaEliminar = new Reserva(0, huespedIdTemp, habitacionIdTemp, fechaEntrada, fechaSalida, costo, "Activa");
        reservaDAO.insertarReserva(reservaParaEliminar);
        assertNotEquals(0, reservaParaEliminar.getReservaID());

        boolean eliminado = reservaDAO.eliminarReserva(reservaParaEliminar.getReservaID());
        assertTrue(eliminado, "La reserva debería haber sido eliminada.");

        Reserva reservaEliminada = reservaDAO.obtenerReservaPorID(reservaParaEliminar.getReservaID());
        assertNull(reservaEliminada, "La reserva no debería existir después de la eliminación.");
        System.out.println("testEliminarReserva completado.");
    }

    @Test
    void testObtenerTodasLasReservas() {
        System.out.println("Ejecutando testObtenerTodasLasReservas...");
        List<Reserva> reservasIniciales = reservaDAO.obtenerTodasLasReservas();
        assertTrue(reservasIniciales.isEmpty(), "La lista de reservas debería estar vacía al inicio del test.");

        LocalDate fecha1 = LocalDate.now().plusDays(2);
        LocalDate fecha2 = LocalDate.now().plusDays(4);
        LocalDate fecha3 = LocalDate.now().plusDays(6);
        LocalDate fecha4 = LocalDate.now().plusDays(8);

        // CAMBIO: true (boolean) en lugar de "Disponible" (String)
        reservaDAO.insertarReserva(new Reserva(0, huespedIdTemp, habitacionIdTemp, fecha1, fecha2, new BigDecimal("100.00"), "Activa"));
        reservaDAO.insertarReserva(new Reserva(0, huespedIdTemp, habitacionIdTemp, fecha3, fecha4, new BigDecimal("200.00"), "Pendiente"));

        List<Reserva> todasLasReservas = reservaDAO.obtenerTodasLasReservas();
        assertFalse(todasLasReservas.isEmpty(), "La lista de reservas no debería estar vacía.");
        assertEquals(2, todasLasReservas.size(), "Debería haber 2 reservas en la lista.");

        boolean reserva1Found = todasLasReservas.stream().anyMatch(r -> r.getCostoTotal().compareTo(new BigDecimal("100.00")) == 0);
        assertTrue(reserva1Found, "La reserva con costo 100.00 debería estar en la lista.");
        System.out.println("testObtenerTodasLasReservas completado.");
    }
}