package pruebas;

import excepciones.DatoInvalidoException;
import excepciones.HorarioConflictivoException;
import excepciones.SistemaAcademicoException;
import modelo.Aula;
import modelo.Materia;
import modelo.Universidad;

/**
prueba del bloque 5: horarios con matriz boolean[7][24] y aulas en TreeMap
reproduce el ejemplo 4 del enunciado
*/
public class PruebaBloque5
{
    private static int aprobadas = 0;
    private static int total = 0;
    public static void main(String[] args) throws SistemaAcademicoException
    {
        Universidad u = new Universidad();
        u.crearMateria(new Materia("CALC101", "Calculo I", 30, 4));
        u.crearMateria(new Materia("FIS101", "Fisica I", 30, 3));

        System.out.println("=== Aulas en TreeMap ===");
        //se crean desordenadas a proposito
        u.crearAula(new Aula("Laboratorio", 25));
        u.crearAula(new Aula("Aula 305", 40));
        u.crearAula(new Aula("101", 35));
        u.crearAula(new Aula("Auditorio", 200));
        u.crearAula(new Aula("Aula 102", 30));
        String nombres = "";
        Aula[] porNombre = u.listarAulas();
        for(int i = 0; i < porNombre.length; i++)
        {
            nombres += porNombre[i].getNombre() + " | ";
        }
        System.out.println("   por nombre: " + nombres);
        //orden alfabetico de las claves en mayusculas: "AUD" va antes que "AUL" porque D < L
        verificar("listar sale ordenado por nombre", nombres, "101 | Auditorio | Aula 102 | Aula 305 | Laboratorio | ");
        Aula[] porCapacidad = u.listarAulasPorCapacidad();
        verificar("la de mas capacidad primero", porCapacidad[0].getNombre(), "Auditorio");
        verificar("la de menos capacidad al final", porCapacidad[porCapacidad.length - 1].getNombre(), "Laboratorio");
        verificar("buscar sin importar mayusculas", u.buscarAula("aula 305").getCapacidad(), 40);
        verificar("aula repetida", lanzaDato(() -> u.crearAula(new Aula("AULA 305", 10))), true);
        verificar("capacidad 0", lanzaDato(() -> new Aula("X", 0)), true);

        System.out.println("\n=== Reservar (ejemplo 4) ===");
        Aula aula = u.buscarAula("101");
        verificar("aula nueva: lunes 8 libre", aula.consultarDisponibilidad(1, 8), true);
        System.out.println("Aula: 101  Dia: 1  Hora: 8  Duracion: 2\nVerificando disponibilidad...");
        System.out.println(aula.verificarDisponibilidad(1, 8, 2));
        u.reservarHorario("101", 1, 8, 2, "CALC101");
        System.out.println("Reserva exitosa.");
        verificar("lunes 8 ocupado", aula.consultarDisponibilidad(1, 8), false);
        verificar("lunes 9 ocupado", aula.consultarDisponibilidad(1, 9), false);
        verificar("lunes 10 sigue libre", aula.consultarDisponibilidad(1, 10), true);
        verificar("lo ocupa CALC101", aula.quienOcupa(1, 9), "CALC101");

        System.out.println("--- INTENTAR RESERVA CONFLICTIVA ---");
        String mensaje = "";
        try
        {
            u.reservarHorario("101", 1, 9, 2, "FIS101");
        }
        catch(HorarioConflictivoException e)
        {
            mensaje = e.getMensajeFormateado();
            System.out.println("Error: " + mensaje);
        }
        verificar("choca el lunes 9", mensaje, "HorarioConflictivoException - Lunes 9:00 ya está reservado por CALC101");
        verificar("no quedo reservada a medias: lunes 10 libre", aula.consultarDisponibilidad(1, 10), true);
        u.reservarHorario("101", 1, 10, 2, "FIS101");
        verificar("pegada justo despues si se puede", aula.quienOcupa(1, 11), "FIS101");
        u.reservarHorario("101", 3, 14, 3, "");
        verificar("sin materia queda con -", aula.quienOcupa(3, 15), "-");
        System.out.println(aula.horarioDelDia(1));
        verificar("horas ocupadas en la semana", aula.horasOcupadasEnLaSemana(), 7);

        System.out.println("\n=== Validaciones ===");
        verificar("dia 7 no existe", lanzaDato(() -> aula.consultarDisponibilidad(7, 8)), true);
        verificar("hora 24 no existe", lanzaDato(() -> aula.consultarDisponibilidad(1, 24)), true);
        verificar("se pasa de medianoche (23 + 2)", lanzaDato(() -> aula.reservar(2, 23, 2)), true);
        verificar("duracion 0", lanzaDato(() -> aula.reservar(2, 8, 0)), true);
        verificar("materia inexistente", lanzaDato(() -> u.reservarHorario("101", 2, 8, 1, "ZZZ")), true);
        verificar("aula inexistente", lanzaDato(() -> u.reservarHorario("999", 2, 8, 1, "")), true);
        verificar("23:00 de 1 hora si se puede", lanzaDato(() -> aula.reservar(6, 23, 1)), false);

        System.out.println("\n=== Liberar ===");
        u.liberarHorario("101", 3, 14, 3);
        verificar("jueves 14 quedo libre", aula.consultarDisponibilidad(3, 14), true);
        verificar("liberar algo libre falla", lanzaDato(() -> u.liberarHorario("101", 3, 14, 1)), true);
        verificar("liberar el lunes 12 (libre) falla", lanzaDato(() -> u.liberarHorario("101", 1, 12, 1)), true);
        verificar("liberar 11 a 13 falla sin tocar las 11", lanzaDato(() -> u.liberarHorario("101", 1, 11, 2)), true);
        verificar("las 11 siguen ocupadas", aula.consultarDisponibilidad(1, 11), false);

        System.out.println("\n=== Cambiar horario ===");
        u.cambiarHorario("101", 1, 8, 2, 2, 8); //CALC101 del lunes al martes
        verificar("lunes 8 queda libre", aula.consultarDisponibilidad(1, 8), true);
        verificar("martes 8 es de CALC101", aula.quienOcupa(2, 8), "CALC101");
        boolean choco = false;
        try
        {
            u.cambiarHorario("101", 2, 8, 2, 1, 10); //el lunes 10 es de FIS101
        }
        catch(HorarioConflictivoException e)
        {
            choco = true;
            System.out.println("Error: " + e.getMensajeFormateado());
        }
        verificar("cambiar a un horario ocupado falla", choco, true);
        verificar("y CALC101 vuelve a quedar el martes", aula.quienOcupa(2, 8), "CALC101");
        verificar("sin perder la segunda hora", aula.quienOcupa(2, 9), "CALC101");

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
