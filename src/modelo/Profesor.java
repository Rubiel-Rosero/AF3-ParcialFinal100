package modelo;

import excepciones.DatoInvalidoException;

/**
profesor, el otro hijo de persona
sirve para mostrar que la herencia tiene mas de un hijo
*/
public class Profesor extends Persona
{
    private String departamento;
    private double salario;
    public Profesor(String id, String nombre, String email, String departamento, double salario) throws DatoInvalidoException
    {
        super(id, nombre, email);
        this.departamento = departamento;
        setSalario(salario);
    }
    @Override
    public String getRol()
    {
        return "Profesor";
    }
    //lo del padre mas departamento y salario
    @Override
    public String mostrarInformacion()
    {
        return super.mostrarInformacion() + "\nDepartamento: " + departamento + "\nSalario: $" + String.format("%,.0f", salario);
    }
    public String getDepartamento()
    {
        return departamento;
    }
    public void setDepartamento(String departamento)
    {
        this.departamento = departamento;
    }
    public double getSalario()
    {
        return salario;
    }
    public void setSalario(double salario) throws DatoInvalidoException
    {
        if(salario < 0)
        {
            throw new DatoInvalidoException("El salario no puede ser negativo");
        }
        this.salario = salario;
    }
}
