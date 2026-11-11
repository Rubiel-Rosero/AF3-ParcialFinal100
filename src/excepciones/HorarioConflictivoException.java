package excepciones;

/**
 * Se lanza cuando se intenta reservar una hora de un aula que ya esta ocupada.
 */
public class HorarioConflictivoException extends SistemaAcademicoException {

    /**
     * @param mensaje explicacion del error
     */
    public HorarioConflictivoException(String mensaje) {
        super(mensaje);
    }
}
