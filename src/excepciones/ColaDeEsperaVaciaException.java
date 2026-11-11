package excepciones;

/**
 * Se lanza cuando se intenta sacar un estudiante de una cola de espera sin elementos.
 */
public class ColaDeEsperaVaciaException extends SistemaAcademicoException {

    /**
     * @param mensaje explicacion del error
     */
    public ColaDeEsperaVaciaException(String mensaje) {
        super(mensaje);
    }
}
