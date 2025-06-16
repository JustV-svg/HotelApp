package view;

import controller.HuespedController;
import dao.HuespedDAO;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel; // El panel raíz del MainFrame.form
    private JTabbedPane tabbedPane1; // El JTabbedPane que creaste

    private HuespedPanel huespedPanel;
    private HuespedController huespedController;
    private HuespedDAO huespedDAO;

    public MainFrame() {
        setTitle("Sistema de Gestión Hotelera");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel); // Establece el panel generado por el UI Designer
        setSize(900, 700); // Un tamaño un poco más grande para empezar
        setLocationRelativeTo(null);
        setVisible(true);

        // Inicializar el DAO de Huéspedes
        huespedDAO = new HuespedDAO();

        // Inicializar el panel de Huéspedes y su controlador
        huespedPanel = new HuespedPanel();
        huespedController = new HuespedController(huespedPanel, huespedDAO);

        // Enlazar el panel de Huéspedes a la primera pestaña (índice 0)
        // Asegúrate de que esta pestaña tenga el título "Huéspedes" en el diseñador
        // Ojo: Si el UI Designer ya creó pestañas vacías, esto las reemplaza.
        // Si no hay pestañas creadas en el .form, usa addTab
        if (tabbedPane1.getTabCount() > 0 && tabbedPane1.getTitleAt(0).equals("Huéspedes")) {
            tabbedPane1.setComponentAt(0, huespedPanel.getMainPanel());
        } else {
            tabbedPane1.addTab("Huéspedes", huespedPanel.getMainPanel());
        }
        // Puedes eliminar las otras pestañas vacías si no las necesitas aún o dejar que el código las añada.
        // Si el UI Designer ya creó "Habitaciones" y "Reservas", no necesitas estas líneas:
        // tabbedPane1.addTab("Habitaciones", new JPanel());
        // tabbedPane1.addTab("Reservas", new JPanel());

        // Si tienes problemas para que se vean bien las pestañas, lo más sencillo es:
        // 1. En el MainFrame.form, elimina todas las pestañas del JTabbedPane.
        // 2. Y luego en MainFrame.java, usa solo estas líneas:
        // tabbedPane1.addTab("Huéspedes", huespedPanel.getMainPanel());
        // tabbedPane1.addTab("Habitaciones", new JPanel()); // Placeholder
        // tabbedPane1.addTab("Reservas", new JPanel());    // Placeholder
    }
}