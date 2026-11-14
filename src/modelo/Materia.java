package modelo;

import estructuras.Cola;
import estructuras.ListaEnlazada;
import estructuras.Nodo;
import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.DatoInvalidoException;
import excepciones.PreRequisitoNoAprobadoException;

/**
una materia con sus cupos
- prerrequisitos: lista enlazada (cada materia tiene una cantidad distinta y puede crecer)
- inscritos: lista enlazada de los estudiantes que ya tienen cupo
- colaEspera: cola, cuando la materia esta llena el primero que llega es el primero que entra
*/
public class Materia
{
    private String codigo;
    private String nombre;
    private int cuposMaximos;
    private int creditos;
    private ListaEnlazada<Materia> prerrequisitos = new ListaEnlazada<>();
    private ListaEnlazada<Estudiante> inscritos = new ListaEnlazada<>();
    private Cola<Estudiante> colaEspera = new Cola<>();
    public Materia(String codigo, String nombre, int cuposMaximos, int creditos) throws DatoInvalidoException
    {
        if(codigo == null || codigo.trim().isEmpty())
        {
            throw new DatoInvalidoException("El código de la materia no puede estar vacío");
        }
        if(nombre == null || nombre.trim().isEmpty())
        {
            throw new DatoInvalidoException("El nombre de la materia no puede estar vacío");
        }
        if(cuposMaximos <= 0)
        {
            throw new DatoInvalidoException("Los cupos deben ser mayores a 0");
        }
        if(creditos <= 0)
        {
            throw new DatoInvalidoException("Los créditos deben ser mayores a 0");
        }
        this.codigo = codigo.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.cuposMaximos = cuposMaximos;
        this.creditos = creditos;
    }

    // prerequisitos: agregar, verificar, mostrar

    //agregar al final de la lista, sin repetir y sin ponerse a si misma
    public void agregarPrerrequisito(Materia prerrequisito) throws DatoInvalidoException
    {
        if(prerrequisito.getCodigo().equals(codigo))
        {
            throw new DatoInvalidoException("Una materia no puede ser prerrequisito de sí misma");
        }
        if(prerrequisitos.contiene(prerrequisito))
        {
            throw new DatoInvalidoException(prerrequisito.getCodigo() + " ya es prerrequisito de " + codigo);
        }
        prerrequisitos.agregarAlFinal(prerrequisito);
    }
    //recorre la lista y junta las que el estudiante no ha aprobado
    public void verificarPrerrequisitos(Estudiante estudiante) throws PreRequisitoNoAprobadoException
    {
        String faltantes = "";
        Nodo<Materia> actual = prerrequisitos.getPrimero();
        while(actual != null)
        {
            String codigoPre = actual.getDato().getCodigo();
            if(!estudiante.haAprobado(codigoPre))
            {
                faltantes += (faltantes.isEmpty() ? "" : ", ") + codigoPre;
            }
            actual = actual.getSiguiente();
        }
        if(!faltantes.isEmpty())
        {
            throw new PreRequisitoNoAprobadoException(estudiante.getNombre() + " no ha aprobado: " + faltantes);
        }
    }
    public String mostrarPrerrequisitos()
    {
        if(prerrequisitos.estaVacia())
        {
            return codigo + " no tiene prerrequisitos";
        }
        String texto = "Prerrequisitos de " + codigo + ":";
        Nodo<Materia> actual = prerrequisitos.getPrimero();
        while(actual != null)
        {
            texto += "\n- " + actual.getDato();
            actual = actual.getSiguiente();
        }
        return texto;
    }

    //inscritos y cola de espera

    //orden de las validaciones: repetido -> prerrequisitos -> cupo
    //asi a la cola de espera solo llega quien si podria ver la materia
    public void inscribir(Estudiante estudiante) throws DatoInvalidoException, PreRequisitoNoAprobadoException, CupoLlenoException
    {
        if(estaInscrito(estudiante))
        {
            throw new DatoInvalidoException(estudiante.getNombre() + " ya está inscrito en " + codigo);
        }
        if(estaEnEspera(estudiante))
        {
            throw new DatoInvalidoException(estudiante.getNombre() + " ya está en la cola de espera de " + codigo);
        }
        verificarPrerrequisitos(estudiante);
        if(getCuposDisponibles() == 0)
        {
            throw new CupoLlenoException(codigo + " no tiene cupos disponibles");
        }
        inscritos.agregarAlFinal(estudiante);
    }
    //se llama cuando inscribir lanzo CupoLlenoException
    public void agregarAColaEspera(Estudiante estudiante)
    {
        colaEspera.encolar(estudiante);
    }
    //saca al estudiante y si hay alguien esperando le da el cupo automaticamente
    //devuelve a quien se le asigno el cupo, o null si la cola estaba vacia
    public Estudiante cancelarInscripcion(Estudiante estudiante) throws DatoInvalidoException
    {
        if(!inscritos.eliminar(estudiante))
        {
            throw new DatoInvalidoException(estudiante.getNombre() + " no está inscrito en " + codigo);
        }
        if(colaEspera.estaVacia())
        {
            return null;
        }
        Estudiante siguiente = colaEspera.desencolar();
        inscritos.agregarAlFinal(siguiente);
        return siguiente;
    }
    public boolean estaInscrito(Estudiante estudiante)
    {
        return inscritos.contiene(estudiante);
    }
    //la cola no tiene contiene(), entonces se recorre desde el frente sin sacar a nadie
    public boolean estaEnEspera(Estudiante estudiante)
    {
        Nodo<Estudiante> actual = colaEspera.getPrimero();
        while(actual != null)
        {
            if(actual.getDato() == estudiante)
            {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }
    //como en el ejemplo 2: Posicion 1: Laura Diaz ...
    public String mostrarColaEspera() throws ColaDeEsperaVaciaException
    {
        if(colaEspera.estaVacia())
        {
            throw new ColaDeEsperaVaciaException("No hay estudiantes en espera para " + codigo);
        }
        String texto = "Materia: " + this;
        int posicion = 1;
        Nodo<Estudiante> actual = colaEspera.getPrimero();
        while(actual != null)
        {
            texto += "\nPosición " + posicion + ": " + actual.getDato().getNombre();
            posicion++;
            actual = actual.getSiguiente();
        }
        return texto + "\nTotal en espera: " + colaEspera.tamanio();
    }
    public int getCuposDisponibles()
    {
        return cuposMaximos - inscritos.tamanio();
    }
    public String getCodigo()
    {
        return codigo;
    }
    public String getNombre()
    {
        return nombre;
    }
    public int getCuposMaximos()
    {
        return cuposMaximos;
    }
    public int getCreditos()
    {
        return creditos;
    }
    public ListaEnlazada<Materia> getPrerrequisitos()
    {
        return prerrequisitos;
    }
    public ListaEnlazada<Estudiante> getInscritos()
    {
        return inscritos;
    }
    public Cola<Estudiante> getColaEspera()
    {
        return colaEspera;
    }
    @Override
    public String toString()
    {
        return codigo + " - " + nombre;
    }
}
