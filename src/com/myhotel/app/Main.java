package com.myhotel.app; // Asegúrate de que el paquete sea correcto

import view.MainFrame;

import javax.swing.SwingUtilities; // Importa SwingUtilities

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}