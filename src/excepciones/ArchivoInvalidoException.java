package excepciones;

/**
 * Se lanza cuando el archivo CSV no existe, no se puede leer o tiene lineas mal formadas.
 */
public class ArchivoInvalidoException extends SistemaAcademicoException {

    /**
     * @param mensaje explicacion del error
     */
    public ArchivoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
