package pruebas;

import estructuras.Cola;
import estructuras.ListaEnlazada;
import estructuras.Nodo;
import estructuras.Pila;

/**
 * Prueba del Bloque 1: estructuras de datos propias
 * (Nodo, ListaEnlazada, Pila, Cola).
 */
public class PruebaBloque1 {

    private static int pruebasAprobadas = 0;
    private static int pruebasTotales = 0;

    public static void main(String[] args) {
        System.out.println("=== P1.1 Nodo ===");
        probarNodo();

        System.out.println("\n=== P1.2 ListaEnlazada ===");
        probarListaEnlazada();

        System.out.println("\n=== P1.3 Pila (LIFO) ===");
        probarPila();

        System.out.println("\n=== P1.4 Cola (FIFO) ===");
        probarCola();

        System.out.println();
        System.out.println("Resultado: " + pruebasAprobadas + "/" + pruebasTotales + " pruebas aprobadas");
    }

    private static void probarNodo() {
        Nodo<String> primero = new Nodo<>("CALC101");
        Nodo<String> segundo = new Nodo<>("FIS101");
        Nodo<String> tercero = new Nodo<>("PROG101");
        primero.setSiguiente(segundo);
        segundo.setSiguiente(tercero);

        StringBuilder recorrido = new StringBuilder();
        Nodo<String> actual = primero;
        while (actual != null) {
            recorrido.append(actual.getDato()).append(" ");
            actual = actual.getSiguiente();
        }
        verificar("Recorrer 3 nodos enlazados", recorrido.toString().trim(), "CALC101 FIS101 PROG101");
    }

    private static void probarListaEnlazada() {
        ListaEnlazada<String> lista = new ListaEnlazada<>();
        verificar("Lista nueva esta vacia", lista.estaVacia(), true);

        lista.agregarAlFinal("B");
        lista.agregarAlFinal("C");
        lista.agregarAlInicio("A");
        lista.agregarAlFinal("D");
        verificar("Agregar inicio/final", lista.toString(), "[A -> B -> C -> D]");
        verificar("Tamanio", lista.tamanio(), 4);
        verificar("Obtener indice 2", lista.obtener(2), "C");
        verificar("Contiene C", lista.contiene("C"), true);
        verificar("No contiene Z", lista.contiene("Z"), false);

        verificar("Eliminar del medio (B)", lista.eliminar("B"), true);
        verificar("  lista sin B", lista.toString(), "[A -> C -> D]");

        verificar("Eliminar la cabeza (A)", lista.eliminar("A"), true);
        verificar("  lista sin A", lista.toString(), "[C -> D]");

        verificar("Eliminar el ultimo (D)", lista.eliminar("D"), true);
        verificar("  lista sin D", lista.toString(), "[C]");

        lista.agregarAlFinal("E");
        verificar("Agregar al final tras borrar el ultimo", lista.toString(), "[C -> E]");

        verificar("Eliminar inexistente devuelve false", lista.eliminar("Z"), false);
        verificar("eliminarEn(0)", lista.eliminarEn(0), "C");
        verificar("eliminarEn(0) deja un solo nodo", lista.toString(), "[E]");

        StringBuilder recorridoForEach = new StringBuilder();
        lista.agregarAlFinal("F");
        for (String elemento : lista) {
            recorridoForEach.append(elemento);
        }
        verificar("Recorrer con for-each", recorridoForEach.toString(), "EF");

        boolean lanzoExcepcion = false;
        try {
            lista.obtener(10);
        } catch (IndexOutOfBoundsException error) {
            lanzoExcepcion = true;
        }
        verificar("Indice invalido lanza excepcion", lanzoExcepcion, true);
    }

    private static void probarPila() {
        Pila<String> pila = new Pila<>();
        pila.apilar("op1");
        pila.apilar("op2");
        pila.apilar("op3");
        verificar("Tope es el ultimo apilado", pila.verTope(), "op3");
        verificar("Tamanio", pila.tamanio(), 3);
        verificar("Desapilar 1 -> op3", pila.desapilar(), "op3");
        verificar("Desapilar 2 -> op2", pila.desapilar(), "op2");
        verificar("Desapilar 3 -> op1", pila.desapilar(), "op1");
        verificar("Pila queda vacia", pila.estaVacia(), true);

        boolean lanzoExcepcion = false;
        try {
            pila.desapilar();
        } catch (IllegalStateException error) {
            lanzoExcepcion = true;
        }
        verificar("Desapilar pila vacia lanza excepcion", lanzoExcepcion, true);

        pila.apilar("x");
        pila.vaciar();
        verificar("vaciar() deja la pila vacia", pila.estaVacia(), true);
    }

    private static void probarCola() {
        Cola<String> cola = new Cola<>();
        cola.encolar("Laura Diaz");
        cola.encolar("Pablo Ortiz");
        cola.encolar("Sofia Mora");
        verificar("Frente es el primero en llegar", cola.verFrente(), "Laura Diaz");

        StringBuilder posiciones = new StringBuilder();
        int posicion = 1;
        for (String estudiante : cola) {
            posiciones.append(posicion++).append(":").append(estudiante).append(" ");
        }
        verificar("Recorrer sin modificar", posiciones.toString().trim(),
            "1:Laura Diaz 2:Pablo Ortiz 3:Sofia Mora");
        verificar("Recorrer no cambia el tamanio", cola.tamanio(), 3);

        verificar("Desencolar 1 -> Laura", cola.desencolar(), "Laura Diaz");
        verificar("Desencolar 2 -> Pablo", cola.desencolar(), "Pablo Ortiz");
        verificar("Desencolar 3 -> Sofia", cola.desencolar(), "Sofia Mora");
        verificar("Cola queda vacia", cola.estaVacia(), true);

        cola.encolar("Nuevo");
        verificar("Encolar tras vaciarse funciona", cola.verFrente(), "Nuevo");
        cola.desencolar();

        boolean lanzoExcepcion = false;
        try {
            cola.desencolar();
        } catch (IllegalStateException error) {
            lanzoExcepcion = true;
        }
        verificar("Desencolar cola vacia lanza excepcion", lanzoExcepcion, true);
    }

    private static void verificar(String descripcion, Object obtenido, Object esperado) {
        pruebasTotales++;
        boolean aprobada = esperado.equals(obtenido);
        if (aprobada) {
            pruebasAprobadas++;
            System.out.println("[OK]    " + descripcion);
        } else {
            System.out.println("[FALLA] " + descripcion + " -> esperado: " + esperado + ", obtenido: " + obtenido);
        }
    }
}
