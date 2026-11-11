package excepciones;

/**
cuando se busca un id de estudiante que no existe
*/
public class EstudianteNoEncontradoException extends SistemaAcademicoException
{
    public EstudianteNoEncontradoException(String mensaje)
    {
        super(mensaje);
    }
}
