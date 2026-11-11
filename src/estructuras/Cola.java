package estructuras;

/**
cola hecha a mano con nodos (el primero que entra es el primero que sale)
se usa para la cola de espera de las materias llenas y para el batch del csv
se guarda frente (por donde salen) y fin (por donde entran)
*/
public class Cola<T>
{
    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;
    public Cola()
    {
        frente = null;
        fin = null;
        tamanio = 0;
    }
    //entrar por el fin
    public void encolar(T dato)
    {
        Nodo<T> nuevo = new Nodo<>(dato);
        if(estaVacia())
        {
            frente = nuevo;
        }
        else
        {
            fin.setSiguiente(nuevo);
        }
        fin = nuevo;
        tamanio++;
    }
    //salir por el frente
    public T desencolar()
    {
        if(estaVacia())
        {
            throw new IllegalStateException("La cola está vacía");
        }
        T dato = frente.getDato();
        frente = frente.getSiguiente();
        if(frente == null)
        {
            fin = null; //si quedo vacia, fin tampoco puede apuntar a nada
        }
        tamanio--;
        return dato;
    }
    //ver quien esta de primero sin sacarlo
    public T verFrente()
    {
        if(estaVacia())
        {
            throw new IllegalStateException("La cola está vacía");
        }
        return frente.getDato();
    }
    //para recorrerla desde afuera sin sacar a nadie (mostrar cola de espera)
    public Nodo<T> getPrimero()
    {
        return frente;
    }
    public boolean estaVacia()
    {
        return frente == null;
    }
    public int tamanio()
    {
        return tamanio;
    }
    @Override
    public String toString()
    {
        String texto = "[";
        Nodo<T> actual = frente;
        while(actual != null)
        {
            texto += actual.getDato();
            if(actual.getSiguiente() != null)
            {
                texto += " <- ";
            }
            actual = actual.getSiguiente();
        }
        return texto + "]";
    }
}
