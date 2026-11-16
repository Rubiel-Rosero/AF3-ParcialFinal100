package modelo;

import estructuras.Pila;
import excepciones.DatoInvalidoException;

/**
el mapa del campus como grafo no dirigido
- nombres: arreglo nativo String[] con los edificios (el indice es el numero del edificio)
- distancias: matriz de adyacencia int[N][N]
  distancias[a][b] = metros entre a y b, 0 = no hay camino directo
  es simetrica (distancias[a][b] == distancias[b][a]) porque el camino sirve en los dos sentidos
la ruta mas corta se calcula con dijkstra
*/
public class Campus
{
    public static final int MAX_EDIFICIOS = 10;
    private static final int INFINITO = Integer.MAX_VALUE;
    private String[] nombres;
    private int[][] distancias;
    private int cantidadEdificios = 0;
    //sobrecarga: sin parametros usa 10 edificios como maximo
    public Campus()
    {
        nombres = new String[MAX_EDIFICIOS];
        distancias = new int[MAX_EDIFICIOS][MAX_EDIFICIOS];
    }
    public Campus(int maxEdificios) throws DatoInvalidoException
    {
        if(maxEdificios < 2)
        {
            throw new DatoInvalidoException("El campus debe permitir al menos 2 edificios");
        }
        nombres = new String[maxEdificios];
        distancias = new int[maxEdificios][maxEdificios]; //java la llena de 0: sin caminos
    }

    // edificios

    //se guarda en la siguiente casilla libre, devuelve el numero que le toco
    public int agregarEdificio(String nombre) throws DatoInvalidoException
    {
        if(nombre == null || nombre.trim().isEmpty())
        {
            throw new DatoInvalidoException("El nombre del edificio no puede estar vacío");
        }
        if(cantidadEdificios == nombres.length)
        {
            throw new DatoInvalidoException("El campus ya tiene " + nombres.length + " edificios, el arreglo está lleno");
        }
        if(buscarEdificio(nombre) != -1)
        {
            throw new DatoInvalidoException("Ya existe el edificio " + nombre.trim());
        }
        nombres[cantidadEdificios] = nombre.trim();
        cantidadEdificios++;
        return cantidadEdificios - 1;
    }
    //recorre el arreglo, -1 si no esta
    public int buscarEdificio(String nombre)
    {
        for(int i = 0; i < cantidadEdificios; i++)
        {
            if(nombres[i].equalsIgnoreCase(nombre.trim()))
            {
                return i;
            }
        }
        return -1;
    }
    public String listarEdificios()
    {
        String texto = "Edificios registrados:";
        for(int i = 0; i < cantidadEdificios; i++)
        {
            texto += "\n" + i + ": " + nombres[i];
        }
        return texto;
    }

    // conexiones

    //se escribe en las dos casillas para que la matriz quede simetrica
    public void agregarConexion(int a, int b, int metros) throws DatoInvalidoException
    {
        validarEdificio(a);
        validarEdificio(b);
        if(a == b)
        {
            throw new DatoInvalidoException("Un edificio no se puede conectar consigo mismo");
        }
        if(metros <= 0)
        {
            throw new DatoInvalidoException("La distancia debe ser mayor a 0 metros (dijkstra no sirve con negativos)");
        }
        distancias[a][b] = metros;
        distancias[b][a] = metros;
    }
    //sobrecarga: lo mismo pero con los nombres
    public void agregarConexion(String edificioA, String edificioB, int metros) throws DatoInvalidoException
    {
        agregarConexion(indiceDe(edificioA), indiceDe(edificioB), metros);
    }
    public void eliminarConexion(int a, int b) throws DatoInvalidoException
    {
        validarEdificio(a);
        validarEdificio(b);
        if(distancias[a][b] == 0)
        {
            throw new DatoInvalidoException("No hay conexión entre " + nombres[a] + " y " + nombres[b]);
        }
        distancias[a][b] = 0;
        distancias[b][a] = 0;
    }
    public int getDistanciaDirecta(int a, int b) throws DatoInvalidoException
    {
        validarEdificio(a);
        validarEdificio(b);
        return distancias[a][b];
    }

    // dijkstra

