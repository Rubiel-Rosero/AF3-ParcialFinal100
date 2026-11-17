package pruebas;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
prueba del bloque 1: las estructuras de java.util que usa el sistema
LinkedList (inscritos, prerrequisitos, historial), Stack (ruta de Dijkstra) y Queue (cola de espera)
cada verificar compara lo que da el metodo con lo que deberia dar
*/
public class PruebaBloque1
{
    private static int aprobadas = 0;
    private static int total = 0;
    public static void main(String[] args)
    {
        System.out.println("=== LinkedList ===");
        probarLista();
        System.out.println("\n=== Stack ===");
        probarPila();
        System.out.println("\n=== Queue ===");
        probarCola();
        System.out.println("\nResultado: " + aprobadas + "/" + total);
    }
    private static void probarLista()
    {
        LinkedList<String> lista = new LinkedList<>();
        verificar("lista nueva vacia", lista.isEmpty(), true);
        lista.addLast("B");
        lista.addLast("C");
        lista.addFirst("A");
        lista.addLast("D");
        verificar("agregar al inicio y al final", lista.toString(), "[A, B, C, D]");
        verificar("size", lista.size(), 4);
        verificar("get posicion 2", lista.get(2), "C");
        verificar("contains C", lista.contains("C"), true);
        verificar("no contains Z", lista.contains("Z"), false);
        lista.remove("B");
        verificar("remove del medio", lista.toString(), "[A, C, D]");
        lista.remove("A");
        verificar("remove el primero", lista.toString(), "[C, D]");
        lista.remove("D");
        verificar("remove el ultimo", lista.toString(), "[C]");
        lista.addLast("E");
        verificar("addLast despues de borrar el ultimo", lista.toString(), "[C, E]");
        verificar("remove algo que no esta", lista.remove("Z"), false);
        String recorrido = "";
        for(String s : lista)
        {
            recorrido += s;
        }
        verificar("recorrer con for-each", recorrido, "CE");
        boolean fallo = false;
        try
        {
            lista.get(10);
        }
        catch(IndexOutOfBoundsException e)
        {
            fallo = true;
        }
        verificar("posicion invalida lanza excepcion", fallo, true);
    }
    //LIFO: el ultimo que entra es el primero que sale
    private static void probarPila()
    {
        Stack<String> pila = new Stack<>();
        pila.push("op1");
        pila.push("op2");
        pila.push("op3");
        verificar("peek es el ultimo", pila.peek(), "op3");
        verificar("size", pila.size(), 3);
        verificar("pop 1", pila.pop(), "op3");
        verificar("pop 2", pila.pop(), "op2");
        verificar("pop 3", pila.pop(), "op1");
        verificar("queda vacia", pila.isEmpty(), true);
        boolean fallo = false;
        try
        {
            pila.pop();
        }
        catch(EmptyStackException e)
        {
            fallo = true;
        }
        verificar("pop vacia lanza EmptyStackException", fallo, true);
        pila.push("x");
        pila.clear();
        verificar("clear", pila.isEmpty(), true);
    }
    //FIFO: el primero que llega es el primero que sale
    private static void probarCola()
    {
        Queue<String> cola = new LinkedList<>();
        cola.offer("Laura Diaz");
        cola.offer("Pablo Ortiz");
        cola.offer("Sofia Mora");
        verificar("peek es el primero que llego", cola.peek(), "Laura Diaz");
        String posiciones = "";
        int posicion = 1;
        for(String s : cola)
        {
            posiciones += posicion + ":" + s + " ";
            posicion++;
        }
        verificar("recorrer sin sacar", posiciones.trim(), "1:Laura Diaz 2:Pablo Ortiz 3:Sofia Mora");
        verificar("recorrer no cambia el size", cola.size(), 3);
        verificar("poll 1", cola.poll(), "Laura Diaz");
        verificar("poll 2", cola.poll(), "Pablo Ortiz");
        verificar("poll 3", cola.poll(), "Sofia Mora");
        verificar("queda vacia", cola.isEmpty(), true);
        cola.offer("Nuevo");
        verificar("offer despues de vaciarse", cola.peek(), "Nuevo");
        cola.poll();
        verificar("poll en cola vacia devuelve null", cola.poll() == null, true);
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
