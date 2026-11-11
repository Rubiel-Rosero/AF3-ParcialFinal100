package modelo;

import excepciones.DatoInvalidoException;

/**
clase padre de estudiante y profesor
es abstracta porque no existe una "persona" suelta en el sistema,
siempre es un estudiante o un profesor
*/
public abstract class Persona
{
    private String id;
    private String nombre;
    private String email;
    public Persona(String id, String nombre, String email) throws DatoInvalidoException
    {
        if(id == null || id.trim().isEmpty())
        {
            throw new DatoInvalidoException("El ID no puede estar vacío");
        }
        this.id = id.trim();
        setNombre(nombre);
        setEmail(email);
    }
    //cada hijo dice que es (Estudiante o Profesor)
    public abstract String getRol();
    //los datos que tienen todos, los hijos le agregan lo suyo
    public String mostrarInformacion()
    {
        return "ID: " + id + "\nNombre: " + nombre + "\nEmail: " + email;
    }
    public String getId()
    {
        return id;
    }
    public String getNombre()
    {
        return nombre;
    }
    public void setNombre(String nombre) throws DatoInvalidoException
    {
        if(nombre == null || nombre.trim().isEmpty())
        {
            throw new DatoInvalidoException("El nombre no puede estar vacío");
        }
        this.nombre = nombre.trim();
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email) throws DatoInvalidoException
    {
        if(email == null || !email.contains("@"))
        {
            throw new DatoInvalidoException("El email debe tener @");
        }
        this.email = email.trim();
    }
    @Override
    public String toString()
    {
        return nombre;
    }
}
