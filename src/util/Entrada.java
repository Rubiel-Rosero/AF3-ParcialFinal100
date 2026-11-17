package util;
import java.util.Scanner;
public class Entrada
{
    private static Scanner sc = new Scanner(System.in);

    //texto que no puede quedar vacio
    public static String leerTexto(String mensaje)
    {
        while(true)
        {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if(!texto.isEmpty())
            {
                return texto;
            }
            System.out.println("No puede quedar vacío, intente de nuevo.");
        }
    }
    public static int leerEntero(String mensaje)
    {
        while(true)
        {
            String texto = leerTexto(mensaje);
            try
            {
                return Integer.parseInt(texto);
            }
            catch(NumberFormatException e)
            {
                System.out.println("\"" + texto + "\" no es un número entero, intente de nuevo.");
            }
        }
    }
    //sobrecarga: con rango, vuelve a preguntar si se sale
    public static int leerEntero(String mensaje, int minimo, int maximo)
    {
        while(true)
        {
            int numero = leerEntero(mensaje);
            if(numero >= minimo && numero <= maximo)
            {
                return numero;
            }
            System.out.println("   Debe estar entre " + minimo + " y " + maximo + ", intente de nuevo.");
        }
    }
    public static double leerDecimal(String mensaje)
    {
        while(true)
        {
            String texto = leerTexto(mensaje);
            try
            {
                return Double.parseDouble(texto);
            }
            catch(NumberFormatException e)
            {
                System.out.println("   \"" + texto + "\" no es un número, intente de nuevo.");
            }
        }
    }
}
