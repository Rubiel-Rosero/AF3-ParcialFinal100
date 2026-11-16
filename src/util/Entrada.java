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
            System.out.println("   No puede quedar vacío, intente de nuevo.");
        }
    }
    //texto que si puede quedar vacio (para datos opcionales)
    public static String leerTextoOpcional(String mensaje)
    {
        System.out.print(mensaje);
        return sc.nextLine().trim();
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
                System.out.println("   \"" + texto + "\" no es un número entero, intente de nuevo.");
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
    //acepta 3.5 o 3,5
    public static double leerDecimal(String mensaje)
    {
        while(true)
        {
            String texto = leerTexto(mensaje).replace(',', '.');
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
    public static boolean confirmar(String mensaje)
    {
        while(true)
        {
            String texto = leerTexto(mensaje + " (s/n): ").toLowerCase();
            if(texto.equals("s") || texto.equals("si"))
            {
                return true;
            }
            if(texto.equals("n") || texto.equals("no"))
            {
                return false;
            }
            System.out.println("   Responda s o n.");
        }
    }
}
