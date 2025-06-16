package controller;

import dao.HuespedDAO;
import model.Huesped;
import view.HuespedPanel;

import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent; // Para el evento de selección de la tabla
import javax.swing.JOptionPane; // Para mostrar mensajes al usuario

import java.util.List;

public class HuespedController {

    private HuespedPanel huespedPanel;
    private HuespedDAO huespedDAO;

    // Variable para almacenar el ID del huésped seleccionado en la tabla
    private int selectedHuespedId = -1;

    public HuespedController(HuespedPanel huespedPanel, HuespedDAO huespedDAO) {
        this.huespedPanel = huespedPanel;
        this.huespedDAO = huespedDAO;
        initialize(); // Llama al método de inicialización
    }

    private void initialize() {
        // 1. Configurar el modelo de la tabla
        huespedPanel.setupTableModel();

        // 2. Cargar los huéspedes existentes al inicio
        loadHuespedesIntoTable();

        // 3. Configurar los ActionListeners para los botones
        huespedPanel.getBtnGuardar().addActionListener(e -> guardarHuesped());
        huespedPanel.getBtnActualizar().addActionListener(e -> actualizarHuesped());
        huespedPanel.getBtnEliminar().addActionListener(e -> eliminarHuesped());
        huespedPanel.getBtnLimpiar().addActionListener(e -> limpiarCampos());

        // 4. Configurar el ListSelectionListener para la tabla (cuando se selecciona una fila)
        huespedPanel.getTblHuespedes().getSelectionModel().addListSelectionListener(this::tableRowSelected);
    }

    private void loadHuespedesIntoTable() {
        DefaultTableModel model = huespedPanel.getTableModel();
        model.setRowCount(0); // Limpiar filas existentes

        List<Huesped> huespedes = huespedDAO.obtenerTodosLosHuespedes();
        for (Huesped huesped : huespedes) {
            model.addRow(new Object[]{
                    huesped.getHuespedID(),
                    huesped.getNombre(),
                    huesped.getApellido(),
                    huesped.getDni(),
                    huesped.getTelefono(),
                    huesped.getEmail()
            });
        }
    }

    private void guardarHuesped() {
        try {
            String nombre = huespedPanel.getTxtNombre().getText();
            String apellido = huespedPanel.getTxtApellido().getText();
            String dni = huespedPanel.getTxtDni().getText();
            String telefono = huespedPanel.getTxtTelefono().getText();
            String email = huespedPanel.getTxtEmail().getText();

            // Validaciones básicas (puedes añadir más)
            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
                JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Nombre, Apellido y DNI son campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Huesped nuevoHuesped = new Huesped(0, nombre, apellido, dni, telefono, email);
            boolean insertado = huespedDAO.insertarHuesped(nuevoHuesped);

            if (insertado) {
                JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Huésped guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadHuespedesIntoTable(); // Recargar la tabla
                huespedPanel.clearFields(); // Limpiar campos del formulario
            } else {
                JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Error al guardar el huésped.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Error: " + ex.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarHuesped() {
        if (selectedHuespedId == -1) {
            JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Por favor, selecciona un huésped de la tabla para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String nombre = huespedPanel.getTxtNombre().getText();
            String apellido = huespedPanel.getTxtApellido().getText();
            String dni = huespedPanel.getTxtDni().getText();
            String telefono = huespedPanel.getTxtTelefono().getText();
            String email = huespedPanel.getTxtEmail().getText();

            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
                JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Nombre, Apellido y DNI son campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Huesped huespedAActualizar = new Huesped(selectedHuespedId, nombre, apellido, dni, telefono, email);
            boolean actualizado = huespedDAO.actualizarHuesped(huespedAActualizar);

            if (actualizado) {
                JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Huésped actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadHuespedesIntoTable();
                huespedPanel.clearFields();
                selectedHuespedId = -1; // Resetear la selección
            } else {
                JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Error al actualizar el huésped.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Error: " + ex.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarHuesped() {
        if (selectedHuespedId == -1) {
            JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Por favor, selecciona un huésped de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(huespedPanel.getMainPanel(), "¿Estás seguro de que quieres eliminar este huésped?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = huespedDAO.eliminarHuesped(selectedHuespedId);
                if (eliminado) {
                    JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Huésped eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadHuespedesIntoTable();
                    huespedPanel.clearFields();
                    selectedHuespedId = -1;
                } else {
                    JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Error al eliminar el huésped.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(huespedPanel.getMainPanel(), "Error: " + ex.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void limpiarCampos() {
        huespedPanel.clearFields();
        selectedHuespedId = -1; // Asegurarse de resetear el ID seleccionado
        huespedPanel.getTblHuespedes().clearSelection(); // Deseleccionar cualquier fila
    }

    // Listener para cuando se selecciona una fila en la tabla
    private void tableRowSelected(ListSelectionEvent e) {
        // Asegurarse de que el evento no se dispare dos veces (valor ajustando y no ajustando)
        if (!e.getValueIsAdjusting()) {
            int selectedRow = huespedPanel.getTblHuespedes().getSelectedRow();
            if (selectedRow != -1) { // Si hay una fila seleccionada
                // Obtener el ID del huésped desde la primera columna de la tabla (índice 0)
                selectedHuespedId = (int) huespedPanel.getTableModel().getValueAt(selectedRow, 0);

                // Rellenar los campos de texto con los datos de la fila seleccionada
                huespedPanel.getTxtNombre().setText(huespedPanel.getTableModel().getValueAt(selectedRow, 1).toString());
                huespedPanel.getTxtApellido().setText(huespedPanel.getTableModel().getValueAt(selectedRow, 2).toString());
                huespedPanel.getTxtDni().setText(huespedPanel.getTableModel().getValueAt(selectedRow, 3).toString());
                huespedPanel.getTxtTelefono().setText(huespedPanel.getTableModel().getValueAt(selectedRow, 4).toString());
                huespedPanel.getTxtEmail().setText(huespedPanel.getTableModel().getValueAt(selectedRow, 5).toString());
            } else {
                // Si no hay fila seleccionada (por ejemplo, después de eliminar)
                selectedHuespedId = -1;
                huespedPanel.clearFields();
            }
        }
    }
}