package modelo;

import excepciones.DatoInvalidoException;

/**
clase padre de quiz, parcial y examen final
cada hijo dice cuanto vale (porcentaje) y que tipo es
con un arreglo Evaluacion[] se calcula la nota definitiva de una materia
*/
public abstract class Evaluacion
{
    private String descripcion;
    private double nota;
    public Evaluacion(String descripcion, double nota) throws DatoInvalidoException
    {
        if(nota < 0 || nota > 5)
        {
            throw new DatoInvalidoException("La nota debe estar entre 0 y 5");
        }
        this.descripcion = descripcion;
        this.nota = nota;
    }
    //cada hijo decide cuanto pesa
    public abstract double getPorcentaje();
    public abstract String getTipo();
    //lo que esta evaluacion le suma a la definitiva
    public double calcularAporte()
    {
        return nota * getPorcentaje() / 100;
    }
    //recorre el arreglo y suma lo que aporta cada una
    //no importa si es quiz, parcial o final: cada una usa su propio getPorcentaje (polimorfismo)
    public static double calcularDefinitiva(Evaluacion[] evaluaciones) throws DatoInvalidoException
    {
        double sumaPorcentajes = 0;
        double definitiva = 0;
        for(int i = 0; i < evaluaciones.length; i++)
        {
            sumaPorcentajes += evaluaciones[i].getPorcentaje();
            definitiva += evaluaciones[i].calcularAporte();
        }
        if(Math.abs(sumaPorcentajes - 100) > 0.001)
        {
            throw new DatoInvalidoException("Los porcentajes suman " + sumaPorcentajes + "% y deben sumar 100%");
        }
        return definitiva;
    }
    public String getDescripcion()
    {
        return descripcion;
    }
    public double getNota()
    {
        return nota;
    }
    @Override
    public String toString()
    {
        return getTipo() + " - " + descripcion + ": " + nota + " (" + getPorcentaje() + "%)";
    }
}
