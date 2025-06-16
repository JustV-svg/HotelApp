package view;

import controller.HuespedController;
import dao.HuespedDAO;
import controller.HabitacionController; // <-- Importar HabitacionController
import dao.HabitacionDAO; // <-- Importar HabitacionDAO

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;

    // Declarar paneles y controladores para Huesped
    private HuespedPanel huespedPanel;
    private HuespedController huespedController;
    private HuespedDAO huespedDAO;

    // Declarar paneles y controladores para Habitacion (NUEVOS)
    private HabitacionPanel habitacionPanel;
    private HabitacionController habitacionController;
    private HabitacionDAO habitacionDAO;

    public MainFrame() {
        setTitle("Sistema de Gestión Hotelera");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel); // Establece el panel raíz de MainFrame.form
        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);

        // Inicialización de DAOs
        huespedDAO = new HuespedDAO();
        habitacionDAO = new HabitacionDAO(); // <-- Inicializar HabitacionDAO

        // Inicialización de paneles y controladores
        huespedPanel = new HuespedPanel();
        huespedController = new HuespedController(huespedPanel, huespedDAO);

        habitacionPanel = new HabitacionPanel(); // <-- Inicializar HabitacionPanel
        habitacionController = new HabitacionController(habitacionPanel, habitacionDAO); // <-- Inicializar HabitacionController

        // Configuración de las pestañas en el JTabbedPane
        // Opcional: Elimina todas las pestañas existentes si el diseñador las creó vacías.
        // Esto asegura que las pestañas se generen siempre con nuestros paneles y no haya duplicados o paneles vacíos.
        tabbedPane1.removeAll(); // <-- Limpia todas las pestañas pre-existentes

        tabbedPane1.addTab("Huéspedes", huespedPanel.getMainPanel()); // Añade la pestaña de Huéspedes
        tabbedPane1.addTab("Habitaciones", habitacionPanel.getMainPanel()); // <-- Añade la pestaña de Habitaciones
        tabbedPane1.addTab("Reservas", new JPanel()); // Placeholder para Reservas, un JPanel vacío por ahora
    }

    // Método main para iniciar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}