package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Cola (FIFO: el primero en entrar es el primero en salir) implementada
 * manualmente con nodos enlazados.
 * <p>
 * Se usa para la cola de espera de materias llenas y para el
 * procesamiento por lotes de solicitudes desde CSV.
 * </p>
 * <p>
 * Guarda dos referencias: {@code frente} (por donde se sale) y
 * {@code fin} (por donde se entra). Asi encolar y desencolar son O(1).
 * </p>
 *
 * @param <T> tipo de los elementos
 */
public class Cola<T> implements Iterable<T> {

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    /** Crea una cola vacia. */
    public Cola() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    /**
     * Agrega un elemento al final de la cola (enqueue). O(1).
     *
     * @param dato elemento a encolar
     */
    public void encolar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (estaVacia()) {
            frente = nuevoNodo;
        } else {
            fin.setSiguiente(nuevoNodo);
        }
        fin = nuevoNodo;
        tamanio++;
    }

    /**
     * Quita y devuelve el elemento del frente (dequeue). O(1).
     *
     * @return el primer elemento que entro
     * @throws IllegalStateException si la cola esta vacia
     */
    public T desencolar() {
        verificarNoVacia();
        T dato = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null; // la cola quedo vacia: fin no puede apuntar a un nodo que ya salio
        }
        tamanio--;
        return dato;
    }

    /**
     * Devuelve el elemento del frente sin quitarlo. O(1).
     *
     * @return el primer elemento
     * @throws IllegalStateException si la cola esta vacia
     */
    public T verFrente() {
        verificarNoVacia();
        return frente.getDato();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamanio() {
        return tamanio;
    }

    private void verificarNoVacia() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola esta vacia");
        }
    }

    /**
     * Recorre la cola del frente al fin sin modificarla
     * (para "Mostrar cola de espera").
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = frente;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (actual == null) {
                    throw new NoSuchElementException();
                }
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                return dato;
            }
        };
    }

    /** Muestra la cola como [frente <- ... <- fin]. */
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder("[");
        Nodo<T> actual = frente;
        while (actual != null) {
            texto.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                texto.append(" <- ");
            }
            actual = actual.getSiguiente();
        }
        return texto.append("]").toString();
    }
}
