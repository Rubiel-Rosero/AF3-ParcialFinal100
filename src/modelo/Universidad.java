package modelo;

import excepciones.DatoInvalidoException;
import excepciones.EstudianteNoEncontradoException;
import java.util.HashMap;

/**
aqui va todo lo de la universidad
- facultades: arreglo nativo Facultad[5] (tamaño fijo, casi nunca cambian)
- estudiantes: HashMap con el id como clave, para buscar en O(1) sin recorrer
en los siguientes bloques aqui tambien van a ir materias y aulas
*/
public class Universidad
{
    public static final int MAX_FACULTADES = 5;
    private Facultad[] facultades = new Facultad[MAX_FACULTADES];
    private int cantidadFacultades = 0;
    private HashMap<String, Estudiante> estudiantes = new HashMap<>();

    // ===================== ESTUDIANTES =====================

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
    public Estudiante eliminarEstudiante(String id) throws EstudianteNoEncontradoException
    {
        Estudiante eliminado = estudiantes.remove(id.trim());
        if(eliminado == null)
        {
            throw new EstudianteNoEncontradoException("No existe estudiante con ID: " + id.trim());
        }
        return eliminado;
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
