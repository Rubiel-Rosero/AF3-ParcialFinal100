package excepciones;

/**
 * Se lanza cuando un estudiante intenta inscribir una materia sin haber aprobado todos sus pre-requisitos.
 */
public class PreRequisitoNoAprobadoException extends SistemaAcademicoException {

    /**
     * @param mensaje explicacion del error
     */
    public PreRequisitoNoAprobadoException(String mensaje) {
        super(mensaje);
    }
}
