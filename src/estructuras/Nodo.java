package estructuras;

/**
 * Nodo generico: la pieza basica de la lista enlazada, la pila y la cola.
 * <p>
 * Guarda un dato y una referencia (flecha) al siguiente nodo.
 * Si {@code siguiente} es {@code null}, este es el ultimo nodo de la cadena.
 * </p>
 *
 * @param <T> tipo del dato que guarda el nodo (Estudiante, Materia, String...)
 */
public class Nodo<T> {

    private T dato;
    private Nodo<T> siguiente;

    /**
     * Crea un nodo suelto, todavia sin siguiente.
     *
     * @param dato valor que guardara el nodo
     */
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}
