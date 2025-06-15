package model;

public class Huesped {
    private int huespedID;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;

    // Constructor vacío (útil para ciertas operaciones de base de datos)
    public Huesped() {
    }

    // Constructor con todos los campos (excepto ID, si se genera automáticamente)
    public Huesped(int huespedID, String nombre, String apellido, String dni, String telefono, String email) {
        this.huespedID = huespedID;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
        this.email = email;
    }

    // --- Getters y Setters ---
    // Puedes generarlos automáticamente en IntelliJ:
    // Clic derecho dentro de la clase > Generate > Getter and Setter (o Alt+Insert)
    public int getHuespedID() {
        return huespedID;
    }

    public void setHuespedID(int huespedID) {
        this.huespedID = huespedID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + dni + ")";
    }
}