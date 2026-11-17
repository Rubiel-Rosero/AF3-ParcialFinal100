package modelo;

import excepciones.DatoInvalidoException;

/*
parcial, vale 25% normalmente
*/
public class Parcial extends Evaluacion
{
    private double porcentaje;
    public Parcial(String descripcion, double nota) throws DatoInvalidoException
    {
        super(descripcion, nota);
        this.porcentaje = 25;
    }
    public Parcial(String descripcion, double nota, double porcentaje) throws DatoInvalidoException
    {
        super(descripcion, nota);
        if(porcentaje <= 0 || porcentaje > 100)
        {
            throw new DatoInvalidoException("El porcentaje debe estar entre 1 y 100");
        }
        this.porcentaje = porcentaje;
    }
    @Override
    public double getPorcentaje()
    {
        return porcentaje;
    }
    @Override
    public String getTipo()
    {
        return "Parcial";
    }
}
