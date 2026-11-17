package estructuras;

/**
pila hecha a mano con nodos (el ultimo que entra es el primero que sale)
se usa para deshacer/rehacer y para navegar entre reportes
solo se guarda el tope, por eso apilar y desapilar no recorren nada
*/
public class Pila<T>
{
    private Nodo<T> tope;
    private int tamanio;
    public Pila()
    {
        tope = null;
        tamanio = 0;
    }
    //poner encima
    public void apilar(T dato)
    {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(tope);
        tope = nuevo;
        tamanio++;
    }
    //sacar el de encima
    public T desapilar()
    {
        if(estaVacia())
        {
            throw new IllegalStateException("La pila está vacía");
        }
        T dato = tope.getDato();
        tope = tope.getSiguiente();
        tamanio--;
        return dato;
    }
    //ver el de encima sin sacarlo
    public T verTope()
    {
        if(estaVacia())
        {
            throw new IllegalStateException("La pila está vacía");
        }
        return tope.getDato();
    }
    //borrar todo (se usa para limpiar la pila de rehacer)
    public void vaciar()
    {
        tope = null;
        tamanio = 0;
    }
    public boolean estaVacia()
    {
        return tope == null;
    }
    public int tamanio()
    {
        return tamanio;
    }
    @Override
    public String toString()
    {
        String texto = "[";
        Nodo<T> actual = tope;
        while(actual != null)
        {
            texto += actual.getDato();
            if(actual.getSiguiente() != null)
            {
                texto += " | ";
            }
            actual = actual.getSiguiente();
        }
        return texto + "]";
    }
}
