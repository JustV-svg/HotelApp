package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HabitacionPanel extends JPanel {
    private JPanel mainPanel; // Panel raíz del formulario
    private JTextField txtNumero;
    private JTextField txtTipo;
    private JTextField txtPrecioNoche;
    private JCheckBox chkDisponible; // Nuevo componente para el estado
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JTable tblHabitaciones;
    private JScrollBar scrollBar1;

    private DefaultTableModel tableModel;

    public HabitacionPanel() {
        // Constructor vacío, el UI Designer se encarga de inflar mainPanel
    }

    // Getters para acceder a los componentes desde el controlador
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getTxtNumero() {
        return txtNumero;
    }

    public JTextField getTxtTipo() {
        return txtTipo;
    }

    public JTextField getTxtPrecioNoche() {
        return txtPrecioNoche;
    }

    public JCheckBox getChkDisponible() {
        return chkDisponible;
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

    public JTable getTblHabitaciones() {
        return tblHabitaciones;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    // Método para inicializar el modelo de la tabla
    public void setupTableModel() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Número", "Tipo", "Precio/Noche", "Disponible"}, 0);
        tblHabitaciones.setModel(tableModel);
    }

    // Método para limpiar los campos de texto
    public void clearFields() {
        txtNumero.setText("");
        txtTipo.setText("");
        txtPrecioNoche.setText("");
        chkDisponible.setSelected(false); // Desmarcar el checkbox
    }
}