package excepciones;

/**
 * Se lanza cuando no hay operaciones para deshacer o rehacer.
 */
public class PilaDeshacerVaciaException extends SistemaAcademicoException {

    /**
     * @param mensaje explicacion del error
     */
    public PilaDeshacerVaciaException(String mensaje) {
        super(mensaje);
    }
}
