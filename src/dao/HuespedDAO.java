package dao;

import model.Huesped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HuespedDAO {

    /**
     * Inserta un nuevo huésped en la base de datos.
     * @param huesped El objeto Huesped a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarHuesped(Huesped huesped) {
        String sql = "INSERT INTO Huespedes (Nombre, Apellido, DNI, Telefono, Email) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // Para obtener el ID generado
            pstmt.setString(1, huesped.getNombre());
            pstmt.setString(2, huesped.getApellido());
            pstmt.setString(3, huesped.getDni());
            pstmt.setString(4, huesped.getTelefono());
            pstmt.setString(5, huesped.getEmail());

            int affectedRows = pstmt.executeUpdate(); // Ejecuta la inserción

            if (affectedRows > 0) {
                // Si se insertó una fila, recuperamos el ID generado
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    huesped.setHuespedID(generatedKeys.getInt(1)); // Establece el ID en el objeto Huesped
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar huésped: " + e.getMessage());
            // En una aplicación real, aquí deberías loggear el error o mostrar un mensaje al usuario
        } finally {
            // Cerrar recursos en orden inverso de apertura
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    /**
     * Obtiene un huésped por su ID.
     * @param huespedID El ID del huésped.
     * @return El objeto Huesped si se encuentra, null en caso contrario.
     */
    public Huesped obtenerHuespedPorID(int huespedID) {
        String sql = "SELECT HuespedID, Nombre, Apellido, DNI, Telefono, Email FROM Huespedes WHERE HuespedID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, huespedID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Huesped(
                        rs.getInt("HuespedID"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getString("DNI"),
                        rs.getString("Telefono"),
                        rs.getString("Email")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener huésped por ID: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(rs);
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    /**
     * Obtiene todos los huéspedes de la base de datos.
     * @return Una lista de objetos Huesped.
     */
    public List<Huesped> obtenerTodosLosHuespedes() {
        List<Huesped> huespedes = new ArrayList<>();
        String sql = "SELECT HuespedID, Nombre, Apellido, DNI, Telefono, Email FROM Huespedes";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Huesped huesped = new Huesped(
                        rs.getInt("HuespedID"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getString("DNI"),
                        rs.getString("Telefono"),
                        rs.getString("Email")
                );
                huespedes.add(huesped);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los huéspedes: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(rs);
            DatabaseConnection.closeConnection(stmt);
            DatabaseConnection.closeConnection(conn);
        }
        return huespedes;
    }

    /**
     * Actualiza la información de un huésped existente.
     * @param huesped El objeto Huesped con la información actualizada.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarHuesped(Huesped huesped) {
        String sql = "UPDATE Huespedes SET Nombre = ?, Apellido = ?, DNI = ?, Telefono = ?, Email = ? WHERE HuespedID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, huesped.getNombre());
            pstmt.setString(2, huesped.getApellido());
            pstmt.setString(3, huesped.getDni());
            pstmt.setString(4, huesped.getTelefono());
            pstmt.setString(5, huesped.getEmail());
            pstmt.setInt(6, huesped.getHuespedID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar huésped: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    /**
     * Elimina un huésped de la base de datos por su ID.
     * @param huespedID El ID del huésped a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarHuesped(int huespedID) {
        String sql = "DELETE FROM Huespedes WHERE HuespedID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, huespedID);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            // Considera si el huésped puede tener reservas asociadas, lo que causaría una Foreign Key violation
            System.err.println("Error al eliminar huésped: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(pstmt);
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }
}