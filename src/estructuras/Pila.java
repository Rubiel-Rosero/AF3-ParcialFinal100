package estructuras;

/**
 * Pila (LIFO: el ultimo en entrar es el primero en salir) implementada
 * manualmente con nodos enlazados.
 * <p>
 * Se usa para deshacer/rehacer operaciones y para navegar entre reportes.
 * </p>
 * <p>
 * Solo se necesita una referencia al {@code tope}: apilar pone un nodo
 * nuevo encima y desapilar quita el de encima. Ambas son O(1) porque
 * nunca se recorre la pila.
 * </p>
 *
 * @param <T> tipo de los elementos
 */
public class Pila<T> {

    private Nodo<T> tope;
    private int tamanio;

    /** Crea una pila vacia. */
    public Pila() {
        tope = null;
        tamanio = 0;
    }

    /**
     * Pone un elemento encima de la pila (push). O(1).
     *
     * @param dato elemento a apilar
     */
    public void apilar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        nuevoNodo.setSiguiente(tope);
        tope = nuevoNodo;
        tamanio++;
    }

    /**
     * Quita y devuelve el elemento de encima (pop). O(1).
     *
     * @return el elemento que estaba en el tope
     * @throws IllegalStateException si la pila esta vacia
     */
    public T desapilar() {
        verificarNoVacia();
        T dato = tope.getDato();
        tope = tope.getSiguiente();
        tamanio--;
        return dato;
    }

    /**
     * Devuelve el elemento de encima sin quitarlo (peek). O(1).
     *
     * @return el elemento en el tope
     * @throws IllegalStateException si la pila esta vacia
     */
    public T verTope() {
        verificarNoVacia();
        return tope.getDato();
    }

    /** Elimina todos los elementos (se usa para limpiar la pila de rehacer). */
    public void vaciar() {
        tope = null;
        tamanio = 0;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int tamanio() {
        return tamanio;
    }

    private void verificarNoVacia() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila esta vacia");
        }
    }

    /** Muestra la pila desde el tope: [tope | ... | fondo]. */
    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder("[");
        Nodo<T> actual = tope;
        while (actual != null) {
            texto.append(actual.getDato());
            if (actual.getSiguiente() != null) {
                texto.append(" | ");
            }
            actual = actual.getSiguiente();
        }
        return texto.append("]").toString();
    }
}
