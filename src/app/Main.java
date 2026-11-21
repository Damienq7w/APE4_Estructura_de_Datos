package app;

import negocio.RedSocial;

/**
 * Ejecución oficial de la secuencia de comprobación (Tabla 3) de
 * APE 4 · Fase 2: Conexión Ambato.
 *
 * Autor: Cunalata Mendoza Damian Alexander
 */
public class Main {

    public static void main(String[] args) {
        RedSocial red = new RedSocial();

        System.out.println("============================================================");
        System.out.println("  APE 4 - FASE 2: CONEXION AMBATO (MULTILISTA DINAMICA)");
        System.out.println("============================================================\n");

        // ---------------------------------------------------------------------
        // ANEXO 1: registrarPersona y mostrarRed (Paso 1 de Tabla 3)
        // ---------------------------------------------------------------------
        System.out.println(">>> PASO 1 [Anexo 1]: Registrar A01-A06 y las 5 amistades iniciales");
        System.out.println("registrarPersona(A01, Sofia):   " + red.registrarPersona("A01", "Sofía"));
        System.out.println("registrarPersona(A02, Mateo):   " + red.registrarPersona("A02", "Mateo"));
        System.out.println("registrarPersona(A03, Valeria): " + red.registrarPersona("A03", "Valeria"));
        System.out.println("registrarPersona(A04, Andres):  " + red.registrarPersona("A04", "Andrés"));
        System.out.println("registrarPersona(A05, Camila):  " + red.registrarPersona("A05", "Camila"));
        System.out.println("registrarPersona(A06, Diego):   " + red.registrarPersona("A06", "Diego"));

        System.out.println("\nAgregando 5 amistades reciprocas:");
        System.out.println("agregarAmistad(A01, A02): " + red.agregarAmistad("A01", "A02"));
        System.out.println("agregarAmistad(A02, A03): " + red.agregarAmistad("A02", "A03"));
        System.out.println("agregarAmistad(A03, A04): " + red.agregarAmistad("A03", "A04"));
        System.out.println("agregarAmistad(A04, A01): " + red.agregarAmistad("A04", "A01"));
        System.out.println("agregarAmistad(A04, A05): " + red.agregarAmistad("A04", "A05"));

        System.out.println("\nEstado de la red (mostrarRed):");
        System.out.println(red.mostrarRed());
        System.out.println("Grados (A01..A06): "
                + red.contarAmigos("A01") + ", "
                + red.contarAmigos("A02") + ", "
                + red.contarAmigos("A03") + ", "
                + red.contarAmigos("A04") + ", "
                + red.contarAmigos("A05") + ", "
                + red.contarAmigos("A06"));

        // ---------------------------------------------------------------------
        // ANEXO 2: Amistades y seguimientos (Pasos 2 y 3 de Tabla 3)
        // ---------------------------------------------------------------------
        System.out.println("\n>>> PASOS 2 y 3 [Anexo 2]: Seguimientos dirigidos");
        System.out.println("Paso 2 -> seguir(A02, A01): " + red.seguir("A02", "A01"));
        System.out.println("Paso 3 -> seguir(A06, A01): " + red.seguir("A06", "A01"));
        System.out.println("Seguidores de A01 (Sofia): " + red.listarSeguidores("A01"));
        System.out.println("Seguidos de A06 (Diego):   " + red.listarSeguidos("A06")
                + " | Amigos de A06: " + red.contarAmigos("A06"));

        // ---------------------------------------------------------------------
        // ANEXO 4: Consultas y conteo (Paso 4 de Tabla 3)
        // ---------------------------------------------------------------------
        System.out.println("\n>>> PASO 4 [Anexo 4]: listarAmigos, listarSeguidos, listarSeguidores y contarAmigos");
        System.out.println("listarAmigos(A04):     " + red.listarAmigos("A04"));
        System.out.println("listarSeguidos(A02):   " + red.listarSeguidos("A02"));
        System.out.println("listarSeguidores(A01): " + red.listarSeguidores("A01"));
        System.out.println("contarAmigos(A04):     " + red.contarAmigos("A04"));
        System.out.println("contarAmigos(A99):     " + red.contarAmigos("A99") + " (usuario inexistente)");
        System.out.println("listarAmigos(A99):     " + red.listarAmigos("A99") + " (usuario inexistente)");

        // ---------------------------------------------------------------------
        // ANEXO 3: eliminarAmistad y eliminarPersona (Pasos 5 y 6 de Tabla 3)
        // ---------------------------------------------------------------------
        System.out.println("\n>>> PASOS 5 y 6 [Anexo 3]: eliminarAmistad y eliminarPersona");
        System.out.println("Paso 5 -> eliminarAmistad(A04, A05): " + red.eliminarAmistad("A04", "A05"));
        System.out.println("  Amigos de A04 tras eliminar: " + red.listarAmigos("A04"));
        System.out.println("  Amigos de A05 tras eliminar: " + red.listarAmigos("A05"));

        System.out.println("Paso 6 -> eliminarPersona(A01): " + red.eliminarPersona("A01"));
        System.out.println("  Estado de la red tras eliminar a Sofia (A01):");
        System.out.println(red.mostrarRed());

        // ---------------------------------------------------------------------
        // PASOS 7 y 8: Validaciones de integridad
        // ---------------------------------------------------------------------
        System.out.println("\n>>> PASOS 7 y 8: Validaciones de integridad");
        System.out.println("Paso 7 -> registrarPersona(A02, Otro Nombre): "
                + red.registrarPersona("A02", "Otro Nombre") + " (esperado: false)");
        System.out.println("Paso 8 -> agregarAmistad(A03, A03):           "
                + red.agregarAmistad("A03", "A03") + " (esperado: false)");
    }
}
