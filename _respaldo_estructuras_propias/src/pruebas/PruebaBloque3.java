package pruebas;

import excepciones.DatoInvalidoException;
import excepciones.EstudianteNoEncontradoException;
import excepciones.SistemaAcademicoException;
import modelo.Estudiante;
import modelo.Universidad;

/**
prueba del bloque 3: gestion de estudiantes con HashMap
registrar, buscar, listar ordenado, eliminar y restaurar
*/
public class PruebaBloque3
{
    private static int aprobadas = 0;
    private static int total = 0;
    public static void main(String[] args) throws SistemaAcademicoException
    {
        Universidad u = new Universidad();

        System.out.println("=== Registrar (ejemplo 1) ===");
        u.registrarEstudiante(new Estudiante("2024001", "Ana Maria Gomez", "ana.gomez@universidad.edu", 3));
        verificar("se registro Ana", u.getCantidadEstudiantes(), 1);
        //se registran desordenados a proposito
        u.registrarEstudiante(new Estudiante("2024005", "Laura Diaz", "laura@u.edu", 1));
        u.registrarEstudiante(new Estudiante("2024002", "Juan Perez", "juan@u.edu", 2));
        u.registrarEstudiante(new Estudiante("2024004", "Carlos Ruiz", "carlos@u.edu", 4));
        u.registrarEstudiante(new Estudiante("2024003", "Maria Lopez", "maria@u.edu", 2));
        verificar("hay 5 estudiantes", u.getCantidadEstudiantes(), 5);
        boolean duplicado = false;
        try
        {
            u.registrarEstudiante(new Estudiante("2024001", "Otra Ana", "otra@u.edu", 1));
        }
        catch(DatoInvalidoException e)
        {
            duplicado = true;
            System.out.println("   Error: " + e.getMensajeFormateado());
        }
        verificar("id repetido no se registra", duplicado, true);
        verificar("sigue la Ana original", u.buscarEstudiante("2024001").getNombre(), "Ana Maria Gomez");

        System.out.println("\n=== Buscar ===");
        Estudiante ana = u.buscarEstudiante("2024001");
        System.out.println("Resultado encontrado:\n" + ana.mostrarInformacion());
        verificar("buscar con espacios", u.buscarEstudiante("  2024003 ").getNombre(), "Maria Lopez");
        boolean noExiste = false;
        try
        {
            u.buscarEstudiante("2024999");
        }
        catch(EstudianteNoEncontradoException e)
        {
            noExiste = true;
            System.out.println("Error: " + e.getMensajeFormateado());
            verificar("mensaje como el ejemplo 1", e.getMensajeFormateado(), "EstudianteNoEncontradoException - No existe estudiante con ID: 2024999");
        }
        verificar("id inexistente lanza excepcion", noExiste, true);

        System.out.println("\n=== Listar (ordenado por id) ===");
        Estudiante[] lista = u.listarEstudiantes();
        String ids = "";
        for(int i = 0; i < lista.length; i++)
        {
            ids += lista[i].getId() + " ";
            System.out.println("   " + lista[i].getId() + " | " + lista[i].getNombre());
        }
        verificar("salen ordenados aunque se registraron desordenados", ids.trim(), "2024001 2024002 2024003 2024004 2024005");

        System.out.println("\n=== Eliminar y restaurar ===");
        ana.registrarNota(1, "CALC101", 4.5);
        Estudiante eliminado = u.eliminarEstudiante("2024001");
        verificar("eliminar devuelve a Ana", eliminado.getNombre(), "Ana Maria Gomez");
        verificar("ya no existe", u.existeEstudiante("2024001"), false);
        verificar("quedan 4", u.getCantidadEstudiantes(), 4);
        boolean eliminarOtraVez = false;
        try
        {
            u.eliminarEstudiante("2024001");
        }
        catch(EstudianteNoEncontradoException e)
        {
            eliminarOtraVez = true;
        }
        verificar("eliminar dos veces lanza excepcion", eliminarOtraVez, true);
        u.restaurarEstudiante(eliminado);
        verificar("restaurar la devuelve", u.existeEstudiante("2024001"), true);
        verificar("vuelve con sus notas", u.buscarEstudiante("2024001").haAprobado("CALC101"), true);

        System.out.println("\n=== Muchos estudiantes ===");
        Universidad grande = new Universidad();
        for(int i = 0; i < 10000; i++)
        {
            grande.registrarEstudiante(new Estudiante("ID" + i, "Estudiante " + i, "e" + i + "@u.edu", 1));
        }
        long inicio = System.nanoTime();
        Estudiante ultimo = grande.buscarEstudiante("ID9999");
        long tiempo = System.nanoTime() - inicio;
        verificar("encuentra el ultimo de 10000", ultimo.getNombre(), "Estudiante 9999");
        System.out.println("   buscar entre 10000 tardó " + tiempo / 1000 + " microsegundos (no recorre los 10000)");

        System.out.println("\nResultado: " + aprobadas + "/" + total);
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
