package model;

import java.math.BigDecimal;
import java.time.LocalDate; // Para manejar fechas modernas

public class Reserva {
    private int reservaID;
    private int huespedID;    // Solo el ID, la clase Huesped completa se recuperaría aparte si es necesario
    private int habitacionID; // Solo el ID
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private BigDecimal costoTotal;
    private String estadoReserva;

    // Constructor vacío
    public Reserva() {
    }

    // Constructor con todos los campos
    public Reserva(int reservaID, int huespedID, int habitacionID, LocalDate fechaEntrada, LocalDate fechaSalida, BigDecimal costoTotal, String estadoReserva) {
        this.reservaID = reservaID;
        this.huespedID = huespedID;
        this.habitacionID = habitacionID;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.costoTotal = costoTotal;
        this.estadoReserva = estadoReserva;
    }

    // --- Getters y Setters ---
    public int getReservaID() {
        return reservaID;
    }

    public void setReservaID(int reservaID) {
        this.reservaID = reservaID;
    }

    public int getHuespedID() {
        return huespedID;
    }

    public void setHuespedID(int huespedID) {
        this.huespedID = huespedID;
    }

    public int getHabitacionID() {
        return habitacionID;
    }

    public void setHabitacionID(int habitacionID) {
        this.habitacionID = habitacionID;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public BigDecimal getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(String estadoReserva) {
        this.estadoReserva = estadoReserva;
    }
}