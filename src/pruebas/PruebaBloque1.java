package pruebas;

import estructuras.Cola;
import estructuras.ListaEnlazada;
import estructuras.Nodo;
import estructuras.Pila;

/**
prueba del bloque 1: nodo, lista enlazada, pila y cola
cada verificar compara lo que da el metodo con lo que deberia dar
*/
public class PruebaBloque1
{
    private static int aprobadas = 0;
    private static int total = 0;
    public static void main(String[] args)
    {
        System.out.println("=== Nodo ===");
        probarNodo();
        System.out.println("\n=== Lista enlazada ===");
        probarLista();
        System.out.println("\n=== Pila ===");
        probarPila();
        System.out.println("\n=== Cola ===");
        probarCola();
        System.out.println("\nResultado: " + aprobadas + "/" + total);
    }
    //enlazar 3 nodos a mano y recorrerlos
    private static void probarNodo()
    {
        Nodo<String> n1 = new Nodo<>("CALC101");
        Nodo<String> n2 = new Nodo<>("FIS101");
        Nodo<String> n3 = new Nodo<>("PROG101");
        n1.setSiguiente(n2);
        n2.setSiguiente(n3);
        String recorrido = "";
        Nodo<String> actual = n1;
        while(actual != null)
        {
            recorrido += actual.getDato() + " ";
            actual = actual.getSiguiente();
        }
        verificar("recorrer 3 nodos", recorrido.trim(), "CALC101 FIS101 PROG101");
    }
    private static void probarLista()
    {
        ListaEnlazada<String> lista = new ListaEnlazada<>();
        verificar("lista nueva vacia", lista.estaVacia(), true);
        lista.agregarAlFinal("B");
        lista.agregarAlFinal("C");
        lista.agregarAlInicio("A");
        lista.agregarAlFinal("D");
        verificar("agregar al inicio y al final", lista.toString(), "[A -> B -> C -> D]");
        verificar("tamanio", lista.tamanio(), 4);
        verificar("obtener posicion 2", lista.obtener(2), "C");
        verificar("contiene C", lista.contiene("C"), true);
        verificar("no contiene Z", lista.contiene("Z"), false);
        //los 3 casos de eliminar
        lista.eliminar("B");
        verificar("eliminar del medio", lista.toString(), "[A -> C -> D]");
        lista.eliminar("A");
        verificar("eliminar el primero", lista.toString(), "[C -> D]");
        lista.eliminar("D");
        verificar("eliminar el ultimo", lista.toString(), "[C]");
        lista.agregarAlFinal("E");
        verificar("agregar despues de borrar el ultimo", lista.toString(), "[C -> E]");
        verificar("eliminar algo que no esta", lista.eliminar("Z"), false);
        //recorrer desde afuera
        String recorrido = "";
        Nodo<String> actual = lista.getPrimero();
        while(actual != null)
        {
            recorrido += actual.getDato();
            actual = actual.getSiguiente();
        }
        verificar("recorrer con getPrimero", recorrido, "CE");
        boolean fallo = false;
        try
        {
            lista.obtener(10);
        }
        catch(IndexOutOfBoundsException e)
        {
            fallo = true;
        }
        verificar("posicion invalida lanza excepcion", fallo, true);
    }
    private static void probarPila()
    {
        Pila<String> pila = new Pila<>();
        pila.apilar("op1");
        pila.apilar("op2");
        pila.apilar("op3");
        verificar("tope es el ultimo", pila.verTope(), "op3");
        verificar("tamanio", pila.tamanio(), 3);
        verificar("desapilar 1", pila.desapilar(), "op3");
        verificar("desapilar 2", pila.desapilar(), "op2");
        verificar("desapilar 3", pila.desapilar(), "op1");
        verificar("queda vacia", pila.estaVacia(), true);
        boolean fallo = false;
        try
        {
            pila.desapilar();
        }
        catch(IllegalStateException e)
        {
            fallo = true;
        }
        verificar("desapilar vacia lanza excepcion", fallo, true);
        pila.apilar("x");
        pila.vaciar();
        verificar("vaciar", pila.estaVacia(), true);
    }
    private static void probarCola()
    {
        Cola<String> cola = new Cola<>();
        cola.encolar("Laura Diaz");
        cola.encolar("Pablo Ortiz");
        cola.encolar("Sofia Mora");
        verificar("frente es el primero que llego", cola.verFrente(), "Laura Diaz");
        //mostrar la cola sin sacar a nadie
        String posiciones = "";
        int posicion = 1;
        Nodo<String> actual = cola.getPrimero();
        while(actual != null)
        {
            posiciones += posicion + ":" + actual.getDato() + " ";
            posicion++;
            actual = actual.getSiguiente();
        }
        verificar("recorrer sin sacar", posiciones.trim(), "1:Laura Diaz 2:Pablo Ortiz 3:Sofia Mora");
        verificar("recorrer no cambia el tamanio", cola.tamanio(), 3);
        verificar("desencolar 1", cola.desencolar(), "Laura Diaz");
        verificar("desencolar 2", cola.desencolar(), "Pablo Ortiz");
        verificar("desencolar 3", cola.desencolar(), "Sofia Mora");
        verificar("queda vacia", cola.estaVacia(), true);
        cola.encolar("Nuevo");
        verificar("encolar despues de vaciarse", cola.verFrente(), "Nuevo");
        cola.desencolar();
        boolean fallo = false;
        try
        {
            cola.desencolar();
        }
        catch(IllegalStateException e)
        {
            fallo = true;
        }
        verificar("desencolar vacia lanza excepcion", fallo, true);
    }
    private static void verificar(String que, Object obtenido, Object esperado)
    {
        total++;
        if(esperado.equals(obtenido))
        {
            aprobadas++;
            System.out.println("[OK] " + que);
        }
        else
        {
            System.out.println("[FALLA] " + que + " -> esperaba " + esperado + " y dio " + obtenido);
        }
    }
}
