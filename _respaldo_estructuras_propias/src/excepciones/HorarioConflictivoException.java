package excepciones;

/**
cuando se quiere reservar una hora del aula que ya esta ocupada
*/
public class HorarioConflictivoException extends SistemaAcademicoException
{
    public HorarioConflictivoException(String mensaje)
    {
        super(mensaje);
    }
}
