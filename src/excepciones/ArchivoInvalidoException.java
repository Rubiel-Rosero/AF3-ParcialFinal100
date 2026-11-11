package excepciones;

/**
cuando el archivo csv no existe o tiene lineas mal escritas
*/
public class ArchivoInvalidoException extends SistemaAcademicoException
{
    public ArchivoInvalidoException(String mensaje)
    {
        super(mensaje);
    }
}
