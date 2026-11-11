package modelo;

import estructuras.ListaEnlazada;
import excepciones.DatoInvalidoException;
import java.util.Locale;

/**
estudiante, hereda de persona
las notas van en una matriz Double[10][20]: fila = semestre, columna = materia
al lado va otra matriz con el codigo de cada materia para saber de que es cada nota
se usa Double y no double para que una casilla vacia sea null (sin nota)
*/
public class Estudiante extends Persona
{
    public static final int MAX_SEMESTRES = 10;
    public static final int MAX_MATERIAS = 20;
    private int semestre;
    private Double[][] notas = new Double[MAX_SEMESTRES][MAX_MATERIAS];
    private String[][] codigosMaterias = new String[MAX_SEMESTRES][MAX_MATERIAS];
    private ListaEnlazada<String> historialMaterias = new ListaEnlazada<>();
    public Estudiante(String id, String nombre, String email, int semestre) throws DatoInvalidoException
    {
        super(id, nombre, email);
        setSemestre(semestre);
    }
    @Override
    public String getRol()
    {
        return "Estudiante";
    }
    //se usa lo del padre y se le agrega semestre y promedio
    @Override
    public String mostrarInformacion()
    {
        String promedio;
        if(tieneNotas())
        {
            promedio = formatear(calcularPromedio());
        }
        else
        {
            promedio = "0.0 (sin notas aún)";
        }
        return super.mostrarInformacion() + "\nSemestre: " + semestre + "\nPromedio acumulado: " + promedio;
    }
    //registrar o cambiar la nota de una materia en un semestre
    //devuelve la nota que habia antes (o null), eso sirve para poder deshacer despues
    public Double registrarNota(int semestre, String codigoMateria, double nota) throws DatoInvalidoException
    {
        validarSemestre(semestre);
        if(nota < 0 || nota > 5)
        {
            throw new DatoInvalidoException("La nota debe estar entre 0 y 5");
        }
        int fila = semestre - 1; //el semestre 1 es la fila 0
        int columna = buscarColumna(fila, codigoMateria);
        if(columna == -1)
        {
            columna = buscarColumnaLibre(fila);
            if(columna == -1)
            {
                throw new DatoInvalidoException("El semestre " + semestre + " ya tiene " + MAX_MATERIAS + " materias");
            }
            codigosMaterias[fila][columna] = codigoMateria;
            historialMaterias.agregarAlFinal(codigoMateria);
        }
        Double anterior = notas[fila][columna];
        notas[fila][columna] = nota;
        return anterior;
    }
    //quitar una nota (lo usa deshacer cuando la nota no existia antes)
    public void borrarNota(int semestre, String codigoMateria) throws DatoInvalidoException
    {
        validarSemestre(semestre);
        int fila = semestre - 1;
        int columna = buscarColumna(fila, codigoMateria);
        if(columna != -1)
        {
            notas[fila][columna] = null;
            codigosMaterias[fila][columna] = null;
            historialMaterias.eliminar(codigoMateria);
        }
    }
    //promedio de un semestre (sobrecarga: mismo nombre, recibe un parametro)
    public double calcularPromedio(int semestre) throws DatoInvalidoException
    {
        validarSemestre(semestre);
        int fila = semestre - 1;
        double suma = 0;
        int cantidad = 0;
        for(int j = 0; j < MAX_MATERIAS; j++)
        {
            if(notas[fila][j] != null)
            {
                suma += notas[fila][j];
                cantidad++;
            }
        }
        return cantidad == 0 ? 0 : suma / cantidad;
    }
    //promedio acumulado de toda la carrera (sobrecarga: sin parametros)
    public double calcularPromedio()
    {
        double suma = 0;
        int cantidad = 0;
        for(int i = 0; i < MAX_SEMESTRES; i++)
        {
            for(int j = 0; j < MAX_MATERIAS; j++)
            {
                if(notas[i][j] != null)
                {
                    suma += notas[i][j];
                    cantidad++;
                }
            }
        }
        return cantidad == 0 ? 0 : suma / cantidad;
    }
    //si en algun semestre saco 3.0 o mas en esa materia (lo usan los pre-requisitos)
    public boolean haAprobado(String codigoMateria)
    {
        for(int i = 0; i < MAX_SEMESTRES; i++)
        {
            for(int j = 0; j < MAX_MATERIAS; j++)
            {
                if(codigoMateria.equals(codigosMaterias[i][j]) && notas[i][j] != null && notas[i][j] >= 3.0)
                {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean tieneNotas()
    {
        for(int i = 0; i < MAX_SEMESTRES; i++)
        {
            for(int j = 0; j < MAX_MATERIAS; j++)
            {
                if(notas[i][j] != null)
                {
                    return true;
                }
            }
        }
        return false;
    }
    //buscar en que columna esta una materia dentro de un semestre, -1 si no esta
    private int buscarColumna(int fila, String codigoMateria)
    {
        for(int j = 0; j < MAX_MATERIAS; j++)
        {
            if(codigoMateria.equals(codigosMaterias[fila][j]))
            {
                return j;
            }
        }
        return -1;
    }
    //primera casilla vacia del semestre, -1 si ya esta lleno
    private int buscarColumnaLibre(int fila)
    {
        for(int j = 0; j < MAX_MATERIAS; j++)
        {
            if(codigosMaterias[fila][j] == null)
            {
                return j;
            }
        }
        return -1;
    }
    private void validarSemestre(int semestre) throws DatoInvalidoException
    {
        if(semestre < 1 || semestre > MAX_SEMESTRES)
        {
            throw new DatoInvalidoException("El semestre debe estar entre 1 y " + MAX_SEMESTRES);
        }
    }
    //dos decimales con punto (en español saldria con coma)
    public static String formatear(double numero)
    {
        return String.format(Locale.US, "%.2f", numero);
    }
    public int getSemestre()
    {
        return semestre;
    }
    public void setSemestre(int semestre) throws DatoInvalidoException
    {
        validarSemestre(semestre);
        this.semestre = semestre;
    }
    public Double getNota(int semestre, int columna)
    {
        return notas[semestre - 1][columna];
    }
    public String getCodigoMateria(int semestre, int columna)
    {
        return codigosMaterias[semestre - 1][columna];
    }
    public ListaEnlazada<String> getHistorialMaterias()
    {
        return historialMaterias;
    }
}
