package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel; // Necesario para la tabla
import java.awt.event.ActionListener; // Para los eventos de los botones

public class HuespedPanel extends JPanel { // Extiende JPanel
    private JPanel mainPanel; // Este es el panel raíz del HuespedPanel.form

    // Campos de texto para la entrada de datos del huésped
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtEmail;

    // Botones de acción
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    // Tabla para mostrar los huéspedes
    private JTable tblHuespedes;

    // Modelo de la tabla
    private DefaultTableModel tableModel;

    public HuespedPanel() {
        // No hacemos nada aquí por ahora, el UI Designer se encarga de inflar el mainPanel
        // setContentPane(mainPanel); // Esto NO se hace en un JPanel, solo en un JFrame
        // add(mainPanel); // Si el panel raíz del .form no está directamente en el JTabbedPane
        // Pero como lo vamos a añadir directamente al JTabbedPane, no necesitamos esto aquí.
        // IntelliJ ya enlaza el mainPanel del .form a esta clase.
    }

    // --- Getters para acceder a los componentes desde el controlador ---
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtApellido() {
        return txtApellido;
    }

    public JTextField getTxtDni() {
        return txtDni;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public JTable getTblHuespedes() {
        return tblHuespedes;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    // --- Método para inicializar el modelo de la tabla ---
    public void setupTableModel() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Apellido", "DNI", "Teléfono", "Email"}, 0);
        tblHuespedes.setModel(tableModel);
    }

    // --- Método para limpiar los campos de texto ---
    public void clearFields() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        // Si hay un campo para ID, también límpialo
    }
}