package pruebas;

import excepciones.ArchivoInvalidoException;
import excepciones.ColaDeEsperaVaciaException;
import excepciones.CupoLlenoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.HorarioConflictivoException;
import excepciones.PilaDeshacerVaciaException;
import excepciones.PreRequisitoNoAprobadoException;
import excepciones.SistemaAcademicoException;

/**
 * Prueba del Bloque 0: verifica que cada excepcion personalizada
 * se puede lanzar y capturar, y que todas se capturan con el tipo base.
 */
public class PruebaBloque0 {

    private static int pruebasAprobadas = 0;
    private static int pruebasTotales = 0;

    public static void main(String[] args) {
        SistemaAcademicoException[] excepcionesDePrueba = {
            new PreRequisitoNoAprobadoException("Falta aprobar CALC101"),
            new HorarioConflictivoException("Lunes 9:00 ya esta reservado"),
            new CupoLlenoException("CALC101 no tiene cupos"),
            new EstudianteNoEncontradoException("No existe estudiante con ID: 2024999"),
            new ColaDeEsperaVaciaException("No hay estudiantes en espera"),
            new PilaDeshacerVaciaException("No hay operaciones para deshacer"),
            new ArchivoInvalidoException("Linea 3 sin coma")
        };

        for (SistemaAcademicoException excepcion : excepcionesDePrueba) {
            probarLanzarYCapturar(excepcion);
        }

        System.out.println();
        System.out.println("Resultado: " + pruebasAprobadas + "/" + pruebasTotales + " pruebas aprobadas");
    }

    /**
     * Lanza la excepcion recibida y verifica que el catch del tipo base la atrapa.
     */
    private static void probarLanzarYCapturar(SistemaAcademicoException excepcionEsperada) {
        pruebasTotales++;
        try {
            throw excepcionEsperada;
        } catch (SistemaAcademicoException capturada) {
            boolean esLaMisma = capturada == excepcionEsperada;
            if (esLaMisma) {
                pruebasAprobadas++;
            }
            System.out.println((esLaMisma ? "[OK]    " : "[FALLA] ") + "Error: " + capturada.getMensajeFormateado());
        }
    }
}
