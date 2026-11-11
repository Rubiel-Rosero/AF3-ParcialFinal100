package estructuras;

/**
cada pieza de la lista, la pila y la cola
guarda un dato y la referencia al siguiente nodo
si siguiente es null es porque es el ultimo
*/
public class Nodo<T>
{
    private T dato;
    private Nodo<T> siguiente;
    public Nodo(T dato)
    {
        this.dato = dato;
        this.siguiente = null;
    }
    public T getDato()
    {
        return dato;
    }
    public void setDato(T dato)
    {
        this.dato = dato;
    }
    public Nodo<T> getSiguiente()
    {
        return siguiente;
    }
    public void setSiguiente(Nodo<T> siguiente)
    {
        this.siguiente = siguiente;
    }
}
