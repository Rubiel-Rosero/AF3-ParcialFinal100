package modelo;

import excepciones.DatoInvalidoException;
import excepciones.HorarioConflictivoException;

/**
un aula con su horario semanal
- horario: matriz boolean[7][24], fila = dia (0 domingo ... 6 sabado), columna = hora (0 a 23)
  true = ocupada, false = libre. al crearla Java la llena de false: el aula arranca toda libre
- reservadoPor: matriz paralela String[7][24] con el codigo de la materia que ocupa cada hora
  (la misma idea de codigosMaterias en Estudiante)
*/
public class Aula
{
    public static final int DIAS = 7;
    public static final int HORAS = 24;
    public static final String[] NOMBRES_DIAS = { "Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado" };
    private String nombre;
    private int capacidad;
    private boolean[][] horario = new boolean[DIAS][HORAS];
    private String[][] reservadoPor = new String[DIAS][HORAS];
    public Aula(String nombre, int capacidad) throws DatoInvalidoException
    {
        if(nombre == null || nombre.trim().isEmpty())
        {
            throw new DatoInvalidoException("El nombre del aula no puede estar vacío");
        }
        if(capacidad <= 0)
        {
            throw new DatoInvalidoException("La capacidad debe ser mayor a 0");
        }
        this.nombre = nombre.trim();
        this.capacidad = capacidad;
    }
    //una sola casilla: O(1)
    public boolean consultarDisponibilidad(int dia, int hora) throws DatoInvalidoException
    {
        validarCasilla(dia, hora);
        return !horario[dia][hora];
    }
    //sin codigo de materia (como lo pide el enunciado)
    public void reservar(int dia, int hora, int duracion) throws DatoInvalidoException, HorarioConflictivoException
    {
        reservar(dia, hora, duracion, "-");
    }
    //sobrecarga: con el codigo de la materia que va a ocupar el aula
    //primero se REVISAN todas las horas y despues se MARCAN todas
    //si se marcara mientras se revisa, un choque en la segunda hora dejaria la primera reservada a medias
    public void reservar(int dia, int hora, int duracion, String codigoMateria) throws DatoInvalidoException, HorarioConflictivoException
    {
        validarRango(dia, hora, duracion);
        for(int h = hora; h < hora + duracion; h++)
        {
            if(horario[dia][h])
            {
                throw new HorarioConflictivoException(NOMBRES_DIAS[dia] + " " + h + ":00 ya está reservado por " + reservadoPor[dia][h]);
            }
        }
        for(int h = hora; h < hora + duracion; h++)
        {
            horario[dia][h] = true;
            reservadoPor[dia][h] = codigoMateria;
        }
    }
    //liberar: igual, primero se revisa que todas esten ocupadas y despues se liberan
    public void liberar(int dia, int hora, int duracion) throws DatoInvalidoException
    {
        validarRango(dia, hora, duracion);
        for(int h = hora; h < hora + duracion; h++)
        {
            if(!horario[dia][h])
            {
                throw new DatoInvalidoException(NOMBRES_DIAS[dia] + " " + h + ":00 no está reservado");
            }
        }
        for(int h = hora; h < hora + duracion; h++)
        {
            horario[dia][h] = false;
            reservadoPor[dia][h] = null;
        }
    }
    //mover una clase a otro dia/hora (lo va a usar deshacer en el bloque 8)
    //si el nuevo horario choca, se vuelve a dejar el viejo como estaba
    public void cambiarHorario(int dia, int hora, int duracion, int nuevoDia, int nuevaHora) throws DatoInvalidoException, HorarioConflictivoException
    {
        String codigo = quienOcupa(dia, hora);
        liberar(dia, hora, duracion);
        try
        {
            reservar(nuevoDia, nuevaHora, duracion, codigo);
        }
        catch(HorarioConflictivoException e)
        {
            reservar(dia, hora, duracion, codigo); //vuelve a dejar el horario viejo
            throw e;
        }
        catch(DatoInvalidoException e)
        {
            reservar(dia, hora, duracion, codigo);
            throw e;
        }
    }
    //texto como en el ejemplo 4: "Lunes 8:00 -> LIBRE"
    public String verificarDisponibilidad(int dia, int hora, int duracion) throws DatoInvalidoException
    {
        validarRango(dia, hora, duracion);
        String texto = "";
        for(int h = hora; h < hora + duracion; h++)
        {
            texto += NOMBRES_DIAS[dia] + " " + h + ":00 -> " + (horario[dia][h] ? "OCUPADO (" + reservadoPor[dia][h] + ")" : "LIBRE") + "\n";
        }
        return texto.trim();
    }
    //recorre una fila de la matriz: las 24 horas de un dia
    public String horarioDelDia(int dia) throws DatoInvalidoException
    {
        validarCasilla(dia, 0);
        String texto = nombre + " - " + NOMBRES_DIAS[dia] + ":";
        boolean hayReservas = false;
        for(int h = 0; h < HORAS; h++)
        {
            if(horario[dia][h])
            {
                texto += "\n  " + h + ":00 - " + (h + 1) + ":00  " + reservadoPor[dia][h];
                hayReservas = true;
            }
        }
        return hayReservas ? texto : texto + " todo el día libre";
    }
    //recorre toda la matriz con dos for: cuantas horas tiene ocupadas en la semana
    public int horasOcupadasEnLaSemana()
    {
        int total = 0;
        for(int d = 0; d < horario.length; d++)
        {
            for(int h = 0; h < horario[d].length; h++)
            {
                if(horario[d][h])
                {
                    total++;
                }
            }
        }
        return total;
    }
    public String quienOcupa(int dia, int hora) throws DatoInvalidoException
    {
        validarCasilla(dia, hora);
        return reservadoPor[dia][hora];
    }
    private void validarCasilla(int dia, int hora) throws DatoInvalidoException
    {
        if(dia < 0 || dia >= DIAS)
        {
            throw new DatoInvalidoException("El día debe estar entre 0 (Domingo) y 6 (Sábado)");
        }
        if(hora < 0 || hora >= HORAS)
        {
            throw new DatoInvalidoException("La hora debe estar entre 0 y 23");
        }
    }
    //que el bloque completo quepa en el dia: no se pasa de medianoche
    private void validarRango(int dia, int hora, int duracion) throws DatoInvalidoException
    {
        validarCasilla(dia, hora);
        if(duracion < 1)
        {
            throw new DatoInvalidoException("La duración debe ser de al menos 1 hora");
        }
        if(hora + duracion > HORAS)
        {
            throw new DatoInvalidoException("La reserva se pasa de la medianoche (" + hora + ":00 + " + duracion + " horas)");
        }
    }
    public String getNombre()
    {
        return nombre;
    }
    public int getCapacidad()
    {
        return capacidad;
    }
    @Override
    public String toString()
    {
        return nombre + " (capacidad " + capacidad + ")";
    }
}
