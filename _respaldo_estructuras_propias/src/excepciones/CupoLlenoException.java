package excepciones;

/**
cuando la materia ya no tiene cupos
*/
public class CupoLlenoException extends SistemaAcademicoException
{
    public CupoLlenoException(String mensaje)
    {
        super(mensaje);
    }
}
