package estructuras;

/**
lista enlazada hecha a mano
se usa para los pre-requisitos de las materias y el historial del estudiante
se guarda el primero y el ultimo para que agregar al final no tenga que recorrer toda la lista
*/
public class ListaEnlazada<T>
{
    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int tamanio;
    public ListaEnlazada()
    {
        primero = null;
        ultimo = null;
        tamanio = 0;
    }
    //agregar al final
    public void agregarAlFinal(T dato)
    {
        Nodo<T> nuevo = new Nodo<>(dato);
        if(estaVacia())
        {
            primero = nuevo;
        }
        else
        {
            ultimo.setSiguiente(nuevo);
        }
        ultimo = nuevo;
        tamanio++;
    }
    //agregar al inicio
    public void agregarAlInicio(T dato)
    {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(primero);
        primero = nuevo;
        if(ultimo == null)
        {
            ultimo = nuevo;
        }
        tamanio++;
    }
    //obtener el dato de una posicion, empieza en 0
    public T obtener(int posicion)
    {
        if(posicion < 0 || posicion >= tamanio)
        {
            throw new IndexOutOfBoundsException("Posición inválida: " + posicion);
        }
        Nodo<T> actual = primero;
        for(int i = 0; i < posicion; i++)
        {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }
    //buscar si un dato esta en la lista
    public boolean contiene(T dato)
    {
        Nodo<T> actual = primero;
        while(actual != null)
        {
            if(actual.getDato().equals(dato))
            {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
    //eliminar un dato
    //el nodo anterior pasa a apuntar al siguiente del que se borra, asi se lo salta
    public boolean eliminar(T dato)
    {
        Nodo<T> anterior = null;
        Nodo<T> actual = primero;
        while(actual != null && !actual.getDato().equals(dato))
        {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if(actual == null)
        {
            return false; //no estaba
        }
        if(anterior == null)
        {
            primero = actual.getSiguiente(); //era el primero
        }
        else
        {
            anterior.setSiguiente(actual.getSiguiente());
        }
        if(actual == ultimo)
        {
            ultimo = anterior; //era el ultimo
        }
        tamanio--;
        return true;
    }
    //borrar todo
    public void vaciar()
    {
        primero = null;
        ultimo = null;
        tamanio = 0;
    }
    //para recorrerla desde afuera con un while
    public Nodo<T> getPrimero()
    {
        return primero;
    }
    public int tamanio()
    {
        return tamanio;
    }
    public boolean estaVacia()
    {
        return tamanio == 0;
    }
    @Override
    public String toString()
    {
        String texto = "[";
        Nodo<T> actual = primero;
        while(actual != null)
        {
            texto += actual.getDato();
            if(actual.getSiguiente() != null)
            {
                texto += " -> ";
            }
            actual = actual.getSiguiente();
        }
        return texto + "]";
    }
}
