package excepciones;

/*
excepcion padre de todas las del sistema
asi en el menu se pueden atrapar todas con un solo catch
*/
public class SistemaAcademicoException extends Exception
{
    private String nombre;
    //cada hija le manda su nombre para mostrarlo en el error
    public SistemaAcademicoException(String nombre, String mensaje)
    {
        super(mensaje);
        this.nombre = nombre;
    }
    //devuelve el error como sale en el enunciado: NombreExcepcion - mensaje
    public String getMensajeFormateado()
    {
        return nombre + " - " + getMessage();
    }
}
