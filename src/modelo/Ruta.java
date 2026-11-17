package modelo;

/**
el resultado de dijkstra: por que edificios pasa, cuanto mide cada tramo y el total
se guarda en arreglos nativos porque cuando se crea ya se sabe cuantos edificios tiene
*/
public class Ruta
{
    private String[] edificios;
    private int[] tramos; //tramos[i] = metros entre edificios[i] y edificios[i + 1]
    private int distanciaTotal;
    public Ruta(String[] edificios, int[] tramos, int distanciaTotal)
    {
        this.edificios = edificios;
        this.tramos = tramos;
        this.distanciaTotal = distanciaTotal;
    }
    public String[] getEdificios()
    {
        return edificios;
    }
    public int getDistanciaTotal()
    {
        return distanciaTotal;
    }
    //como en el ejemplo 5: Ingenieria -> Cafeteria (150m) -> Rectoria (180m)
    public String getCamino()
    {
        String texto = edificios[0];
        for(int i = 1; i < edificios.length; i++)
        {
            texto += " -> " + edificios[i] + " (" + tramos[i - 1] + "m)";
        }
        return texto;
    }
    @Override
    public String toString()
    {
        return "Ruta más corta:\n" + getCamino() + "\nDistancia TOTAL: " + distanciaTotal + " metros";
    }
}
