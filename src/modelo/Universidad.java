package modelo;

import estructuras.Nodo;
import excepciones.CupoLlenoException;
import excepciones.DatoInvalidoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PreRequisitoNoAprobadoException;
import java.util.HashMap;
import java.util.TreeMap;

/**
aqui va todo lo de la universidad
- facultades: arreglo nativo Facultad[5] (tamaño fijo, casi nunca cambian)
- estudiantes: HashMap con el id como clave, para buscar en O(1) sin recorrer
- materias: HashMap con el codigo como clave
- aulas: TreeMap con el nombre como clave, para listarlas ordenadas
*/
public class Universidad
{
    public static final int MAX_FACULTADES = 5;
    private Facultad[] facultades = new Facultad[MAX_FACULTADES];
    private int cantidadFacultades = 0;
    private HashMap<String, Estudiante> estudiantes = new HashMap<>();
    private HashMap<String, Materia> materias = new HashMap<>();
    //TreeMap y no HashMap: las aulas se listan ordenadas por nombre sin tener que ordenar
    //la clave es el nombre en mayusculas para que "aula 101" y "Aula 101" sean la misma
    private TreeMap<String, Aula> aulas = new TreeMap<>();
    //el mapa del campus: edificios y caminos (matriz de adyacencia + dijkstra)
    private Campus campus = new Campus();

    // estudiantes

    //registrar: el id no se puede repetir porque es la clave del mapa
    public void registrarEstudiante(Estudiante estudiante) throws DatoInvalidoException
    {
        if(estudiantes.containsKey(estudiante.getId()))
        {
            throw new DatoInvalidoException("Ya existe un estudiante con ID: " + estudiante.getId());
        }
        estudiantes.put(estudiante.getId(), estudiante);
    }
    //buscar por id: get va directo a la cubeta, si no esta devuelve null
    public Estudiante buscarEstudiante(String id) throws EstudianteNoEncontradoException
    {
        Estudiante encontrado = estudiantes.get(id.trim());
        if(encontrado == null)
        {
            throw new EstudianteNoEncontradoException("No existe estudiante con ID: " + id.trim());
        }
        return encontrado;
    }
    //baja definitiva: remove devuelve el estudiante que saco
    //se devuelve para que deshacer (bloque 8) lo pueda volver a meter
    //no se deja eliminar a alguien inscrito o en espera: primero hay que cancelar,
    //si no quedaria un estudiante "fantasma" ocupando un cupo
    public Estudiante eliminarEstudiante(String id) throws EstudianteNoEncontradoException, DatoInvalidoException
    {
        Estudiante estudiante = buscarEstudiante(id);
        for(Materia m : materias.values())
        {
            if(m.estaInscrito(estudiante) || m.estaEnEspera(estudiante))
            {
                throw new DatoInvalidoException(estudiante.getNombre() + " sigue en " + m.getCodigo() + ". Cancele primero la inscripción");
            }
        }
        return estudiantes.remove(estudiante.getId());
    }
    //lo usa deshacer: vuelve a meter un estudiante eliminado, con sus notas y todo
    public void restaurarEstudiante(Estudiante estudiante)
    {
        estudiantes.put(estudiante.getId(), estudiante);
    }
    //el hashmap no guarda orden, entonces se pasan a un arreglo nativo
    //y se ordenan por id con insercion
    public Estudiante[] listarEstudiantes()
    {
        Estudiante[] lista = new Estudiante[estudiantes.size()];
        int i = 0;
        for(Estudiante e : estudiantes.values())
        {
            lista[i] = e;
            i++;
        }
        //ordenamiento por insercion: cada uno se corre a la izquierda hasta su lugar
        for(int j = 1; j < lista.length; j++)
        {
            Estudiante actual = lista[j];
            int k = j - 1;
            while(k >= 0 && lista[k].getId().compareTo(actual.getId()) > 0)
            {
                lista[k + 1] = lista[k];
                k--;
            }
            lista[k + 1] = actual;
        }
        return lista;
    }
    public int getCantidadEstudiantes()
    {
        return estudiantes.size();
    }
    public boolean existeEstudiante(String id)
    {
        return estudiantes.containsKey(id.trim());
    }

    // Materias

    public void crearMateria(Materia materia) throws DatoInvalidoException
    {
        if(materias.containsKey(materia.getCodigo()))
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
    //aqui se atrapa la hija (CupoLleno) para tratarla distinto: no es error, es ir a la cola
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
    //igual que con estudiantes: del mapa a un arreglo y ordenado por codigo con insercion
    public Materia[] listarMaterias()
    {
        Materia[] lista = new Materia[materias.size()];
        int i = 0;
        for(Materia m : materias.values())
        {
            lista[i] = m;
            i++;
        }
        for(int j = 1; j < lista.length; j++)
        {
            Materia actual = lista[j];
            int k = j - 1;
            while(k >= 0 && lista[k].getCodigo().compareTo(actual.getCodigo()) > 0)
            {
                lista[k + 1] = lista[k];
                k--;
            }
            lista[k + 1] = actual;
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
            Nodo<Estudiante> actual = m.getInscritos().getPrimero();
            while(actual != null)
            {
                if(actual.getDato() == estudiante)
                {
                    texto += m + "\n";
                }
                actual = actual.getSiguiente();
            }
        }
        return texto.isEmpty() ? estudiante.getNombre() + " no tiene materias inscritas" : texto.trim();
    }
    public int getCantidadMaterias()
    {
        return materias.size();
    }

    // aulas y horarios

    public void crearAula(Aula aula) throws DatoInvalidoException
    {
        String clave = aula.getNombre().toUpperCase();
        if(aulas.containsKey(clave))
        {
            throw new DatoInvalidoException("Ya existe el aula " + aula.getNombre());
        }
        aulas.put(clave, aula);
    }
    //el TreeMap baja por el arbol comparando nombres: O(log n)
    public Aula buscarAula(String nombre) throws DatoInvalidoException
    {
        Aula encontrada = aulas.get(nombre.trim().toUpperCase());
        if(encontrada == null)
        {
            throw new DatoInvalidoException("No existe el aula " + nombre.trim());
        }
        return encontrada;
    }
    //values() de un TreeMap ya sale ordenado por la clave (nombre), no hay que ordenar
    public Aula[] listarAulas()
    {
        Aula[] lista = new Aula[aulas.size()];
        int i = 0;
        for(Aula a : aulas.values())
        {
            lista[i] = a;
            i++;
        }
        return lista;
    }
    //el enunciado tambien pide por capacidad: el TreeMap esta ordenado por nombre,
    //entonces se copia a un arreglo y se ordena por capacidad (mayor a menor) con insercion
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
    public int getCantidadAulas()
    {
        return aulas.size();
    }

    // rutas entre edificios (lo hace Campus)

    public Campus getCampus()
    {
        return campus;
    }

    // ===================== FACULTADES =====================

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
