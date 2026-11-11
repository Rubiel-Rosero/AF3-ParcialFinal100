package excepciones;

/**
 * Excepcion base de todo el sistema academico.
 * <p>
 * Todas las excepciones personalizadas heredan de esta clase. Asi el menu
 * principal puede capturarlas todas con un solo bloque catch, sin perder
 * el tipo especifico de cada error (polimorfismo aplicado a excepciones).
 * </p>
 */
public class SistemaAcademicoException extends Exception {

    /**
     * Crea la excepcion con un mensaje descriptivo para el usuario.
     *
     * @param mensaje explicacion del error
     */
    public SistemaAcademicoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Devuelve el error con el formato que pide el enunciado:
     * "NombreDeLaExcepcion - mensaje".
     *
     * @return texto listo para mostrar en consola
     */
    public String getMensajeFormateado() {
        return getClass().getSimpleName() + " - " + getMessage();
    }
}
