package dao;

import model.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ReservaDAO {

    public boolean insertarReserva(Reserva reserva) {
        String sql = "INSERT INTO Reservas (HuespedID, HabitacionID, FechaEntrada, FechaSalida, CostoTotal, EstadoReserva) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, reserva.getHuespedID());
            pstmt.setInt(2, reserva.getHabitacionID());
            pstmt.setDate(3, Date.valueOf(reserva.getFechaEntrada())); // Convertir LocalDate a java.sql.Date
            pstmt.setDate(4, Date.valueOf(reserva.getFechaSalida()));
            pstmt.setBigDecimal(5, reserva.getCostoTotal());
            pstmt.setString(6, reserva.getEstadoReserva());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    reserva.setReservaID(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar reserva: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    public Reserva obtenerReservaPorID(int reservaID) {
        String sql = "SELECT ReservaID, HuespedID, HabitacionID, FechaEntrada, FechaSalida, CostoTotal, EstadoReserva FROM Reservas WHERE ReservaID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservaID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Reserva(
                        rs.getInt("ReservaID"),
                        rs.getInt("HuespedID"),
                        rs.getInt("HabitacionID"),
                        rs.getDate("FechaEntrada").toLocalDate(), // Convertir java.sql.Date a LocalDate
                        rs.getDate("FechaSalida").toLocalDate(),
                        rs.getBigDecimal("CostoTotal"),
                        rs.getString("EstadoReserva")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reserva por ID: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(rs);
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    public List<Reserva> obtenerTodasLasReservas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT ReservaID, HuespedID, HabitacionID, FechaEntrada, FechaSalida, CostoTotal, EstadoReserva FROM Reservas";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Reserva reserva = new Reserva(
                        rs.getInt("ReservaID"),
                        rs.getInt("HuespedID"),
                        rs.getInt("HabitacionID"),
                        rs.getDate("FechaEntrada").toLocalDate(),
                        rs.getDate("FechaSalida").toLocalDate(),
                        rs.getBigDecimal("CostoTotal"),
                        rs.getString("EstadoReserva")
                );
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las reservas: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(rs);
            DatabaseConnection.closeConnection(stmt);
            DatabaseConnection.closeConnection(conn);
        }
        return reservas;
    }

    public boolean actualizarReserva(Reserva reserva) {
        String sql = "UPDATE Reservas SET HuespedID = ?, HabitacionID = ?, FechaEntrada = ?, FechaSalida = ?, CostoTotal = ?, EstadoReserva = ? WHERE ReservaID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reserva.getHuespedID());
            pstmt.setInt(2, reserva.getHabitacionID());
            pstmt.setDate(3, Date.valueOf(reserva.getFechaEntrada()));
            pstmt.setDate(4, Date.valueOf(reserva.getFechaSalida()));
            pstmt.setBigDecimal(5, reserva.getCostoTotal());
            pstmt.setString(6, reserva.getEstadoReserva());
            pstmt.setInt(7, reserva.getReservaID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar reserva: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    public boolean eliminarReserva(int reservaID) {
        String sql = "DELETE FROM Reservas WHERE ReservaID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reservaID);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar reserva: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }
}