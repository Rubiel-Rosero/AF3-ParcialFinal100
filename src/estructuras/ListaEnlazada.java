package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista enlazada simple implementada manualmente.
 * <p>
 * Se usa para los pre-requisitos de cada materia y para el historial
 * de materias cursadas de cada estudiante.
 * </p>
 * <p>
 * Guarda referencia al primer nodo ({@code cabeza}) y al ultimo
 * ({@code ultimo}) para que agregar al final sea O(1) y no haya que
 * recorrer toda la lista.
 * </p>
 * <p>
 * Implementa {@link Iterable} para poder recorrerla con
 * {@code for (T dato : lista)}. Iterable es solo una interfaz de Java:
 * el almacenamiento y el recorrido son nuestros.
 * </p>
 *
 * @param <T> tipo de los elementos
 */
public class ListaEnlazada<T> implements Iterable<T> {

    private Nodo<T> cabeza;
    private Nodo<T> ultimo;
    private int tamanio;

    /** Crea una lista vacia. */
    public ListaEnlazada() {
        cabeza = null;
        ultimo = null;
        tamanio = 0;
    }

    /**
     * Agrega un elemento al final de la lista. Complejidad O(1).
     *
     * @param dato elemento a agregar
     */
    public void agregarAlFinal(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (estaVacia()) {
            cabeza = nuevoNodo;
        } else {
            ultimo.setSiguiente(nuevoNodo);
        }
        ultimo = nuevoNodo;
        tamanio++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Complejidad O(1).
     *
     * @param dato elemento a agregar
     */
    public void agregarAlInicio(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.setSiguiente(cabeza);
        cabeza = nuevoNodo;
        if (ultimo == null) {
            ultimo = nuevoNodo;
        }
        tamanio++;
    }

    /**
     * Devuelve el elemento en la posicion indicada. Complejidad O(n).
     *
     * @param indice posicion empezando en 0
     * @return el elemento en esa posicion
     * @throws IndexOutOfBoundsException si el indice no existe
     */
    public T obtener(int indice) {
        validarIndice(indice);
        Nodo<T> actual = cabeza;
        for (int posicion = 0; posicion < indice; posicion++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    /**
     * Indica si la lista contiene un elemento (compara con equals). O(n).
     *
     * @param dato elemento buscado
     * @return true si esta en la lista
     */
    public boolean contiene(T dato) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (sonIguales(actual.getDato(), dato)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Elimina la primera aparicion del elemento. O(n).
     * <p>
     * Idea clave: para "sacar" un nodo, el nodo anterior pasa a apuntar
     * al siguiente del eliminado. El eliminado queda sin referencias y el
     * recolector de basura de Java lo libera.
     * </p>
     *
     * @param dato elemento a eliminar
     * @return true si se encontro y elimino
     */
    public boolean eliminar(T dato) {
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;

        while (actual != null && !sonIguales(actual.getDato(), dato)) {
            anterior = actual;
            actual = actual.getSiguiente();
        }

        if (actual == null) {
            return false;
        }

        desenlazar(anterior, actual);
        return true;
    }

    /**
     * Elimina el elemento en la posicion indicada. O(n).
     *
     * @param indice posicion empezando en 0
     * @return el elemento eliminado
     * @throws IndexOutOfBoundsException si el indice no existe
     */
    public T eliminarEn(int indice) {
        validarIndice(indice);
        Nodo<T> anterior = null;
        Nodo<T> actual = cabeza;
        for (int posicion = 0; posicion < indice; posicion++) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        desenlazar(anterior, actual);
        return actual.getDato();
    }

    /** Elimina todos los elementos. */
    public void vaciar() {
        cabeza = null;
        ultimo = null;
        tamanio = 0;
    }

    public int tamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    /**
     * Saca {@code actual} de la cadena. Cubre los tres casos:
     * eliminar la cabeza, uno del medio o el ultimo.
     */
    private void desenlazar(Nodo<T> anterior, Nodo<T> actual) {
        if (anterior == null) {
            cabeza = actual.getSiguiente();          // era la cabeza
        } else {
            anterior.setSiguiente(actual.getSiguiente()); // "saltar" el nodo
        }
        if (actual == ultimo) {
            ultimo = anterior;                        // era el ultimo
        }
        tamanio--;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException(
                "Indice " + indice + " fuera de rango (tamanio " + tamanio + ")");
        }
    }

    /** Compara dos datos permitiendo valores null. */
    private boolean sonIguales(T primero, T segundo) {
        return primero == null ? segundo == null : primero.equals(segundo);
    }

    /**
     * Permite recorrer la lista con for-each, avanzando nodo por nodo.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

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

    /** Muestra la lista como [a -> b -> c]. */
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            texto.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                texto.append(" -> ");
            }
            actual = actual.getSiguiente();
        }
        return texto.append("]").toString();
    }
}
