package pruebas;

import negocio.RedSocial;

/**
 * Pruebas automatizadas sin dependencias externas (sin JUnit)
 * para verificar todas las operaciones y casos borde de APE 4 · Fase 2.
 *
 * Autor: Cunalata Mendoza Damian Alexander
 */
public class PruebasRedSocial {

    private static int total = 0;
    private static int ok = 0;

    private static void check(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            ok++;
        }
        System.out.println((condicion ? "OK    " : "FALLA ") + nombre);
    }

    private static RedSocial redInicial() {
        RedSocial red = new RedSocial();
        red.registrarPersona("A01", "Sofía");
        red.registrarPersona("A02", "Mateo");
        red.registrarPersona("A03", "Valeria");
        red.registrarPersona("A04", "Andrés");
        red.registrarPersona("A05", "Camila");
        red.registrarPersona("A06", "Diego");

        red.agregarAmistad("A01", "A02");
        red.agregarAmistad("A02", "A03");
        red.agregarAmistad("A03", "A04");
        red.agregarAmistad("A04", "A01");
        red.agregarAmistad("A04", "A05");
        return red;
    }

    public static void main(String[] args) {
        System.out.println("=== PRUEBAS AUTOMATIZADAS: CONEXION AMBATO (APE 4 - FASE 2) ===\n");

        // 1. Registro y validaciones de usuario
        RedSocial r1 = new RedSocial();
        check("Registrar usuario valido A01", r1.registrarPersona("A01", "Sofía"));
        check("Rechazar duplicado con distinta mayuscula/minuscula (a01)", !r1.registrarPersona("a01", "Otra Sofía"));
        check("Rechazar usuario null", !r1.registrarPersona(null, "Nombre"));
        check("Rechazar usuario vacio", !r1.registrarPersona("", "Nombre"));
        check("Rechazar usuario con espacios", !r1.registrarPersona("A 02", "Mateo"));
        check("Rechazar usuario con comas", !r1.registrarPersona("A02,B", "Mateo"));
        check("Rechazar nombre vacio", !r1.registrarPersona("A02", "   "));

        // 2. Estado inicial Fase 1 (Paso 1 de Tabla 3)
        RedSocial red = redInicial();
        check("Paso 1: grados iniciales 2,2,2,3,1,0",
                red.contarAmigos("A01") == 2 &&
                red.contarAmigos("A02") == 2 &&
                red.contarAmigos("A03") == 2 &&
                red.contarAmigos("A04") == 3 &&
                red.contarAmigos("A05") == 1 &&
                red.contarAmigos("A06") == 0);
        check("Normalizacion en consultas (a04 == A04)", red.contarAmigos("a04") == 3);
        check("contarAmigos usuario inexistente retorna -1", red.contarAmigos("Z99") == -1);
        check("listarAmigos usuario inexistente retorna null", red.listarAmigos("Z99") == null);
        check("listarSeguidos usuario inexistente retorna null", red.listarSeguidos("Z99") == null);
        check("listarSeguidores usuario inexistente retorna null", red.listarSeguidores("Z99") == null);

        // 3. Pasos 2 y 3: seguimientos dirigidos
        check("Paso 2: seguir(A02, A01) retorna true", red.seguir("A02", "A01"));
        check("Paso 2: seguir duplicado(A02, A01) retorna false", !red.seguir("A02", "A01"));
        check("Paso 2: seguir es dirigido (A01 no sigue a A02)", "Ninguno".equals(red.listarSeguidos("A01")));
        check("Paso 3: seguir(A06, A01) retorna true", red.seguir("A06", "A01"));
        String seguidoresA01 = red.listarSeguidores("A01");
        check("Paso 2 y 3: seguidores de A01 incluye Mateo y Diego",
                seguidoresA01 != null && seguidoresA01.contains("Mateo") && seguidoresA01.contains("Diego"));

        // 4. Paso 4: listarAmigos(A04)
        String amigosA04 = red.listarAmigos("A04");
        check("Paso 4: listarAmigos(A04) incluye Valeria, Sofia y Camila",
                amigosA04 != null && amigosA04.contains("Valeria")
                        && amigosA04.contains("Sofía") && amigosA04.contains("Camila"));

        // 5. dejarDeSeguir
        check("dejarDeSeguir(A06, A01) retorna true", red.dejarDeSeguir("A06", "A01"));
        check("dejarDeSeguir inexistente retorna false", !red.dejarDeSeguir("A06", "A01"));
        check("Volver a seguir(A06, A01) para probar limpieza en Paso 6", red.seguir("A06", "A01"));

        // 6. Paso 5: eliminarAmistad(A04, A05)
        check("Paso 5: eliminarAmistad(A04, A05) retorna true", red.eliminarAmistad("A04", "A05"));
        check("Paso 5: eliminarAmistad ya eliminada retorna false", !red.eliminarAmistad("A04", "A05"));
        check("Paso 5: A04 queda con 2 amigos y A05 con 0",
                red.contarAmigos("A04") == 2 && red.contarAmigos("A05") == 0);

        // 7. Paso 6: eliminarPersona(A01) y limpieza total de referencias entrantes
        check("Paso 6: eliminarPersona(A01) retorna true", red.eliminarPersona("A01"));
        check("Paso 6: A01 ya no existe (contarAmigos == -1)", red.contarAmigos("A01") == -1);
        check("Paso 6: A02 perdio amistad y seguimiento hacia A01",
                red.contarAmigos("A02") == 1 && "Ninguno".equals(red.listarSeguidos("A02")));
        check("Paso 6: A04 perdio amistad con A01 (queda con 1 amigo)", red.contarAmigos("A04") == 1);
        check("Paso 6: A06 perdio seguimiento hacia A01", "Ninguno".equals(red.listarSeguidos("A06")));

        // 8. Pasos 7 y 8: duplicado y lazo
        check("Paso 7: registrarPersona(A02, otro nombre) retorna false",
                !red.registrarPersona("A02", "Otro Nombre"));
        check("Paso 8: agregarAmistad(A03, A03) retorna false (lazo)", !red.agregarAmistad("A03", "A03"));
        check("seguir(A03, A03) retorna false (lazo)", !red.seguir("A03", "A03"));
        check("agregarAmistad duplicada(A02, A03) retorna false", !red.agregarAmistad("A02", "A03"));

        System.out.println("\n============================================================");
        System.out.println("RESULTADO FINAL: " + ok + "/" + total + " pruebas superadas.");
        System.out.println("============================================================");
        if (ok != total) {
            System.exit(1);
        }
    }
}
