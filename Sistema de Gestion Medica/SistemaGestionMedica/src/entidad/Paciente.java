package entidad;

public class Paciente {
    private String id, dni, nombre, telefono;

    public Paciente(String id, String dni, String nombre, String telefono) {
        this.id = id; this.dni = dni; this.nombre = nombre; this.telefono = telefono;
    }
    public String getId() { return id; }
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public void setId(String id) { this.id = id; }
    public void setDni(String dni) { this.dni = dni; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    @Override public String toString() { return nombre; }
}
