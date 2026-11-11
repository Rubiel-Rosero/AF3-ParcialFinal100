package modelo;

import excepciones.DatoInvalidoException;

/**
aqui va la lista fija de facultades: un arreglo nativo Facultad[5]
es de tamaño fijo porque la universidad no crea facultades todos los dias
en los siguientes bloques aqui tambien van a ir estudiantes, materias y aulas
*/
public class Universidad
{
    public static final int MAX_FACULTADES = 5;
    private Facultad[] facultades = new Facultad[MAX_FACULTADES];
    private int cantidadFacultades = 0;
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
