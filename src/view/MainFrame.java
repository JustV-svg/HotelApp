package view;

import javax.swing.*;
import java.awt.*; // Necesario para BorderLayout

public class MainFrame extends JFrame {
    private JPanel mainPanel; // Este es el panel raíz generado por el diseñador
    private JTabbedPane tabbedPane1; // El JTabbedPane que acabas de añadir

    public MainFrame() {
        // Configura el JFrame principal
        setTitle("Sistema de Gestión Hotelera");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Establece el panel principal del formulario como contenido del JFrame
        setContentPane(mainPanel);
        // Empaqueta los componentes para que tomen su tamaño preferido
        pack();
        setSize(800,600);
        // Centra la ventana en la pantalla
        setLocationRelativeTo(null);
        // Haz la ventana visible
        setVisible(true);

        // Aquí es donde añadiremos los paneles personalizados a las pestañas
        // Por ahora, solo HuespedPanel, los otros los haremos después
        // Necesitaremos una instancia de HuespedPanel para añadirla
        // HuespedPanel huespedPanel = new HuespedPanel(); // No instanciamos aquí directamente
        // tabbedPane1.addTab("Huéspedes", huespedPanel.getMainPanel()); // Asegúrate de tener el método getMainPanel() en HuespedPanel

        // Lo que tenemos ahora es un diseño vacío, solo con las pestañas.
        // Los paneles específicos (HuespedPanel, HabitacionPanel, ReservaPanel)
        // los añadiremos cuando los hayamos diseñado y programado.
        // Por ahora, el JTabbedPane se habrá creado con los nombres de las pestañas
        // pero con paneles vacíos dentro.
    }

}
