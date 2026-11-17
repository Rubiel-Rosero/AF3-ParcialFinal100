package pruebas;

import excepciones.DatoInvalidoException;
import excepciones.SistemaAcademicoException;
import modelo.Campus;
import modelo.Ruta;
import modelo.Universidad;

/*
prueba del bloque 6: matriz de adyacencia int[N][N] y dijkstra
reproduce el ejemplo 5 del enunciado
*/
public class PruebaBloque6
{
    private static int aprobadas = 0;
    private static int total = 0;
    public static void main(String[] args) throws SistemaAcademicoException
    {
        Universidad u = new Universidad();
        Campus campus = u.getCampus();
        campus.cargarCampusPorDefecto();

        System.out.println("=== Matriz de adyacencia ===");
        System.out.println(campus.listarEdificios());
        System.out.println(campus.mostrarMatriz());
        verificar("hay 5 edificios", campus.getCantidadEdificios(), 5);
        verificar("Ingenieria-Cafeteria 150", campus.getDistanciaDirecta(0, 2), 150);
        verificar("es simetrica", campus.getDistanciaDirecta(2, 0), 150);
        verificar("Ingenieria-Rectoria sin camino directo", campus.getDistanciaDirecta(0, 3), 0);
        boolean simetrica = true;
        for(int i = 0; i < 5; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                if(campus.getDistanciaDirecta(i, j) != campus.getDistanciaDirecta(j, i))
                {
                    simetrica = false;
                }
            }
        }
        verificar("toda la matriz es simetrica", simetrica, true);

        System.out.println("\n=== Ruta mas corta (ejemplo 5) ===");
        System.out.println("Origen: Ingenieria\nDestino: Rectoria\n--- RESULTADO ---");
        Ruta ruta = campus.calcularRutaMasCorta("Ingenieria", "Rectoria");
        System.out.println(ruta);
        verificar("camino como el ejemplo 5", ruta.getCamino(), "Ingenieria -> Cafeteria (150m) -> Rectoria (180m)");
        verificar("total 330", ruta.getDistanciaTotal(), 330);
        verificar("por nombre sin importar mayusculas", campus.calcularRutaMasCorta("INGENIERIA", "rectoria").getDistanciaTotal(), 330);
        //la que se mejora en el camino: directo 300, pasando por cafeteria 270
        Ruta aLab = campus.calcularRutaMasCorta(0, 4);
        verificar("a Laboratorios 270 y no 300 directo", aLab.getDistanciaTotal(), 270);
        verificar("pasa por Cafeteria", aLab.getCamino(), "Ingenieria -> Cafeteria (150m) -> Laboratorios (120m)");
        verificar("a Biblioteca el directo (200)", campus.calcularRutaMasCorta(0, 1).getDistanciaTotal(), 200);
        verificar("al reves da lo mismo", campus.calcularRutaMasCorta("Rectoria", "Ingenieria").getDistanciaTotal(), 330);
        Ruta mismo = campus.calcularRutaMasCorta(2, 2);
        verificar("origen = destino: 0 metros", mismo.getDistanciaTotal(), 0);
        verificar("origen = destino: un solo edificio", mismo.getEdificios().length, 1);

        System.out.println("\n=== Quitar Cafeteria-Rectoria (pregunta de practica 3) ===");
        campus.eliminarConexion(2, 3);
        Ruta nueva = campus.calcularRutaMasCorta("Ingenieria", "Rectoria");
        System.out.println(nueva);
        verificar("ahora va por Biblioteca: 450", nueva.getDistanciaTotal(), 450);
        campus.agregarConexion("Cafeteria", "Rectoria", 180);
        verificar("al volver a conectarla vuelve a 330", campus.calcularRutaMasCorta(0, 3).getDistanciaTotal(), 330);

        System.out.println("\n=== Edificio sin caminos ===");
        int gimnasio = campus.agregarEdificio("Gimnasio");
        verificar("Gimnasio es el 5", gimnasio, 5);
        String mensaje = "";
        try
        {
            campus.calcularRutaMasCorta("Ingenieria", "Gimnasio");
        }
        catch(DatoInvalidoException e)
        {
            mensaje = e.getMessage();
            System.out.println("Error: " + e.getMensajeFormateado());
        }
        verificar("sin ruta lanza excepcion", mensaje, "No hay ruta entre Ingenieria y Gimnasio");
        campus.agregarConexion("Gimnasio", "Laboratorios", 90);
        System.out.println(campus.calcularRutaMasCorta("Ingenieria", "Gimnasio"));
        verificar("conectado ya tiene ruta (270 + 90)", campus.calcularRutaMasCorta("Ingenieria", "Gimnasio").getDistanciaTotal(), 360);

        System.out.println("\n=== Validaciones ===");
        verificar("metros 0", lanzaDato(() -> campus.agregarConexion(0, 1, 0)), true);
        verificar("metros negativos", lanzaDato(() -> campus.agregarConexion(0, 1, -50)), true);
        verificar("consigo mismo", lanzaDato(() -> campus.agregarConexion(3, 3, 10)), true);
        verificar("edificio 9 no existe", lanzaDato(() -> campus.agregarConexion(0, 9, 10)), true);
        verificar("nombre que no existe", lanzaDato(() -> campus.calcularRutaMasCorta("Ingenieria", "Piscina")), true);
        verificar("edificio repetido", lanzaDato(() -> campus.agregarEdificio("biblioteca")), true);
        verificar("eliminar conexion que no existe", lanzaDato(() -> campus.eliminarConexion(0, 3)), true);
        Campus chico = new Campus(2);
        chico.agregarEdificio("A");
        chico.agregarEdificio("B");
        verificar("arreglo lleno", lanzaDato(() -> chico.agregarEdificio("C")), true);

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
