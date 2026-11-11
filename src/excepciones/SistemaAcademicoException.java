package excepciones;

/**
excepcion padre de todas las del sistema
asi en el menu se pueden atrapar todas con un solo catch
*/
public class SistemaAcademicoException extends Exception
{
    public SistemaAcademicoException(String mensaje)
    {
        super(mensaje);
    }
    //devuelve el error como sale en el enunciado: NombreExcepcion - mensaje
    public String getMensajeFormateado()
    {
        return getClass().getSimpleName() + " - " + getMessage();
    }
}
