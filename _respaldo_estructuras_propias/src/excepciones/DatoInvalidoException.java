package excepciones;

/**
cuando el usuario escribe un dato que no sirve
(email sin @, semestre fuera de rango, nota mayor a 5, etc)
*/
public class DatoInvalidoException extends SistemaAcademicoException
{
    public DatoInvalidoException(String mensaje)
    {
        super(mensaje);
    }
}
