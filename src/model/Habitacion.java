package model;
import java.math.BigDecimal; // Importa para usar BigDecimal

public class Habitacion {
    private int habitacionID;
    private String numero;
    private String tipo;
    private BigDecimal precioPorNoche; // Se esta usando BigDecimal para precios
    private String estado;

    public Habitacion() {
    }

    public Habitacion(int habitacionID, String numero, String tipo, BigDecimal precioPorNoche, String estado) {
        this.habitacionID = habitacionID;
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precioPorNoche;
        this.estado = estado;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Habitación " + numero + " (" + tipo + ") - " + estado;
    }
}