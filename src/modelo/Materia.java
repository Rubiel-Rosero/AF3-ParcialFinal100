package modelo;

import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.DatoInvalidoException;
import excepciones.PreRequisitoNoAprobadoException;
import java.util.LinkedList;// la clase Queue es una interfaz y no se puede instanciar directamente, por eso se usa LinkedList
import java.util.Queue;
/*
una materia con sus cupos, creditos, prerrequisitos, inscritos y cola de espera
*/
public class Materia
{
    private String codigo;
    private String nombre;
    private int cuposMaximos;
    private int creditos;
    private LinkedList<Materia> prerrequisitos = new LinkedList<>();
    private LinkedList<Estudiante> inscritos = new LinkedList<>();
    private Queue<Estudiante> colaEspera = new LinkedList<>();
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
        if(prerrequisitos.contains(prerrequisito))
        {
            throw new DatoInvalidoException(prerrequisito.getCodigo() + " ya es prerrequisito de " + codigo);
        }
        prerrequisitos.addLast(prerrequisito);
    }
    //recorre la lista y junta las que el estudiante no ha aprobado
    public void verificarPrerrequisitos(Estudiante estudiante) throws PreRequisitoNoAprobadoException
    {
        String faltantes = "";
        for(int i = 0; i < prerrequisitos.size(); i++)
        {
            String codigoPre = prerrequisitos.get(i).getCodigo();
            if(!estudiante.haAprobado(codigoPre))
            {
                if(!faltantes.isEmpty())
                {
                    faltantes += ", ";
                }
                faltantes += codigoPre;
            }
        }
        if(!faltantes.isEmpty())
        {
            throw new PreRequisitoNoAprobadoException(estudiante.getNombre() + " no ha aprobado: " + faltantes);
        }
    }
    public String mostrarPrerrequisitos()
    {
        if(prerrequisitos.isEmpty())
        {
            return codigo + " no tiene prerrequisitos";
        }
        String texto = "Prerrequisitos de " + codigo + ":";
        for(int i = 0; i < prerrequisitos.size(); i++)
        {
            texto += "\n- " + prerrequisitos.get(i);
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
        inscritos.addLast(estudiante);
    }
    //se llama cuando inscribir lanzo CupoLlenoException
    public void agregarAColaEspera(Estudiante estudiante)
    {
        colaEspera.offer(estudiante);
    }
    //saca al estudiante y si hay alguien esperando le da el cupo automaticamente
    //devuelve a quien se le asigno el cupo, o null si la cola estaba vacia
    public Estudiante cancelarInscripcion(Estudiante estudiante) throws DatoInvalidoException
    {
        if(!inscritos.remove(estudiante))
        {
            throw new DatoInvalidoException(estudiante.getNombre() + " no está inscrito en " + codigo);
        }
        if(colaEspera.isEmpty())
        {
            return null;
        }
        Estudiante siguiente = colaEspera.poll(); // Remueve el primero de la cola
        inscritos.addLast(siguiente);
        return siguiente;
    }
    public boolean estaInscrito(Estudiante estudiante)
    {
        return inscritos.contains(estudiante);
    }
    //busca en la cola sin sacar a nadie
    public boolean estaEnEspera(Estudiante estudiante)
    {
        return colaEspera.contains(estudiante);
    }
    //como en el ejemplo 2: Posicion 1: Laura Diaz 
    public String mostrarColaEspera() throws ColaDeEsperaVaciaException
    {
        if(colaEspera.isEmpty())
        {
            throw new ColaDeEsperaVaciaException("No hay estudiantes en espera para " + codigo);
        }
        String texto = "Materia: " + this;
        int posicion = 1;
        for(Estudiante estudiante : colaEspera)
        {
            texto += "\nPosición " + posicion + ": " + estudiante.getNombre();
            posicion++;
        }
        return texto + "\nTotal en espera: " + colaEspera.size();
    }
    public int getCuposDisponibles()
    {
        return cuposMaximos - inscritos.size();
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
    public LinkedList<Materia> getPrerrequisitos()
    {
        return prerrequisitos;
    }
    public LinkedList<Estudiante> getInscritos()
    {
        return inscritos;
    }
    public Queue<Estudiante> getColaEspera()
    {
        return colaEspera;
    }
    @Override
    public String toString()
    {
        return codigo + " - " + nombre;
    }
}
