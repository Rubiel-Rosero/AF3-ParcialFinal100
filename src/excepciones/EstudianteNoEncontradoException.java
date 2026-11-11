package excepciones;

/**
 * Se lanza cuando se busca un ID de estudiante que no existe en el sistema.
 */
public class EstudianteNoEncontradoException extends SistemaAcademicoException {

    /**
     * @param mensaje explicacion del error
     */
    public EstudianteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
