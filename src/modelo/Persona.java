package modelo;

/**
 * Entidad usuario de la red social. El identificador se almacena normalizado en minúsculas.
 */
public class Persona {
    public String usuario;
    public String nombre;

    public Persona(String usuario, String nombre) {
        this.usuario = usuario;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre + " (" + usuario + ")";
    }
}
