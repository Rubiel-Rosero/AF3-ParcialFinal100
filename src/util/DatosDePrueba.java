package util;

import excepciones.SistemaAcademicoException;
import modelo.Aula;
import modelo.Estudiante;
import modelo.Materia;
import modelo.Universidad;

/**
datos para probar el menu
*/
public class DatosDePrueba
{
    public static void cargar(Universidad u) throws SistemaAcademicoException
    {
        u.cargarFacultades();
        u.getCampus().cargarCampusPorDefecto();

        //estudiantes
        u.registrarEstudiante(new Estudiante("2024001", "Ana Maria Gomez", "ana.gomez@universidad.edu", 3));
        u.registrarEstudiante(new Estudiante("2024002", "Juan Perez", "juan.perez@universidad.edu", 1));
        u.registrarEstudiante(new Estudiante("2024003", "Maria Lopez", "maria.lopez@universidad.edu", 1));
        u.registrarEstudiante(new Estudiante("2024004", "Carlos Ruiz", "carlos.ruiz@universidad.edu", 1));
        u.registrarEstudiante(new Estudiante("2024005", "Laura Diaz", "laura.diaz@universidad.edu", 1));
        u.registrarEstudiante(new Estudiante("2024006", "Pablo Ortiz", "pablo.ortiz@universidad.edu", 1));

        //notas de ana
        Estudiante ana = u.buscarEstudiante("2024001");
        ana.registrarNota(1, "CALC101", 4.5);
        ana.registrarNota(1, "FIS101", 2.8);
        ana.registrarNota(1, "PROG101", 5.0);
        ana.registrarNota(2, "CALC102", 3.5);
        ana.registrarNota(2, "FIS102", 3.0);
        ana.registrarNota(2, "MATDIS", 2.5);

        //materias
        u.crearMateria(new Materia("CALC101", "Calculo I", 3, 4));
        u.crearMateria(new Materia("CALC102", "Calculo II", 30, 4));
        u.crearMateria(new Materia("FIS101", "Fisica I", 30, 3));
        u.crearMateria(new Materia("PROG101", "Programacion I", 30, 3));
        u.crearMateria(new Materia("PROG201", "Programacion II", 25, 3));
        u.agregarPrerrequisito("CALC102", "CALC101");
        u.agregarPrerrequisito("PROG201", "PROG101");

        //aulas
        u.crearAula(new Aula("101", 35));
        u.crearAula(new Aula("102", 30));
        u.crearAula(new Aula("Auditorio", 200));
        u.crearAula(new Aula("Laboratorio", 25));
    }
}
