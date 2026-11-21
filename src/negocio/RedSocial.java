package negocio;

import modelo.Arista;
import modelo.Persona;
import modelo.TipoRelacion;
import modelo.Vertice;

/**
 * Red social dinámica «Conexión Ambato» implementada mediante multilista
 * (lista enlazada de vértices y listas enlazadas de aristas con nodos propios).
 * No utiliza HashMap, HashSet, ArrayList ni LinkedList.
 *
 * Autor: Cunalata Mendoza Damian Alexander
 */
public class RedSocial {

    private Vertice cabeza;

    public RedSocial() {
        this.cabeza = null;
    }

    // =========================================================================
    // AUXILIARES PRIVADOS
    // =========================================================================

    private boolean esUsuarioValido(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return false;
        }
        for (int i = 0; i < usuario.length(); i++) {
            char c = usuario.charAt(i);
            if (Character.isWhitespace(c) || c == ',') {
                return false;
            }
        }
        return true;
    }

    private Vertice buscar(String usuario) {
        if (usuario == null) {
            return null;
        }
        String clave = usuario.trim().toLowerCase();
        for (Vertice v = cabeza; v != null; v = v.sig) {
            if (v.persona.usuario.equals(clave)) {
                return v;
            }
        }
        return null;
    }

    private void insertar(Vertice origen, Vertice destino, TipoRelacion tipo) {
        Arista a = new Arista();
        a.destino = destino;
        a.tipo = tipo;
        a.sig = origen.aristas;
        origen.aristas = a;
    }

    private boolean existe(Vertice origen, Vertice destino, TipoRelacion tipo) {
        for (Arista a = origen.aristas; a != null; a = a.sig) {
            if (a.destino == destino && a.tipo == tipo) {
                return true;
            }
        }
        return false;
    }

    private boolean quitarArista(Vertice origen, Vertice destino, TipoRelacion tipo) {
        Arista anterior = null;
        Arista actual = origen.aristas;
        while (actual != null) {
            if (actual.destino == destino && actual.tipo == tipo) {
                if (anterior == null) {
                    origen.aristas = actual.sig;
                } else {
                    anterior.sig = actual.sig;
                }
                return true;
            }
            anterior = actual;
            actual = actual.sig;
        }
        return false;
    }

    private void quitarHacia(Vertice origen, Vertice objetivo) {
        while (origen.aristas != null && origen.aristas.destino == objetivo) {
            origen.aristas = origen.aristas.sig;
        }
        Arista actual = origen.aristas;
        while (actual != null && actual.sig != null) {
            if (actual.sig.destino == objetivo) {
                actual.sig = actual.sig.sig;
            } else {
                actual = actual.sig;
            }
        }
    }

    // =========================================================================
    // GESTIÓN DE PERSONAS
    // =========================================================================

    public boolean registrarPersona(String usuario, String nombre) {
        if (!esUsuarioValido(usuario) || nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        String clave = usuario.trim().toLowerCase();
        if (buscar(clave) != null) {
            return false;
        }
        Vertice nuevo = new Vertice(new Persona(clave, nombre.trim()));
        nuevo.sig = cabeza;
        cabeza = nuevo;
        return true;
    }

    public boolean eliminarPersona(String usuario) {
        Vertice objetivo = buscar(usuario);
        if (objetivo == null) {
            return false;
        }
        for (Vertice w = cabeza; w != null; w = w.sig) {
            quitarHacia(w, objetivo);
        }
        Vertice anterior = null;
        Vertice actual = cabeza;
        while (actual != objetivo) {
            anterior = actual;
            actual = actual.sig;
        }
        if (anterior == null) {
            cabeza = objetivo.sig;
        } else {
            anterior.sig = objetivo.sig;
        }
        objetivo.aristas = null;
        objetivo.sig = null;
        return true;
    }

    // =========================================================================
    // OPERACIONES DE RELACIONES (AMISTAD Y SEGUIR)
    // =========================================================================

    public boolean agregarAmistad(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || existe(va, vb, TipoRelacion.AMISTAD)) {
            return false;
        }
        insertar(va, vb, TipoRelacion.AMISTAD);
        insertar(vb, va, TipoRelacion.AMISTAD);
        return true;
    }

    public boolean seguir(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || existe(va, vb, TipoRelacion.SEGUIR)) {
            return false;
        }
        insertar(va, vb, TipoRelacion.SEGUIR);
        return true;
    }

    public boolean eliminarAmistad(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb || !existe(va, vb, TipoRelacion.AMISTAD)) {
            return false;
        }
        quitarArista(va, vb, TipoRelacion.AMISTAD);
        quitarArista(vb, va, TipoRelacion.AMISTAD);
        return true;
    }

    public boolean dejarDeSeguir(String a, String b) {
        Vertice va = buscar(a);
        Vertice vb = buscar(b);
        if (va == null || vb == null || va == vb) {
            return false;
        }
        return quitarArista(va, vb, TipoRelacion.SEGUIR);
    }

    // =========================================================================
    // CONSULTAS, CONTEO Y VISUALIZACIÓN
    // =========================================================================

    public String listarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Arista a = v.aristas; a != null; a = a.sig) {
            if (a.tipo == TipoRelacion.AMISTAD) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(a.destino.persona.toString());
            }
        }
        return sb.length() == 0 ? "Ninguno" : sb.toString();
    }

    public String listarSeguidos(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Arista a = v.aristas; a != null; a = a.sig) {
            if (a.tipo == TipoRelacion.SEGUIR) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(a.destino.persona.toString());
            }
        }
        return sb.length() == 0 ? "Ninguno" : sb.toString();
    }

    public String listarSeguidores(String usuario) {
        Vertice objetivo = buscar(usuario);
        if (objetivo == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Vertice w = cabeza; w != null; w = w.sig) {
            if (w != objetivo && existe(w, objetivo, TipoRelacion.SEGUIR)) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(w.persona.toString());
            }
        }
        return sb.length() == 0 ? "Ninguno" : sb.toString();
    }

    public int contarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        if (v == null) {
            return -1;
        }
        int total = 0;
        for (Arista a = v.aristas; a != null; a = a.sig) {
            if (a.tipo == TipoRelacion.AMISTAD) {
                total++;
            }
        }
        return total;
    }

    public String mostrarRed() {
        if (cabeza == null) {
            return "(Red vacía)";
        }
        StringBuilder sb = new StringBuilder();
        int totalUsuarios = 0;
        int aristasAmistad = 0;
        int aristasSeguir = 0;

        for (Vertice v = cabeza; v != null; v = v.sig) {
            totalUsuarios++;
            sb.append("[").append(v.persona.usuario.toUpperCase()).append(" - ")
              .append(v.persona.nombre).append("] ");

            StringBuilder amigos = new StringBuilder();
            StringBuilder seguidos = new StringBuilder();
            for (Arista a = v.aristas; a != null; a = a.sig) {
                if (a.tipo == TipoRelacion.AMISTAD) {
                    aristasAmistad++;
                    if (amigos.length() > 0) amigos.append(", ");
                    amigos.append(a.destino.persona.nombre)
                          .append("(").append(a.destino.persona.usuario).append(")");
                } else if (a.tipo == TipoRelacion.SEGUIR) {
                    aristasSeguir++;
                    if (seguidos.length() > 0) seguidos.append(", ");
                    seguidos.append(a.destino.persona.nombre)
                            .append("(").append(a.destino.persona.usuario).append(")");
                }
            }
            sb.append("| Amigos: ").append(amigos.length() == 0 ? "Ninguno" : amigos);
            sb.append(" | Sigue a: ").append(seguidos.length() == 0 ? "Ninguno" : seguidos);
            sb.append("\n");
        }
        sb.append("Resumen -> Usuarios: ").append(totalUsuarios)
          .append(" | Amistades recíprocas: ").append(aristasAmistad / 2)
          .append(" (").append(aristasAmistad).append(" aristas internas)")
          .append(" | Seguimientos: ").append(aristasSeguir);
        return sb.toString();
    }
}
