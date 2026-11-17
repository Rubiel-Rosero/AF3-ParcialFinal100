package excepciones;

/**
cuando el estudiante no ha aprobado algun pre-requisito de la materia
*/
public class PreRequisitoNoAprobadoException extends SistemaAcademicoException
{
    public PreRequisitoNoAprobadoException(String mensaje)
    {
        super(mensaje);
    }
}
