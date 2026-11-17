package excepciones;

/**
cuando no hay operaciones para deshacer o rehacer
*/
public class PilaDeshacerVaciaException extends SistemaAcademicoException
{
    public PilaDeshacerVaciaException(String mensaje)
    {
        super(mensaje);
    }
}