    public Ruta calcularRutaMasCorta(String origen, String destino) throws DatoInvalidoException
    {
        return calcularRutaMasCorta(indiceDe(origen), indiceDe(destino));
    }
    public Ruta calcularRutaMasCorta(int origen, int destino) throws DatoInvalidoException
    {
        validarEdificio(origen);
        validarEdificio(destino);
        int n = cantidadEdificios;
        int[] distancia = new int[n];
        boolean[] visitado = new boolean[n];
        int[] anterior = new int[n];
        //1. todas en infinito porque aun no se sabe llegar a ninguna, el origen en 0
        for(int i = 0; i < n; i++)
        {
            distancia[i] = INFINITO;
            anterior[i] = -1;
        }
        distancia[origen] = 0;
        //2. en cada vuelta se confirma un edificio
        for(int vuelta = 0; vuelta < n; vuelta++)
        {
            int u = masCercanoSinVisitar(distancia, visitado);
            if(u == -1)
            {
                break; //lo que queda no se puede alcanzar
            }
            visitado[u] = true;
            if(u == destino)
            {
                break; //el destino ya es definitivo, no hace falta seguir
            }
            //3. relajar: si pasar por u es mas corto para llegar a v, se actualiza
            for(int v = 0; v < n; v++)
            {
                if(distancias[u][v] > 0 && !visitado[v] && distancia[u] + distancias[u][v] < distancia[v])
                {
                    distancia[v] = distancia[u] + distancias[u][v];
                    anterior[v] = u;
                }
            }
        }
        if(distancia[destino] == INFINITO)
        {
            throw new DatoInvalidoException("No hay ruta entre " + nombres[origen] + " y " + nombres[destino]);
        }
        return armarRuta(anterior, destino, distancia[destino]);
    }
    //el no visitado con menor distancia, -1 si todos los que quedan estan en infinito
    //como u nunca esta en infinito, distancia[u] + metros no se desborda
    private int masCercanoSinVisitar(int[] distancia, boolean[] visitado)
    {
        int mejor = -1;
        for(int i = 0; i < distancia.length; i++)
        {
            if(!visitado[i] && distancia[i] != INFINITO && (mejor == -1 || distancia[i] < distancia[mejor]))
            {
                mejor = i;
            }
        }
        return mejor;
    }
    //anterior[] lleva del destino hacia atras: se apila y al desapilar sale en el orden correcto
    private Ruta armarRuta(int[] anterior, int destino, int total)
    {
        Pila<Integer> pila = new Pila<>();
        int actual = destino;
        while(actual != -1)
        {
            pila.apilar(actual);
            actual = anterior[actual];
        }
        int cantidad = pila.tamanio();
        String[] edificios = new String[cantidad];
        int[] tramos = new int[cantidad - 1];
        int previo = -1;
        for(int i = 0; i < cantidad; i++)
        {
            int edificio = pila.desapilar();
            edificios[i] = nombres[edificio];
            if(previo != -1)
            {
                tramos[i - 1] = distancias[previo][edificio];
            }
            previo = edificio;
        }
        return new Ruta(edificios, tramos, total);
    }

    // apoyo

    //la matriz completa como tabla (recorrido con dos for)
    public String mostrarMatriz()
    {
        String texto = String.format("%-14s", "");
        for(int j = 0; j < cantidadEdificios; j++)
        {
            texto += String.format("%6d", j);
        }
        for(int i = 0; i < cantidadEdificios; i++)
        {
            texto += "\n" + String.format("%-14s", i + " " + nombres[i]);
            for(int j = 0; j < cantidadEdificios; j++)
            {
                texto += String.format("%6d", distancias[i][j]);
            }
        }
        return texto;
    }
    //los 5 edificios del ejemplo 5 y sus caminos
    public void cargarCampusPorDefecto() throws DatoInvalidoException
    {
        agregarEdificio("Ingenieria");
        agregarEdificio("Biblioteca");
        agregarEdificio("Cafeteria");
        agregarEdificio("Rectoria");
        agregarEdificio("Laboratorios");
        agregarConexion(0, 1, 200);
        agregarConexion(0, 2, 150);
        agregarConexion(0, 4, 300);
        agregarConexion(1, 2, 100);
        agregarConexion(1, 3, 250);
        agregarConexion(2, 3, 180);
        agregarConexion(2, 4, 120);
        agregarConexion(3, 4, 220);
    }
    private int indiceDe(String nombre) throws DatoInvalidoException
    {
        int indice = buscarEdificio(nombre);
        if(indice == -1)
        {
            throw new DatoInvalidoException("No existe el edificio " + nombre.trim());
        }
        return indice;
    }
    private void validarEdificio(int indice) throws DatoInvalidoException
    {
        if(indice < 0 || indice >= cantidadEdificios)
        {
            throw new DatoInvalidoException("El edificio debe estar entre 0 y " + (cantidadEdificios - 1));
        }
    }
    public String getNombreEdificio(int indice) throws DatoInvalidoException
    {
        validarEdificio(indice);
        return nombres[indice];
    }
    public int getCantidadEdificios()
    {
        return cantidadEdificios;
    }
}
