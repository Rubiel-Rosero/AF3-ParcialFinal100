package pruebas;

import excepciones.ColaDeEsperaVaciaException;
import excepciones.DatoInvalidoException;
import excepciones.PreRequisitoNoAprobadoException;
import excepciones.SistemaAcademicoException;
import modelo.Estudiante;
import modelo.Materia;
import modelo.Universidad;

/**
prueba del bloque 4: materias, prerrequisitos (lista enlazada) y cola de espera
reproduciendo el ejemplo 2 del enunciado
*/
public class PruebaBloque4
{
    private static int aprobadas = 0;
    private static int total = 0;
    public static void main(String[] args) throws SistemaAcademicoException
    {
        Universidad u = new Universidad();
        u.registrarEstudiante(new Estudiante("2024001", "Ana Maria Gomez", "ana@u.edu", 3));
        u.registrarEstudiante(new Estudiante("2024002", "Juan Perez", "juan@u.edu", 1));
        u.registrarEstudiante(new Estudiante("2024003", "Maria Lopez", "maria@u.edu", 1));
        u.registrarEstudiante(new Estudiante("2024004", "Carlos Ruiz", "carlos@u.edu", 1));
        u.registrarEstudiante(new Estudiante("2024005", "Laura Diaz", "laura@u.edu", 1));
        u.registrarEstudiante(new Estudiante("2024006", "Pablo Ortiz", "pablo@u.edu", 1));

        System.out.println("=== Crear materias ===");
        u.crearMateria(new Materia("CALC101", "Calculo I", 3, 4));
        u.crearMateria(new Materia("calc102", "Calculo II", 2, 4));
        u.crearMateria(new Materia("FIS101", "Fisica I", 30, 3));
        verificar("hay 3 materias", u.getCantidadMaterias(), 3);
        verificar("el codigo se guarda en mayusculas", u.buscarMateria("CALC102").getNombre(), "Calculo II");
        verificar("materia repetida", lanzaDato(() -> u.crearMateria(new Materia("CALC101", "Otra", 5, 3))), true);
        verificar("cupos en 0", lanzaDato(() -> new Materia("X1", "X", 0, 3)), true);
        verificar("materia inexistente", lanzaDato(() -> u.buscarMateria("ZZZ999")), true);

        System.out.println("\n=== Prerrequisitos (lista enlazada) ===");
        u.agregarPrerrequisito("CALC102", "CALC101");
        u.agregarPrerrequisito("CALC102", "FIS101");
        System.out.println(u.buscarMateria("CALC102").mostrarPrerrequisitos());
        verificar("CALC102 tiene 2 prerrequisitos", u.buscarMateria("CALC102").getPrerrequisitos().size(), 2);
        verificar("no repetir prerrequisito", lanzaDato(() -> u.agregarPrerrequisito("CALC102", "CALC101")), true);
        verificar("no ser prerrequisito de si misma", lanzaDato(() -> u.agregarPrerrequisito("CALC101", "CALC101")), true);
        //Ana aprobo CALC101 pero no ha visto FIS101
        u.buscarEstudiante("2024001").registrarNota(1, "CALC101", 4.5);
        String mensaje = "";
        try
        {
            u.inscribirEstudiante("2024001", "CALC102");
        }
        catch(PreRequisitoNoAprobadoException e)
        {
            mensaje = e.getMensajeFormateado();
            System.out.println("Error: " + mensaje);
        }
        verificar("le falta FIS101", mensaje, "PreRequisitoNoAprobadoException - Ana Maria Gomez no ha aprobado: FIS101");
        u.buscarEstudiante("2024001").registrarNota(1, "FIS101", 3.0);
        verificar("con los dos aprobados se inscribe", u.inscribirEstudiante("2024001", "CALC102"), true);
        verificar("inscribir dos veces", lanzaDato(() -> u.inscribirEstudiante("2024001", "CALC102")), true);

        System.out.println("\n=== Cola de espera (ejemplo 2) ===");
        Materia calculo = u.buscarMateria("CALC101");
        System.out.println("Materia: " + calculo + "\nCupos totales: " + calculo.getCuposMaximos());
        String[] ids = { "2024002", "2024003", "2024004", "2024005", "2024006" };
        for(int i = 0; i < ids.length; i++)
        {
            boolean inscrito = u.inscribirEstudiante(ids[i], "CALC101");
            String nombre = u.buscarEstudiante(ids[i]).getNombre();
            if(inscrito)
            {
                System.out.println((i + 1) + ". " + nombre + " -> Cupo disponible (cupos restantes: " + calculo.getCuposDisponibles() + ")");
            }
            else
            {
                System.out.println((i + 1) + ". " + nombre + " -> Materia llena. Agregada a COLA DE ESPERA");
            }
        }
        verificar("3 inscritos", calculo.getInscritos().size(), 3);
        verificar("2 en espera", calculo.getColaEspera().size(), 2);
        verificar("Laura esta de primera", calculo.getColaEspera().peek().getNombre(), "Laura Diaz");
        verificar("encolar dos veces a Laura", lanzaDato(() -> u.inscribirEstudiante("2024005", "CALC101")), true);
        System.out.println("--- MOSTRAR COLA DE ESPERA ---\n" + calculo.mostrarColaEspera());

        System.out.println("--- Liberar cupo (cancelar inscripcion de Juan Perez) ---");
        Estudiante asignado = u.cancelarInscripcion("2024002", "CALC101");
        System.out.println("Cancelacion exitosa. Cupo liberado.");
        System.out.println("Asignando cupo a " + asignado.getNombre() + " (primer estudiante en cola)");
        verificar("el cupo es para Laura", asignado.getNombre(), "Laura Diaz");
        verificar("Laura ya esta inscrita", calculo.estaInscrito(asignado), true);
        verificar("Juan ya no esta", calculo.estaInscrito(u.buscarEstudiante("2024002")), false);
        verificar("siguen 3 inscritos", calculo.getInscritos().size(), 3);
        verificar("en espera queda solo Pablo", calculo.getColaEspera().peek().getNombre(), "Pablo Ortiz");
        verificar("cancelar a alguien no inscrito", lanzaDato(() -> u.cancelarInscripcion("2024002", "CALC101")), true);
        u.cancelarInscripcion("2024003", "CALC101"); //entra Pablo
        Estudiante nadie = u.cancelarInscripcion("2024004", "CALC101"); //la cola ya esta vacia
        verificar("cola vacia: nadie recibe el cupo", nadie == null, true);
        verificar("queda un cupo libre", calculo.getCuposDisponibles(), 1);
        boolean colaVacia = false;
        try
        {
            calculo.mostrarColaEspera();
        }
        catch(ColaDeEsperaVaciaException e)
        {
            colaVacia = true;
            System.out.println("Error: " + e.getMensajeFormateado());
        }
        verificar("mostrar cola vacia lanza excepcion", colaVacia, true);

        System.out.println("\n=== Eliminar estudiante inscrito ===");
        verificar("no se puede eliminar a Laura (inscrita)", lanzaDato(() -> u.eliminarEstudiante("2024005")), true);
        u.cancelarInscripcion("2024005", "CALC101");
        verificar("despues de cancelar si se puede", u.eliminarEstudiante("2024005").getNombre(), "Laura Diaz");
        System.out.println(u.materiasDeEstudiante("2024001"));

        System.out.println("\nResultado: " + aprobadas + "/" + total);
    }
    private interface Accion
    {
        void ejecutar() throws SistemaAcademicoException;
    }
    private static boolean lanzaDato(Accion accion)
    {
        try
        {
            accion.ejecutar();
            return false;
        }
        catch(DatoInvalidoException e)
        {
            return true;
        }
        catch(SistemaAcademicoException e)
        {
            return false;
        }
    }
    private static void verificar(String que, Object obtenido, Object esperado)
    {
        total++;
        if(esperado.equals(obtenido))
        {
            aprobadas++;
            System.out.println("[OK] " + que);
        }
        else
        {
            System.out.println("[FALLA] " + que + " -> esperaba " + esperado + " y dio " + obtenido);
        }
    }
}
