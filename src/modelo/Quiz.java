package modelo;

import excepciones.DatoInvalidoException;

/**
quiz corto, vale 10%
*/
public class Quiz extends Evaluacion
{
    public Quiz(String descripcion, double nota) throws DatoInvalidoException
    {
        super(descripcion, nota);
    }
    @Override
    public double getPorcentaje()
    {
        return 10;
    }
    @Override
    public String getTipo()
    {
        return "Quiz";
    }
}
