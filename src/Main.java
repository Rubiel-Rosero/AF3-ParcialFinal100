import excepciones.SistemaAcademicoException;
import modelo.Aula;
import modelo.Campus;
import modelo.Estudiante;
import modelo.Materia;
import modelo.Universidad;
import util.DatosDePrueba;
import util.Entrada;

/**
clase principal con el menu con todas las opciones del enunciado
los errores del sistema se atrapan con un solo catch (la clase padre SistemaAcademicoException)
y se muestran con su nombre, sin que el programa se cierre
*/
public class Main
{
    private static Universidad universidad = new Universidad();
    public static void main(String[] args)
    {
        System.out.println("============================================================");
        System.out.println("  PLANIFICACIÓN ACADÉMICA - SISTEMA UNIVERSITARIO");
        System.out.println("============================================================");
        cargarDatosIniciales();
        int opcion;
        do
        {
            mostrarMenu();
            opcion = Entrada.leerEntero("Seleccione una opción: ");
            System.out.println();
            try
            {
                ejecutarOpcion(opcion);
            }
            catch(SistemaAcademicoException e)
            {
                //cualquiera de las excepciones propias: se muestra y se sigue
                System.out.println("Error: " + e.getMensajeFormateado());
            }
            catch(RuntimeException e)
            {
                //un error inesperado tampoco cierra el programa
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }while(opcion != 22);
    }
    private static void cargarDatosIniciales()
    {
        try
        {
            if(Entrada.confirmar("¿Cargar datos de prueba de los ejemplos del enunciado?"))
            {
                DatosDePrueba.cargar(universidad);
                System.out.println("Datos de prueba cargados: 6 estudiantes, 5 materias, 4 aulas, 5 edificios y 5 facultades.");
            }
            else
            {
                universidad.cargarFacultades();
                universidad.getCampus().cargarCampusPorDefecto();
                System.out.println("Se cargaron solo las facultades y los 5 edificios del campus.");
            }
        }
        catch(SistemaAcademicoException e)
        {
            System.out.println("No se pudieron cargar los datos: " + e.getMensajeFormateado());
        }
    }
    private static void mostrarMenu()
    {
        System.out.println("\n============================================================");
        System.out.println("  PLANIFICACIÓN ACADÉMICA - SISTEMA UNIVERSITARIO");
        System.out.println("============================================================");
        System.out.println("=== GESTIÓN DE ESTUDIANTES ===");
        System.out.println(" 1. Registrar estudiante");
        System.out.println(" 2. Buscar estudiante por ID");
        System.out.println(" 3. Listar todos los estudiantes");
        System.out.println(" 4. Eliminar estudiante");
        System.out.println("=== GESTIÓN DE MATERIAS ===");
        System.out.println(" 5. Crear materia");
        System.out.println(" 6. Agregar pre-requisito");
        System.out.println(" 7. Mostrar pre-requisitos");
        System.out.println(" 8. Inscribir estudiante");
        System.out.println(" 9. Cancelar inscripción");
        System.out.println("10. Mostrar cola de espera");
        System.out.println("=== GESTIÓN DE HORARIOS ===");
        System.out.println("11. Reservar horario en aula");
        System.out.println("12. Liberar horario");
        System.out.println("13. Consultar disponibilidad");
        System.out.println("=== RUTAS ENTRE EDIFICIOS ===");
        System.out.println("14. Agregar conexión entre edificios");
        System.out.println("15. Calcular ruta más corta");
        System.out.println("=== REPORTES ACADÉMICOS ===");
        System.out.println("16. Registrar nota");
        System.out.println("17. Ver reporte académico");
        System.out.println("18. Navegador de reportes (atrás/adelante)");
        System.out.println("=== SISTEMA DESHACER/REHACER ===");
        System.out.println("19. Deshacer última operación");
        System.out.println("20. Rehacer última operación");
        System.out.println("=== PROCESAMIENTO POR LOTES ===");
        System.out.println("21. Procesar archivo CSV");
        System.out.println("=== SALIR ===");
        System.out.println("22. Salir");
    }
    private static void ejecutarOpcion(int opcion) throws SistemaAcademicoException
    {
        switch(opcion)
        {
            case 1: registrarEstudiante(); break;
            case 2: buscarEstudiante(); break;
            case 3: listarEstudiantes(); break;
            case 4: eliminarEstudiante(); break;
            case 5: crearMateria(); break;
            case 6: agregarPrerrequisito(); break;
            case 7: mostrarPrerrequisitos(); break;
            case 8: inscribirEstudiante(); break;
            case 9: cancelarInscripcion(); break;
            case 10: mostrarColaEspera(); break;
            case 11: reservarHorario(); break;
            case 12: liberarHorario(); break;
            case 13: consultarDisponibilidad(); break;
            case 14: agregarConexion(); break;
            case 15: calcularRuta(); break;
            case 16: case 17: case 18:
                System.out.println("Esta opción se habilita en el bloque 7 (reportes académicos).");
                break;
            case 19: case 20:
                System.out.println("Esta opción se habilita en el bloque 8 (deshacer/rehacer).");
                break;
            case 21:
                System.out.println("Esta opción se habilita en el bloque 9 (procesamiento por lotes).");
                break;
            case 22:
                System.out.println("Hasta luego.");
                break;
            default:
                System.out.println("Opción inválida. Escriba un número del 1 al 22.");
        }
    }
    // estudiantes
    private static void registrarEstudiante() throws SistemaAcademicoException
    {
        System.out.println("--- REGISTRO DE ESTUDIANTE ---");
        String id = Entrada.leerTexto("ID: ");
        String nombre = Entrada.leerTexto("Nombre: ");
        String email = Entrada.leerTexto("Email: ");
        int semestre = Entrada.leerEntero("Semestre actual: ");
        universidad.registrarEstudiante(new Estudiante(id, nombre, email, semestre));
        System.out.println("Estudiante registrado exitosamente.");
    }
    private static void buscarEstudiante() throws SistemaAcademicoException
    {
        System.out.println("--- BUSCAR ESTUDIANTE ---");
        String id = Entrada.leerTexto("ID: ");
        Estudiante estudiante = universidad.buscarEstudiante(id);
        System.out.println("Resultado encontrado:\n" + estudiante.mostrarInformacion());
    }
    private static void listarEstudiantes()
    {
        System.out.println("--- ESTUDIANTES REGISTRADOS ---");
        Estudiante[] lista = universidad.listarEstudiantes();
        if(lista.length == 0)
        {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        for(int i = 0; i < lista.length; i++)
        {
            System.out.println(lista[i].getId() + " | " + lista[i].getNombre() + " | Semestre " + lista[i].getSemestre() + " | Promedio " + Estudiante.formatear(lista[i].calcularPromedio()));
        }
        System.out.println("Total: " + lista.length);
    }
    private static void eliminarEstudiante() throws SistemaAcademicoException
    {
        System.out.println("--- ELIMINAR ESTUDIANTE ---");
        String id = Entrada.leerTexto("ID: ");
        Estudiante estudiante = universidad.buscarEstudiante(id);
        if(!Entrada.confirmar("¿Seguro que desea eliminar a " + estudiante.getNombre() + "?"))
        {
            System.out.println("Eliminación cancelada.");
            return;
        }
        universidad.eliminarEstudiante(id);
        System.out.println("Estudiante " + estudiante.getNombre() + " eliminado.");
    }
    // materias
    private static void crearMateria() throws SistemaAcademicoException
    {
        System.out.println("--- CREAR MATERIA ---");
        String codigo = Entrada.leerTexto("Código: ");
        String nombre = Entrada.leerTexto("Nombre: ");
        int cupos = Entrada.leerEntero("Cupos máximos: ");
        int creditos = Entrada.leerEntero("Créditos: ");
        universidad.crearMateria(new Materia(codigo, nombre, cupos, creditos));
        System.out.println("Materia creada exitosamente.");
    }
    private static void agregarPrerrequisito() throws SistemaAcademicoException
    {
        System.out.println("--- AGREGAR PRE-REQUISITO ---");
        mostrarMaterias();
        String codigo = Entrada.leerTexto("Materia: ");
        String prerrequisito = Entrada.leerTexto("Pre-requisito: ");
        universidad.agregarPrerrequisito(codigo, prerrequisito);
        System.out.println("Pre-requisito agregado.");
    }
    private static void mostrarPrerrequisitos() throws SistemaAcademicoException
    {
        System.out.println("--- PRE-REQUISITOS ---");
        String codigo = Entrada.leerTexto("Materia: ");
        System.out.println(universidad.buscarMateria(codigo).mostrarPrerrequisitos());
    }
    private static void inscribirEstudiante() throws SistemaAcademicoException
    {
        System.out.println("--- INSCRIBIR ESTUDIANTE ---");
        String id = Entrada.leerTexto("ID del estudiante: ");
        String codigo = Entrada.leerTexto("Materia: ");
        Materia materia = universidad.buscarMateria(codigo);
        if(universidad.inscribirEstudiante(id, codigo))
        {
            System.out.println("Inscripción exitosa en " + materia + " (cupos restantes: " + materia.getCuposDisponibles() + ")");
        }
        else
        {
            System.out.println("Materia llena. Agregado a COLA DE ESPERA (posición " + materia.getColaEspera().tamanio() + ")");
        }
    }
    private static void cancelarInscripcion() throws SistemaAcademicoException
    {
        System.out.println("--- CANCELAR INSCRIPCIÓN ---");
        String id = Entrada.leerTexto("ID del estudiante: ");
        String codigo = Entrada.leerTexto("Materia: ");
        Estudiante asignado = universidad.cancelarInscripcion(id, codigo);
        System.out.println("Cancelación exitosa. Cupo liberado.");
        if(asignado != null)
        {
            System.out.println("Asignando cupo a " + asignado.getNombre() + " (primer estudiante en cola)");
        }
    }
    private static void mostrarColaEspera() throws SistemaAcademicoException
    {
        System.out.println("--- MOSTRAR COLA DE ESPERA ---");
        String codigo = Entrada.leerTexto("Materia: ");
        System.out.println(universidad.buscarMateria(codigo).mostrarColaEspera());
    }
    private static void mostrarMaterias()
    {
        Materia[] lista = universidad.listarMaterias();
        System.out.print("Materias:");
        for(int i = 0; i < lista.length; i++)
        {
            System.out.print("  " + lista[i].getCodigo());
        }
        System.out.println();
    }
    // horarios
    private static void reservarHorario() throws SistemaAcademicoException
    {
        System.out.println("--- RESERVAR HORARIO EN AULA ---");
        mostrarAulas();
        String aula = Entrada.leerTexto("Aula: ");
        int dia = leerDia();
        int hora = Entrada.leerEntero("Hora (0-23): ", 0, 23);
        int duracion = Entrada.leerEntero("Duración (horas): ");
        String codigo = Entrada.leerTextoOpcional("Materia (Enter para ninguna): ");
        System.out.println("Verificando disponibilidad...");
        System.out.println(universidad.buscarAula(aula).verificarDisponibilidad(dia, hora, duracion));
        universidad.reservarHorario(aula, dia, hora, duracion, codigo);
        System.out.println("Reserva exitosa.");
    }
    private static void liberarHorario() throws SistemaAcademicoException
    {
        System.out.println("--- LIBERAR HORARIO ---");
        mostrarAulas();
        String aula = Entrada.leerTexto("Aula: ");
        int dia = leerDia();
        int hora = Entrada.leerEntero("Hora (0-23): ", 0, 23);
        int duracion = Entrada.leerEntero("Duración (horas): ");
        universidad.liberarHorario(aula, dia, hora, duracion);
        System.out.println("Horario liberado.");
    }
    private static void consultarDisponibilidad() throws SistemaAcademicoException
    {
        System.out.println("--- CONSULTAR DISPONIBILIDAD ---");
        mostrarAulas();
        Aula aula = universidad.buscarAula(Entrada.leerTexto("Aula: "));
        int dia = leerDia();
        int hora = Entrada.leerEntero("Hora (0-23): ", 0, 23);
        if(aula.consultarDisponibilidad(dia, hora))
        {
            System.out.println(Aula.NOMBRES_DIAS[dia] + " " + hora + ":00 -> LIBRE");
        }
        else
        {
            System.out.println(aula.verificarDisponibilidad(dia, hora, 1)); //dice por quien esta ocupado
        }
        System.out.println(aula.horarioDelDia(dia));
    }
    private static int leerDia()
    {
        return Entrada.leerEntero("Día (0=Domingo, 1=Lunes, ... 6=Sábado): ", 0, 6);
    }
    private static void mostrarAulas()
    {
        Aula[] lista = universidad.listarAulas();
        System.out.print("Aulas:");
        for(int i = 0; i < lista.length; i++)
        {
            System.out.print("  " + lista[i].getNombre());
        }
        System.out.println();
    }

    // rutas

    private static void agregarConexion() throws SistemaAcademicoException
    {
        System.out.println("--- AGREGAR CONEXIÓN ENTRE EDIFICIOS ---");
        Campus campus = universidad.getCampus();
        System.out.println(campus.listarEdificios());
        int origen = leerEdificio(campus, "Edificio A: ");
        int destino = leerEdificio(campus, "Edificio B: ");
        int metros = Entrada.leerEntero("Distancia en metros: ");
        campus.agregarConexion(origen, destino, metros);
        System.out.println("Conexión agregada: " + campus.getNombreEdificio(origen) + " <-> " + campus.getNombreEdificio(destino) + " (" + metros + "m)");
    }
    private static void calcularRuta() throws SistemaAcademicoException
    {
        System.out.println("--- CALCULAR RUTA MÁS CORTA ---");
        Campus campus = universidad.getCampus();
        System.out.println(campus.listarEdificios());
        int origen = leerEdificio(campus, "Origen: ");
        int destino = leerEdificio(campus, "Destino: ");
        System.out.println("--- RESULTADO ---");
        System.out.println(campus.calcularRutaMasCorta(origen, destino));
    }
    //acepta el numero o el nombre del edificio
    private static int leerEdificio(Campus campus, String mensaje)
    {
        while(true)
        {
            String texto = Entrada.leerTexto(mensaje);
            try
            {
                int numero = Integer.parseInt(texto);
                if(numero >= 0 && numero < campus.getCantidadEdificios())
                {
                    return numero;
                }
            }
            catch(NumberFormatException e)
            {
                int indice = campus.buscarEdificio(texto);
                if(indice != -1)
                {
                    return indice;
                }
            }
            System.out.println("No existe ese edificio, escriba el número o el nombre.");
        }
    }
}
