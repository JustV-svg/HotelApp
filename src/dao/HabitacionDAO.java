package dao;

import model.Habitacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // Importar BigDecimal

public class HabitacionDAO {

    public boolean insertarHabitacion(Habitacion habitacion) {
        String sql = "INSERT INTO Habitaciones (Numero, Tipo, PrecioPorNoche, Estado) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, habitacion.getNumero());
            pstmt.setString(2, habitacion.getTipo());
            pstmt.setBigDecimal(3, habitacion.getPrecioPorNoche()); // Usar setBigDecimal
            pstmt.setString(4, habitacion.getEstado());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    habitacion.setHabitacionID(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar habitación: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    public Habitacion obtenerHabitacionPorID(int habitacionID) {
        String sql = "SELECT HabitacionID, Numero, Tipo, PrecioPorNoche, Estado FROM Habitaciones WHERE HabitacionID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, habitacionID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Habitacion(
                        rs.getInt("HabitacionID"),
                        rs.getString("Numero"),
                        rs.getString("Tipo"),
                        rs.getBigDecimal("PrecioPorNoche"), // Usar getBigDecimal
                        rs.getString("Estado")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener habitación por ID: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(rs);
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    public List<Habitacion> obtenerTodasLasHabitaciones() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT HabitacionID, Numero, Tipo, PrecioPorNoche, Estado FROM Habitaciones";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Habitacion habitacion = new Habitacion(
                        rs.getInt("HabitacionID"),
                        rs.getString("Numero"),
                        rs.getString("Tipo"),
                        rs.getBigDecimal("PrecioPorNoche"),
                        rs.getString("Estado")
                );
                habitaciones.add(habitacion);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las habitaciones: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(rs);
            DatabaseConnection.closeConnection(stmt);
            DatabaseConnection.closeConnection(conn);
        }
        return habitaciones;
    }

    public boolean actualizarHabitacion(Habitacion habitacion) {
        String sql = "UPDATE Habitaciones SET Numero = ?, Tipo = ?, PrecioPorNoche = ?, Estado = ? WHERE HabitacionID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, habitacion.getNumero());
            pstmt.setString(2, habitacion.getTipo());
            pstmt.setBigDecimal(3, habitacion.getPrecioPorNoche());
            pstmt.setString(4, habitacion.getEstado());
            pstmt.setInt(5, habitacion.getHabitacionID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar habitación: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    public boolean eliminarHabitacion(int habitacionID) {
        String sql = "DELETE FROM Habitaciones WHERE HabitacionID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, habitacionID);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar habitación: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }
}