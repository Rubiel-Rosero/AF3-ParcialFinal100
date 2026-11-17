package modelo;

import excepciones.DatoInvalidoException;

/**
examen final, vale 30%
*/
public class ExamenFinal extends Evaluacion
{
    public ExamenFinal(String descripcion, double nota) throws DatoInvalidoException
    {
        super(descripcion, nota);
    }
    @Override
    public double getPorcentaje()
    {
        return 30;
    }
    @Override
    public String getTipo()
    {
        return "Examen final";
    }
}
