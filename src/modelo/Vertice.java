package modelo;

/**
 * Nodo propio de la lista enlazada principal de vértices (personas).
 * Cada vértice mantiene su propia lista de aristas salientes.
 */
public class Vertice {
    public Persona persona;
    public Arista aristas;
    public Vertice sig;

    public Vertice(Persona persona) {
        this.persona = persona;
        this.aristas = null;
        this.sig = null;
    }
}
