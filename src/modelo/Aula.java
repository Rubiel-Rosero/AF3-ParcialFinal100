package modelo;

import excepciones.DatoInvalidoException;
import excepciones.HorarioConflictivoException;

/**
una aula con su horario semanal
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
    //una sola casilla: O(1) osea constante, no depende de la cantidad de dias ni horas
    public boolean consultarDisponibilidad(int dia, int hora) throws DatoInvalidoException
    {
        validarCasilla(dia, hora);
        return !horario[dia][hora];
    }
    //sin codigo de materia
    public void reservar(int dia, int hora, int duracion) throws DatoInvalidoException, HorarioConflictivoException
    {
        reservar(dia, hora, duracion, "-");
    }
    //sobrecarga: con el codigo de la materia que va a ocupar el aula
    public void reservar(int dia, int hora, int duracion, String codigoMateria) throws DatoInvalidoException, HorarioConflictivoException
    {
        validarRango(dia, hora, duracion);
        for(int h = hora; h < hora + duracion; h++)
        {
            if(horario[dia][h])
            {
                throw new HorarioConflictivoException(NOMBRES_DIAS[dia] + " " + h + ":00 ya está reservado" + porQuien(dia, h));
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
    //mover una clase a otro dia/hora
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
    public String verificarDisponibilidad(int dia, int hora, int duracion) throws DatoInvalidoException
    {
        validarRango(dia, hora, duracion);
        String texto = "";
        for(int h = hora; h < hora + duracion; h++)
        {
            if(horario[dia][h])
            {
                texto += NOMBRES_DIAS[dia] + " " + h + ":00 -> OCUPADO" + porQuien(dia, h) + "\n";
            }
            else
            {
                texto += NOMBRES_DIAS[dia] + " " + h + ":00 -> LIBRE\n";
            }
        }
        return texto.trim();
    }
    //recorre una fila de la matriz osea las 24 horas de un dia
    public String horarioDelDia(int dia) throws DatoInvalidoException
    {
        validarCasilla(dia, 0);
        String texto = nombre + " - " + NOMBRES_DIAS[dia] + ":";
        boolean hayReservas = false;
        for(int h = 0; h < HORAS; h++)
        {
            if(horario[dia][h])
            {
                if("-".equals(reservadoPor[dia][h]))
                {
                    texto += "\n  " + h + ":00 - " + (h + 1) + ":00  (sin materia)";
                }
                else
                {
                    texto += "\n  " + h + ":00 - " + (h + 1) + ":00  " + reservadoPor[dia][h];
                }
                hayReservas = true;
            }
        }
        if(!hayReservas)
        {
            return texto + " todo el día libre";
        }
        return texto;
    }
    //recorre toda la matriz con dos for osea cuantas horas tiene ocupadas en la semana
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
    //"por CALC101" o nada si se reservo sin materia
    private String porQuien(int dia, int hora)
    {
        if("-".equals(reservadoPor[dia][hora]))
        {
            return "";
        }
        return " por " + reservadoPor[dia][hora];
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
    //que el bloque completo quepa en el dia que no se pasa de medianoche
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
