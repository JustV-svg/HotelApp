package controller;

import dao.HabitacionDAO;
import model.Habitacion;
import view.HabitacionPanel;

import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JOptionPane;

import java.math.BigDecimal; // <-- ¡IMPORTANTE! Importar BigDecimal
import java.util.List;

public class HabitacionController {

    private HabitacionPanel habitacionPanel;
    private HabitacionDAO habitacionDAO;
    private int selectedHabitacionId = -1;

    public HabitacionController(HabitacionPanel habitacionPanel, HabitacionDAO habitacionDAO) {
        this.habitacionPanel = habitacionPanel;
        this.habitacionDAO = habitacionDAO;
        initialize();
    }

    private void initialize() {
        habitacionPanel.setupTableModel();
        loadHabitacionesIntoTable();

        habitacionPanel.getBtnGuardar().addActionListener(e -> guardarHabitacion());
        habitacionPanel.getBtnActualizar().addActionListener(e -> actualizarHabitacion());
        habitacionPanel.getBtnEliminar().addActionListener(e -> eliminarHabitacion());
        habitacionPanel.getBtnLimpiar().addActionListener(e -> limpiarCampos());

        habitacionPanel.getTblHabitaciones().getSelectionModel().addListSelectionListener(this::tableRowSelected);
    }

    private void loadHabitacionesIntoTable() {
        DefaultTableModel model = habitacionPanel.getTableModel();
        model.setRowCount(0);

        List<Habitacion> habitaciones = habitacionDAO.obtenerTodasLasHabitaciones();
        for (Habitacion habitacion : habitaciones) {
            model.addRow(new Object[]{
                    habitacion.getHabitacionID(),
                    habitacion.getNumero(),
                    habitacion.getTipo(),
                    habitacion.getPrecioPorNoche(), // Ya es BigDecimal, la tabla lo muestra bien
                    habitacion.isDisponible() ? "Sí" : "No"
            });
        }
    }

    private void guardarHabitacion() {
        try {
            String numero = habitacionPanel.getTxtNumero().getText();
            String tipo = habitacionPanel.getTxtTipo().getText();
            String precioStr = habitacionPanel.getTxtPrecioNoche().getText();
            boolean disponible = habitacionPanel.getChkDisponible().isSelected();

            if (numero.isEmpty() || tipo.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Número, Tipo y Precio son campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal precioPorNoche; // <-- ¡CAMBIO AQUÍ! Ahora es BigDecimal
            try {
                precioPorNoche = new BigDecimal(precioStr); // <-- ¡CAMBIO AQUÍ! Parsear a BigDecimal
                if (precioPorNoche.compareTo(BigDecimal.ZERO) <= 0) { // <-- ¡CAMBIO AQUÍ! Comparar con BigDecimal.ZERO
                    JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "El precio por noche debe ser un número positivo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "El precio por noche debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // <-- ¡CAMBIO CLAVE AQUÍ! Constructor de Habitacion con el orden correcto:
            // Habitacion(int habitacionID, String numero, String tipo, BigDecimal precioPorNoche, boolean disponible)
            Habitacion nuevaHabitacion = new Habitacion(0, numero, tipo, precioPorNoche, disponible);
            boolean insertado = habitacionDAO.insertarHabitacion(nuevaHabitacion);

            if (insertado) {
                JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Habitación guardada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadHabitacionesIntoTable();
                habitacionPanel.clearFields();
            } else {
                JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Error al guardar la habitación.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Error: " + ex.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void actualizarHabitacion() {
        if (selectedHabitacionId == -1) {
            JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Por favor, selecciona una habitación de la tabla para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String numero = habitacionPanel.getTxtNumero().getText();
            String tipo = habitacionPanel.getTxtTipo().getText();
            String precioStr = habitacionPanel.getTxtPrecioNoche().getText();
            boolean disponible = habitacionPanel.getChkDisponible().isSelected();

            if (numero.isEmpty() || tipo.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Número, Tipo y Precio son campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal precioPorNoche; // <-- ¡CAMBIO AQUÍ! Ahora es BigDecimal
            try {
                precioPorNoche = new BigDecimal(precioStr); // <-- ¡CAMBIO AQUÍ! Parsear a BigDecimal
                if (precioPorNoche.compareTo(BigDecimal.ZERO) <= 0) { // <-- ¡CAMBIO AQUÍ! Comparar con BigDecimal.ZERO
                    JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "El precio por noche debe ser un número positivo.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "El precio por noche debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // <-- ¡CAMBIO CLAVE AQUÍ! Constructor de Habitacion con el orden correcto:
            Habitacion habitacionAActualizar = new Habitacion(selectedHabitacionId, numero, tipo, precioPorNoche, disponible);
            boolean actualizado = habitacionDAO.actualizarHabitacion(habitacionAActualizar);

            if (actualizado) {
                JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Habitación actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadHabitacionesIntoTable();
                habitacionPanel.clearFields();
                selectedHabitacionId = -1;
            } else {
                JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Error al actualizar la habitación.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Error: " + ex.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarHabitacion() {
        if (selectedHabitacionId == -1) {
            JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Por favor, selecciona una habitación de la tabla para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(habitacionPanel.getMainPanel(), "¿Estás seguro de que quieres eliminar esta habitación?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = habitacionDAO.eliminarHabitacion(selectedHabitacionId);
                if (eliminado) {
                    JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Habitación eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadHabitacionesIntoTable();
                    habitacionPanel.clearFields();
                    selectedHabitacionId = -1;
                } else {
                    JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Error al eliminar la habitación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(habitacionPanel.getMainPanel(), "Error: " + ex.getMessage(), "Error Inesperado", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void limpiarCampos() {
        habitacionPanel.clearFields();
        selectedHabitacionId = -1;
        habitacionPanel.getTblHabitaciones().clearSelection();
    }

    private void tableRowSelected(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = habitacionPanel.getTblHabitaciones().getSelectedRow();
            if (selectedRow != -1) {
                selectedHabitacionId = (int) habitacionPanel.getTableModel().getValueAt(selectedRow, 0);

                habitacionPanel.getTxtNumero().setText(habitacionPanel.getTableModel().getValueAt(selectedRow, 1).toString());
                habitacionPanel.getTxtTipo().setText(habitacionPanel.getTableModel().getValueAt(selectedRow, 2).toString());

                // <-- ¡CAMBIO AQUÍ! Obtener BigDecimal de la tabla y convertir a String para JTextField
                BigDecimal precio = (BigDecimal) habitacionPanel.getTableModel().getValueAt(selectedRow, 3);
                habitacionPanel.getTxtPrecioNoche().setText(precio.toPlainString());

                boolean disponible = habitacionPanel.getTableModel().getValueAt(selectedRow, 4).toString().equals("Sí");
                habitacionPanel.getChkDisponible().setSelected(disponible);
            } else {
                selectedHabitacionId = -1;
                habitacionPanel.clearFields();
            }
        }
    }
}