package modelo;

import excepciones.CupoLlenoException;
import excepciones.DatoInvalidoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import java.util.HashMap;
import java.util.TreeMap;

/*
aqui va todo lo de la universidad
*/
public class Universidad
{
    public static final int MAX_FACULTADES = 5;
    private Facultad[] facultades = new Facultad[MAX_FACULTADES];
    private int cantidadFacultades = 0;
    private HashMap<String, Estudiante> estudiantes = new HashMap<>();
    private HashMap<String, Materia> materias = new HashMap<>();
    //TreeMap las aulas se listan ordenadas por nombre sin tener que ordenar
    private TreeMap<String, Aula> aulas = new TreeMap<>();
    //el mapa del campus edificios y caminos es decir la matriz de adyacencia y dijkstra)
    private Campus campus = new Campus();
    // estudiantes
    //registrar el id no se puede repetir porque es la clave del mapa
    public void registrarEstudiante(Estudiante estudiante) throws DatoInvalidoException
    {
        if(estudiantes.get(estudiante.getId()) != null)
        {
            throw new DatoInvalidoException("Ya existe un estudiante con ID: " + estudiante.getId());
        }
        estudiantes.put(estudiante.getId(), estudiante);
    }
    //buscar por id get va directo a la cubeta, si no esta devuelve null
    public Estudiante buscarEstudiante(String id) throws EstudianteNoEncontradoException
    {
        Estudiante encontrado = estudiantes.get(id.trim());
        if(encontrado == null)
        {
            throw new EstudianteNoEncontradoException("No existe estudiante con ID: " + id.trim());
        }
        return encontrado;
    }
    //baja definitiva remove devuelve el estudiante que saco
    public Estudiante eliminarEstudiante(String id) throws EstudianteNoEncontradoException, DatoInvalidoException
    {
        Estudiante estudiante = buscarEstudiante(id);
        for(String codigo : materias.keySet())
        {
            Materia m = materias.get(codigo);
            if(m.estaInscrito(estudiante) || m.estaEnEspera(estudiante))
            {
                throw new DatoInvalidoException(estudiante.getNombre() + " sigue en " + m.getCodigo() + ". Cancele primero la inscripción");
            }
        }
        return estudiantes.remove(estudiante.getId());
    }
    //deshacer vuelve a meter un estudiante eliminado, con sus notas y todo
    public void restaurarEstudiante(Estudiante estudiante)
    {
        estudiantes.put(estudiante.getId(), estudiante);
    }
    //se pasan del mapa a un arreglo nativo
    public Estudiante[] listarEstudiantes()
    {
        Estudiante[] lista = new Estudiante[estudiantes.size()];
        int i = 0;
        for(String id : estudiantes.keySet())
        {
            lista[i] = estudiantes.get(id);
            i++;
        }
        return lista;
    }
    public int getCantidadEstudiantes()
    {
        return estudiantes.size();
    }
    public boolean existeEstudiante(String id)
    {
        return estudiantes.get(id.trim()) != null;
    }
    // Materias
    public void crearMateria(Materia materia) throws DatoInvalidoException
    {
        if(materias.get(materia.getCodigo()) != null)
        {
            throw new DatoInvalidoException("Ya existe la materia " + materia.getCodigo());
        }
        materias.put(materia.getCodigo(), materia);
    }
    public Materia buscarMateria(String codigo) throws DatoInvalidoException
    {
        Materia encontrada = materias.get(codigo.trim().toUpperCase());
        if(encontrada == null)
        {
            throw new DatoInvalidoException("No existe la materia " + codigo.trim().toUpperCase());
        }
        return encontrada;
    }
    public void agregarPrerrequisito(String codigoMateria, String codigoPrerrequisito) throws DatoInvalidoException
    {
        Materia materia = buscarMateria(codigoMateria);
        Materia prerrequisito = buscarMateria(codigoPrerrequisito);
        materia.agregarPrerrequisito(prerrequisito);
    }
    //devuelve true si quedo inscrito, false si quedo en la cola de espera
    //aqui se atrapa la hija para tratarla distinto no es un error como tal, es ir a la cola
    public boolean inscribirEstudiante(String id, String codigoMateria) throws EstudianteNoEncontradoException, DatoInvalidoException, PreRequisitoNoAprobadoException
    {
        Estudiante estudiante = buscarEstudiante(id);
        Materia materia = buscarMateria(codigoMateria);
        try
        {
            materia.inscribir(estudiante);
            return true;
        }
        catch(CupoLlenoException e)
        {
            materia.agregarAColaEspera(estudiante);
            return false;
        }
    }
    //devuelve a quien se le asigno el cupo liberado (el primero de la cola) o null
    public Estudiante cancelarInscripcion(String id, String codigoMateria) throws EstudianteNoEncontradoException, DatoInvalidoException
    {
        Estudiante estudiante = buscarEstudiante(id);
        Materia materia = buscarMateria(codigoMateria);
        return materia.cancelarInscripcion(estudiante);
    }
    //igual que con estudiantes, del mapa a un arreglo
    public Materia[] listarMaterias()
    {
        Materia[] lista = new Materia[materias.size()];
        int i = 0;
        for(String codigo : materias.keySet())
        {
            lista[i] = materias.get(codigo);
            i++;
        }
        return lista;
    }
    //en que materias esta inscrito un estudiante (recorre la lista de inscritos de cada una)
    public String materiasDeEstudiante(String id) throws EstudianteNoEncontradoException
    {
        Estudiante estudiante = buscarEstudiante(id);
        String texto = "";
        for(Materia m : listarMaterias())
        {
            if(m.getInscritos().contains(estudiante))
            {
                texto += m + "\n";
            }
        }
        if(texto.isEmpty())
        {
            return estudiante.getNombre() + " no tiene materias inscritas";
        }
        return texto.trim();
    }
    public int getCantidadMaterias()
    {
        return materias.size();
    }
    //aulas y horarios
    public void crearAula(Aula aula) throws DatoInvalidoException
    {
        String clave = aula.getNombre().toUpperCase();
        if(aulas.get(clave) != null)
        {
            throw new DatoInvalidoException("Ya existe el aula " + aula.getNombre());
        }
        aulas.put(clave, aula);
    }
    //el TreeMap baja por el arbol comparando nombres
    public Aula buscarAula(String nombre) throws DatoInvalidoException
    {
        Aula encontrada = aulas.get(nombre.trim().toUpperCase());
        if(encontrada == null)
        {
            throw new DatoInvalidoException("No existe el aula " + nombre.trim());
        }
        return encontrada;
    }
    //TreeMap ya sale ordenado por la clave nombre, no hay que ordenar
    public Aula[] listarAulas()
    {
        Aula[] lista = new Aula[aulas.size()];
        int i = 0;
        for(String clave : aulas.keySet())
        {
            lista[i] = aulas.get(clave);
            i++;
        }
        return lista;
    }
    //copia a un arreglo y se ordena por capacidad mayor a menor con insercion
    public Aula[] listarAulasPorCapacidad()
    {
        Aula[] lista = listarAulas();
        for(int j = 1; j < lista.length; j++)
        {
            Aula actual = lista[j];
            int k = j - 1;
            while(k >= 0 && lista[k].getCapacidad() < actual.getCapacidad())
            {
                lista[k + 1] = lista[k];
                k--;
            }
            lista[k + 1] = actual;
        }
        return lista;
    }
    //si viene codigo de materia, la materia tiene que existir
    public void reservarHorario(String nombreAula, int dia, int hora, int duracion, String codigoMateria) throws DatoInvalidoException, HorarioConflictivoException
    {
        Aula aula = buscarAula(nombreAula);
        String codigo = "-";
        if(codigoMateria != null && !codigoMateria.trim().isEmpty())
        {
            codigo = buscarMateria(codigoMateria).getCodigo();
        }
        aula.reservar(dia, hora, duracion, codigo);
    }
    public void liberarHorario(String nombreAula, int dia, int hora, int duracion) throws DatoInvalidoException
    {
        buscarAula(nombreAula).liberar(dia, hora, duracion);
    }
    public void cambiarHorario(String nombreAula, int dia, int hora, int duracion, int nuevoDia, int nuevaHora) throws DatoInvalidoException, HorarioConflictivoException
    {
        buscarAula(nombreAula).cambiarHorario(dia, hora, duracion, nuevoDia, nuevaHora);
    }
    // rutas entre edificios (lo hace Campus)
    public Campus getCampus()
    {
        return campus;
    }
    //Facultades
    //agregar en la siguiente casilla libre
    public void agregarFacultad(Facultad facultad) throws DatoInvalidoException
    {
        if(cantidadFacultades == MAX_FACULTADES)
        {
            throw new DatoInvalidoException("Ya hay " + MAX_FACULTADES + " facultades, el arreglo está lleno");
        }
        if(buscarFacultad(facultad.getCodigo()) != null)
        {
            throw new DatoInvalidoException("Ya existe la facultad " + facultad.getCodigo());
        }
        facultades[cantidadFacultades] = facultad;
        cantidadFacultades++;
    }
    //buscar recorriendo el arreglo, null si no esta
    public Facultad buscarFacultad(String codigo)
    {
        for(int i = 0; i < cantidadFacultades; i++)
        {
            if(facultades[i].getCodigo().equalsIgnoreCase(codigo))
            {
                return facultades[i];
            }
        }
        return null;
    }
    //las 5 facultades con las que arranca el sistema
    public void cargarFacultades() throws DatoInvalidoException
    {
        agregarFacultad(new Facultad("ING", "Ingeniería"));
        agregarFacultad(new Facultad("CIE", "Ciencias Básicas"));
        agregarFacultad(new Facultad("ECO", "Ciencias Económicas"));
        agregarFacultad(new Facultad("SAL", "Ciencias de la Salud"));
        agregarFacultad(new Facultad("HUM", "Humanidades"));
    }
    public String listarFacultades()
    {
        String texto = "";
        for(int i = 0; i < cantidadFacultades; i++)
        {
            texto += (i + 1) + ". " + facultades[i] + "\n";
        }
        return texto;
    }
    public int getCantidadFacultades()
    {
        return cantidadFacultades;
    }
}
