package pruebas;

import excepciones.*;

/**
prueba del bloque 0
se lanza cada excepcion y se mira que el catch de la clase padre la atrape
*/
public class PruebaBloque0
{
    public static void main(String[] args)
    {
        SistemaAcademicoException[] excepciones = {
            new PreRequisitoNoAprobadoException("Falta aprobar CALC101"),
            new HorarioConflictivoException("Lunes 9:00 ya está reservado"),
            new CupoLlenoException("CALC101 no tiene cupos"),
            new EstudianteNoEncontradoException("No existe estudiante con ID: 2024999"),
            new ColaDeEsperaVaciaException("No hay estudiantes en espera"),
            new PilaDeshacerVaciaException("No hay operaciones para deshacer"),
            new ArchivoInvalidoException("Línea 3 sin coma")
        };
        int aprobadas = 0;
        for(int i = 0; i < excepciones.length; i++)
        {
            try
            {
                throw excepciones[i];
            }
            catch(SistemaAcademicoException e)
            {
                System.out.println("[OK] Error: " + e.getMensajeFormateado());
                aprobadas++;
            }
        }
        System.out.println("\nResultado: " + aprobadas + "/" + excepciones.length);
    }
}
