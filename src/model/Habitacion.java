package model;

import java.math.BigDecimal; // Importa para usar BigDecimal

public class Habitacion {
    private int habitacionID;
    private String numero;
    private String tipo;
    private BigDecimal precioPorNoche; // Se esta usando BigDecimal para precios
    private boolean disponible;

    public Habitacion() {
        // Constructor vacío es útil si usas setters para construir el objeto
    }

    // Constructor con todos los campos
    public Habitacion(int habitacionID, String numero, String tipo, BigDecimal precioPorNoche, boolean disponible) {
        this.habitacionID = habitacionID;
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precioPorNoche;
        this.disponible = disponible;
    }

    // --- Getters y Setters ---
    public int getHabitacionID() {
        return habitacionID;
    }

    public void setHabitacionID(int habitacionID) {
        this.habitacionID = habitacionID;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getPrecioPorNoche() {
        return precioPorNoche;
    }

    public void setPrecioPorNoche(BigDecimal precioPorNoche) {
        this.precioPorNoche = precioPorNoche;
    }


    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        // Ajustar el toString para mostrar "Disponible" o "No disponible"
        return "Habitación " + numero + " (" + tipo + ") - " + (disponible ? "Disponible" : "No disponible");
    }
}