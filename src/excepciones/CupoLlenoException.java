package excepciones;

/**
 * Se lanza cuando una materia no tiene cupos disponibles.
 */
public class CupoLlenoException extends SistemaAcademicoException {

    /**
     * @param mensaje explicacion del error
     */
    public CupoLlenoException(String mensaje) {
        super(mensaje);
    }
}
