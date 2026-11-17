package excepciones;

/**
cuando se quiere sacar a alguien de la cola de espera y no hay nadie
*/
public class ColaDeEsperaVaciaException extends SistemaAcademicoException
{
    public ColaDeEsperaVaciaException(String mensaje)
    {
        super(mensaje);
    }
}
