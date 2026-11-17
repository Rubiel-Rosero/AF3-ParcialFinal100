package pruebas;

import excepciones.DatoInvalidoException;
import excepciones.SistemaAcademicoException;
import modelo.Estudiante;
import modelo.Evaluacion;
import modelo.ExamenFinal;
import modelo.Facultad;
import modelo.Parcial;
import modelo.Quiz;
import modelo.Persona;
import modelo.Profesor;
import modelo.Universidad;

/**
prueba del bloque 2: persona, estudiante, profesor, facultades
usa los datos del ejemplo 1 y el ejemplo 6 del enunciado
*/
public class PruebaBloque2
{
    private static int aprobadas = 0;
    private static int total = 0;
    public static void main(String[] args) throws SistemaAcademicoException
    {
        System.out.println("=== Herencia y polimorfismo ===");
        probarHerencia();
        System.out.println("\n=== Notas Double[10][20] (ejemplo 6) ===");
        probarNotas();
        System.out.println("\n=== Validaciones ===");
        probarValidaciones();
        System.out.println("\n=== Facultad[5] ===");
        probarFacultades();
        System.out.println("\n=== Evaluaciones (Quiz, Parcial, ExamenFinal) ===");
        probarEvaluaciones();
        System.out.println("\nResultado: " + aprobadas + "/" + total);
    }
    private static void probarHerencia() throws SistemaAcademicoException
    {
        Estudiante ana = new Estudiante("2024001", "Ana Maria Gomez", "ana.gomez@universidad.edu", 3);
        Profesor jhon = new Profesor("P001", "Jhon Caro", "jhon@universidad.edu", "Sistemas", 5000000);
        //un arreglo de Persona puede guardar estudiantes y profesores
        Persona[] personas = { ana, jhon };
        verificar("estudiante es una persona", personas[0] instanceof Persona, true);
        verificar("getRol del estudiante", personas[0].getRol(), "Estudiante");
        verificar("getRol del profesor", personas[1].getRol(), "Profesor");
        //el mismo metodo da cosas distintas segun el objeto (sobrescritura)
        String infoAna = personas[0].mostrarInformacion();
        verificar("mostrarInformacion como en el ejemplo 1", infoAna,
            "ID: 2024001\nNombre: Ana Maria Gomez\nEmail: ana.gomez@universidad.edu\nSemestre: 3\nPromedio acumulado: 0.0 (sin notas aún)");
        verificar("profesor muestra departamento", personas[1].mostrarInformacion().contains("Departamento: Sistemas"), true);
        System.out.println("\n" + infoAna + "\n");
    }
    private static void probarNotas() throws SistemaAcademicoException
    {
        Estudiante ana = new Estudiante("2024001", "Ana Maria Gomez", "ana.gomez@universidad.edu", 3);
        ana.registrarNota(1, "CALC101", 4.5);
        ana.registrarNota(1, "FIS101", 2.8);
        ana.registrarNota(1, "PROG101", 5.0);
        ana.registrarNota(2, "CALC102", 3.5);
        ana.registrarNota(2, "FIS102", 3.0);
        ana.registrarNota(2, "MATDIS", 2.5);
        verificar("promedio semestre 1", Estudiante.formatear(ana.calcularPromedio(1)), "4.10");
        verificar("promedio semestre 2", Estudiante.formatear(ana.calcularPromedio(2)), "3.00");
        verificar("promedio acumulado", Estudiante.formatear(ana.calcularPromedio()), "3.55");
        verificar("semestre sin notas da 0", ana.calcularPromedio(5), 0.0);
        verificar("aprobo CALC101", ana.haAprobado("CALC101"), true);
        verificar("no aprobo FIS101 (2.8)", ana.haAprobado("FIS101"), false);
        verificar("no ha visto ALG200", ana.haAprobado("ALG200"), false);
        verificar("historial tiene 6 materias", ana.getHistorialMaterias().tamanio(), 6);
        //cambiar una nota devuelve la anterior (para deshacer)
        Double anterior = ana.registrarNota(1, "FIS101", 3.2);
        verificar("cambiar nota devuelve la anterior", anterior, 2.8);
        verificar("ahora si aprobo FIS101", ana.haAprobado("FIS101"), true);
        verificar("cambiar nota no repite en historial", ana.getHistorialMaterias().tamanio(), 6);
        Double primera = ana.registrarNota(3, "EST201", 4.0);
        verificar("nota nueva devuelve null", primera == null, true);
        ana.borrarNota(3, "EST201");
        verificar("borrarNota la quita del historial", ana.getHistorialMaterias().contiene("EST201"), false);
        //llenar un semestre completo
        Estudiante lleno = new Estudiante("X1", "Lleno", "x@u.edu", 1);
        for(int j = 0; j < Estudiante.MAX_MATERIAS; j++)
        {
            lleno.registrarNota(1, "M" + j, 3.0);
        }
        verificar("materia 21 en el mismo semestre falla", lanza(() -> lleno.registrarNota(1, "M20", 3.0)), true);
    }
    private static void probarValidaciones()
    {
        verificar("email sin @", lanza(() -> new Estudiante("1", "Ana", "ana.correo", 1)), true);
        verificar("nombre vacio", lanza(() -> new Estudiante("1", "  ", "a@u.edu", 1)), true);
        verificar("id vacio", lanza(() -> new Estudiante("", "Ana", "a@u.edu", 1)), true);
        verificar("semestre 0", lanza(() -> new Estudiante("1", "Ana", "a@u.edu", 0)), true);
        verificar("semestre 11", lanza(() -> new Estudiante("1", "Ana", "a@u.edu", 11)), true);
        verificar("salario negativo", lanza(() -> new Profesor("P1", "Jhon", "j@u.edu", "Sis", -1)), true);
        verificar("nota 5.1", lanza(() -> new Estudiante("1", "Ana", "a@u.edu", 1).registrarNota(1, "X", 5.1)), true);
        verificar("nota negativa", lanza(() -> new Estudiante("1", "Ana", "a@u.edu", 1).registrarNota(1, "X", -1)), true);
    }
    private static void probarFacultades() throws SistemaAcademicoException
    {
        Universidad u = new Universidad();
        u.cargarFacultades();
        verificar("se cargan 5", u.getCantidadFacultades(), 5);
        verificar("buscar ING", u.buscarFacultad("ing").getNombre(), "Ingeniería");
        verificar("buscar inexistente da null", u.buscarFacultad("ART") == null, true);
        verificar("la sexta no cabe", lanza(() -> u.agregarFacultad(new Facultad("ART", "Artes"))), true);
        System.out.print(u.listarFacultades());
    }
    private static void probarEvaluaciones() throws SistemaAcademicoException
    {
        //2 quices (10% c/u) + 2 parciales (25% c/u) + final (30%) = 100%
        Evaluacion[] evaluaciones = {
            new Quiz("Quiz 1", 4.0),
            new Quiz("Quiz 2", 3.0),
            new Parcial("Parcial 1", 3.6),
            new Parcial("Parcial 2", 4.0),
            new ExamenFinal("Final", 4.5)
        };
        verificar("evaluaciones[0] es Quiz", evaluaciones[0].getTipo(), "Quiz");
        verificar("evaluaciones[4] es Examen final", evaluaciones[4].getTipo(), "Examen final");
        verificar("quiz vale 10%", evaluaciones[0].getPorcentaje(), 10.0);
        verificar("parcial vale 25%", evaluaciones[2].getPorcentaje(), 25.0);
        verificar("final vale 30%", evaluaciones[4].getPorcentaje(), 30.0);
        verificar("aporte del final 4.5 * 30%", Estudiante.formatear(evaluaciones[4].calcularAporte()), "1.35");
        double definitiva = Evaluacion.calcularDefinitiva(evaluaciones);
        verificar("nota definitiva", Estudiante.formatear(definitiva), "3.95");
        for(int i = 0; i < evaluaciones.length; i++)
        {
            System.out.println("   " + evaluaciones[i]);
        }
        //la definitiva se guarda en la matriz del estudiante
        Estudiante ana = new Estudiante("2024001", "Ana Maria Gomez", "ana@u.edu", 3);
        ana.registrarNota(3, "PROG201", definitiva);
        verificar("definitiva queda en la matriz", ana.haAprobado("PROG201"), true);
        //sobrecarga del constructor de Parcial
        verificar("parcial con otro porcentaje", new Parcial("Proyecto", 4.0, 40).getPorcentaje(), 40.0);
        Evaluacion[] incompletas = { new Quiz("Q", 4.0), new ExamenFinal("F", 4.0) };
        verificar("porcentajes que no suman 100", lanza(() -> Evaluacion.calcularDefinitiva(incompletas)), true);
        verificar("evaluacion con nota 6", lanza(() -> new Quiz("Q", 6)), true);
    }
    //para probar que algo lanza DatoInvalidoException
    private interface Accion
    {
        void ejecutar() throws SistemaAcademicoException;
    }
    private static boolean lanza(Accion accion)
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
