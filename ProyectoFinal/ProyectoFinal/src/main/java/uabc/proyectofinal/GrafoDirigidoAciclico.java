/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uabc.proyectofinal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *  Clase que crea, modifica y representa a un grafo dirigido aciclico
 * @author Dell
 */
public class GrafoDirigidoAciclico {

    private Map<String, List<String>> listaAdyacencia;
    private int numVertices;
    private String[] claves;
    int[][] matriz;
    private String nombre;
    private String fechaHora;

    // Constructor que recibe un argumento n que define los vértices numerados o aleatorios
    //aleatorio indica si los datos sera aleatorios o no
    //aristas indica si se agregara aristas aleatorioas o no
    public GrafoDirigidoAciclico(int n, boolean aleatorio, boolean aristas) {
        //Si el numero de vertices es menor que cero lanza una excepcion
        if (n <= 0) {
            throw new IllegalArgumentException("El número de vertices debe ser mayor que cero.");
        }

        //Inicializa las variables para crear el grafo
        this.numVertices = n;
        this.listaAdyacencia = new HashMap<>();
        Random random = new Random();
        Set<String> verticesGenerados = new HashSet<>();

        // Si aleatorio es true, generamos vértices aleatorios (números o letras)
        if (aleatorio) {
            while (verticesGenerados.size() < n) {
                if (random.nextBoolean()) {
                    // Generar un vértice numérico aleatorio
                    int vertice = random.nextInt(101); // Números aleatorios entre 0 y 100
                    verticesGenerados.add(String.valueOf(vertice)); // Convertir a String para almacenarlo
                } else {
                    // Generar un vértice alfabético aleatorio (letras)
                    char vertice = (char) (random.nextInt(26) + 'A'); // Letras aleatorias de A a Z
                    verticesGenerados.add(String.valueOf(vertice)); // Convertir a String
                }
            }
        } else {
            // Si aleatorio es false, los numeramos de 0 a n-1
            for (int i = 0; i < n; i++) {
                verticesGenerados.add(String.valueOf(i));
            }
        }

        // Inicializar la lista de adyacencia con los vértices generados
        for (String vertice : verticesGenerados) {
            listaAdyacencia.put(vertice, new ArrayList<>());
        }

        // Convertir el conjunto de vértices en un array para mejor acceso
        claves = listaAdyacencia.keySet().toArray(new String[0]);
        setFechayHora(); //Guardar la fecha y hora de la creacion del grafo

        if (aristas) {
            // Generar un número aleatorio de aristas entre 1 y el máximo posible sin ciclos
            int maxAristas = n * (n - 1) / 2; // Número máximo de aristas posibles en un grafo dirigido acíclico simple
            int numAristas = random.nextInt(maxAristas) + 1; 

            for (int k = 0; k < numAristas; k++) {
                // Seleccionar dos vértices diferentes al azar
                String origen, destino;
                do {
                    origen = claves[random.nextInt(numVertices)];
                    destino = claves[random.nextInt(numVertices)];
                } while (origen.equals(destino) || listaAdyacencia.get(origen).contains(destino));

                // Intentar agregar la arista sin causar ciclos
                if (!insertarArista(origen, destino)) {
                    k--; // Si no se pudo agregar por ciclo, intentar de nuevo
                }
            }
        }
    }

    // Constructor vacío que genera 4 vértices con valores aleatorios
    public GrafoDirigidoAciclico() {
        this.numVertices = 4;
        this.listaAdyacencia = new HashMap<>();
        Random random = new Random();
        Set<String> verticesGenerados = new HashSet<>();

        // Generar 4 números únicos aleatorios como vértices
        while (verticesGenerados.size() < numVertices) {
            int vertice = random.nextInt(101); // Números aleatorios entre 0 y 100
            verticesGenerados.add(String.valueOf(vertice)); // Garantizar no repetidos
        }

        // Inicializar la lista de adyacencia con los vértices generados
        for (String vertice : verticesGenerados) {
            listaAdyacencia.put(vertice, new ArrayList<>());
        }

        //Lo mismo que el constructor anterior
        claves = listaAdyacencia.keySet().toArray(new String[0]);
        setFechayHora();
    }

    // Constructor que recibe un numero de vertices y genera el grafo sin aristas
    public GrafoDirigidoAciclico(int n) {
        //Verifica si n es mayor a cero para crear el grafo
        if (n <= 0) {
            throw new IllegalArgumentException("El número de vertices debe ser mayor que cero.");
        }
        this.numVertices = n;
        this.listaAdyacencia = new HashMap<>();
        //genera los valores de los vertices
        for (int i = 0; i < n; i++) {
            listaAdyacencia.put(String.valueOf(i), new ArrayList<>());
        }
        //Lo mismo de los constructores anteriores
        claves = listaAdyacencia.keySet().toArray(new String[0]);
        setFechayHora();
    }

    //Constructor que recibe los valores de la clase Archivador y crea un grafo
    public GrafoDirigidoAciclico(Map<String, List<String>> listaAdyacencia, int numVertices, String nombre, String fechaHora) {
        this.listaAdyacencia = listaAdyacencia;
        this.numVertices = numVertices;
        this.matriz = new int[numVertices][numVertices];
        this.nombre = nombre;
        this.fechaHora = fechaHora;
        claves = listaAdyacencia.keySet().toArray(new String[0]);
    }

    //Metodo para crear la lista de adyacencia desde la matriz de adyacencia
    public void crearListaAdyacenciaDesdeMatriz() {
        // Asegurarse de que la matriz esté inicializada
        if (matriz == null) {
            throw new IllegalStateException("La matriz de adyacencia no está inicializada.");
        }

        // Inicializamos la lista de adyacencia
        listaAdyacencia.clear(); // Limpiar cualquier lista previa
        for (int i = 0; i < numVertices; i++) {
            String verticeOrigen = claves[i];
            listaAdyacencia.put(verticeOrigen, new ArrayList<>());
        }

        // Recorremos la matriz de adyacencia para construir la lista
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                // Si hay una arista (matriz[i][j] == 1), agregar el vértice de destino a la lista de adyacencia
                if (matriz[i][j] == 1) {
                    String verticeOrigen = claves[i];
                    String verticeDestino = claves[j];
                    listaAdyacencia.get(verticeOrigen).add(verticeDestino);
                }
            }
        }
    }

    //Metodo que obtiene la fecha y hora del sistema y se lo asigna al atributo de fechaHora del grafo
    private void setFechayHora() {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        fechaHora = fechaHoraActual.format(formateador);
    }

    //Metodo para agregar un vertice
    public void crearVertice(String valor) {
        //Verifica si el vertice tiene algun valor
        if (valor == null || valor.isEmpty()) {
            System.out.println("El valor del vértice no puede estar vacío.");
            return;
        }

        // Agregar el vértice al grafo con su lista de adyacencia vacía
        listaAdyacencia.put(valor, new ArrayList<>());
        numVertices++;
        claves = listaAdyacencia.keySet().toArray(new String[0]);
    }

    //Metodo que obtiene el Grado de entrada del vertice ingresado
    public int gradoDeEntrada(String i) {
        validarVertice(i);
        int grado = 0; //Inicia el grado como 0
        for (List<String> adyacentes : listaAdyacencia.values()) { //Busca vertices adyacentes
            if (adyacentes.contains(i)) {
                grado++;
            }
        }
        return grado;
    }

    //Metodo que obtiene el Grado de salida del vertice ingresado
    public int gradoDeSalida(String i) {
        validarVertice(i);
        return listaAdyacencia.get(i).size(); //Regresa el tamaño de la lista de adyacencia
    }

    //Metodo que obtiene el Número de aristas del grafo
    public int cuantasAristasHay() {
        int count = 0;
        for (List<String> adyacentes : listaAdyacencia.values()) {
            count += adyacentes.size();
        }
        return count;
    }

    //Metodo que Verifica si hay arista entre i y j
    public boolean adyacente(String i, String j) {
        validarVertice(i);
        validarVertice(j);
        return listaAdyacencia.get(i).contains(j);//Si i contiene a j
    }

    //Metodo que Verifica si hay conexión directa entre i y j
    public boolean conectados(String i, String j) {
        validarVertice(i);
        validarVertice(j);

        //Si son iguales significa que es el mmismo y no es una conexion
        if (i.equals(j)) {
            return false;
        }
        
        //inicializar las variables para comprobar las conexiones
        Queue<String> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();
        cola.add(i);
        visitados.add(i);
        //Mientras la cola no este vacia recore la cola
        while (!cola.isEmpty()) {
            String actual = cola.poll();//Regresa el ultimo valor de la cola
            if (actual.equals(j)) {//Si actual y j son iguales significa que hay conexion
                return true;
            }
            List<String> vecinos = listaAdyacencia.get(actual); //Crea una lista de adyacencia para los vecinos del vertice actual
            if (vecinos != null) { //Recorrer la lista de vecinos mientras no este vacia
                for (String vecino : vecinos) {
                    //Si vecino no se encuentra en los vertices visitados lo agregas a las lista de visitados y a la cola
                    if (!visitados.contains(vecino)) {
                        visitados.add(vecino);
                        cola.add(vecino);
                    }
                }
            }
        }
        return false; //Regresa false si no hay conexion
    }

    //Metodo para realizar Ordenamiento topológico regresa un String
    public String topologicalSort() {
        //Si el grafo tiene ciclos no se puede realizar
        if (tieneCiclos()) {
            return "No se puede ordenar porque es un grafo ciclico.";
        }
        //Crear la matriz de adyacencia
        mostrarEstructura();
        //Si el grafo no tiene aristas no se puede realizar
        if (!tieneAristas()) {
            return "El grafo no tiene aristas";
        }
        //Crear un map para el grado de entrada de cada vertice estableciendo en -1
        Map<String, Integer> gradoEntrada = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            gradoEntrada.put(claves[i], -1);
        }
        //Agregar los grados de entrada a cada vertice
        for (int i = 0; i < numVertices; i++) {
            gradoEntrada.put(claves[i], gradoDeEntrada(claves[i]));
        }
        
        //Crea una lista de prioridad para 
        PriorityQueue<String> cola = new PriorityQueue<>();
        for (String clave : gradoEntrada.keySet()) {
            if (gradoEntrada.get(clave) == 0 && !listaAdyacencia.get(clave).isEmpty()) {
                cola.add(clave);
            }
        }
        //Crea una lista ordenada
        List<String> orden = new ArrayList<>();
        while (!cola.isEmpty()) {
            String actual = cola.poll();
            orden.add(actual);
            for (String vecino : listaAdyacencia.get(actual)) {
                if (gradoEntrada.get(vecino) != null) {
                    gradoEntrada.put(vecino, gradoEntrada.get(vecino) - 1);
                    if (gradoEntrada.get(vecino) == 0) {
                        cola.add(vecino);
                    }
                }
            }
        }
        //Crea la string con los elementos de la lista orden separados por -
        return String.join(" - ", orden);
    }

    //Metodo para Detectar ciclos en el grafo utilizando el recorrido DFS
    public boolean tieneCiclos() {
        Set<String> visitados = new HashSet<>();
        Set<String> pila = new HashSet<>(); // Pila para el recorrido DFS

        // Recorremos todos los vértices del grafo
        for (int i = 0; i < numVertices; i++) {
            // Si el vértice no ha sido visitado, ejecutamos DFS
            if (!visitados.contains(claves[i])) {
                if (dfsCiclo(claves[i], visitados, pila)) {
                    return true;  // Se encuentra un ciclo, retornamos true
                }
            }
        }
        return false; // Si no se encuentra ciclo retorna false
    }

    // Metodo para Mostrar la estructura como matriz
    public String mostrarEstructura() {
        //Inicializar un mapa de indices
        Map<String, Integer> indiceMap = new HashMap<>();
        for (int i = 0; i < claves.length; i++) {
            indiceMap.put(claves[i], i);
        }

        //Inicializa la matriz con el tamaño de vertices
        matriz = new int[numVertices][numVertices];
        for (int i = 0; i < claves.length; i++) {
            String verticeOrigen = claves[i];
            List<String> adyacentes = listaAdyacencia.get(verticeOrigen);

            //Verifica si hay algun valor en adyacentes para continuar
            if (adyacentes != null) {
                for (String verticeDestino : adyacentes) {
                    // Verificar si el vértice destino existe en el índice
                    if (indiceMap.containsKey(verticeDestino)) {
                        matriz[indiceMap.get(verticeOrigen)][indiceMap.get(verticeDestino)] = 1;
                    }
                }
            }
        }

        //Usa un string builder para crear el string para mostrar la matriz
        StringBuilder sb = new StringBuilder("Matriz de Adyacencia:\n");
        // Agregar las claves como encabezado de columnas
        sb.append("    "); // Espacio para la columna de encabezados
        for (String clave : claves) {
            sb.append(String.format("%4s", clave));
        }
        sb.append("\n");

        // Agregar filas con claves como encabezados
        for (int i = 0; i < claves.length; i++) {
            sb.append(String.format("%4s", claves[i])); // Encabezado de fila
            for (int j = 0; j < matriz[i].length; j++) {
                sb.append(String.format("%4s", matriz[i][j]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
    
    // Método para mostrar el grafo como lista de adyacencia
    public String mostrarListaAdyacencia() {
        StringBuilder sb = new StringBuilder();
        sb.append("Lista de Adyacencia: \n");
        //Bucle para recorrer los valores en la lista de adyacencia
        for (Map.Entry<String, List<String>> entry : listaAdyacencia.entrySet()) {
            sb.append(entry.getKey());
            sb.append(" -> ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    //Metodo para validar si el vertice se encuentrra dentro de la estructura
    private void validarVertice(String i) {
        if (!listaAdyacencia.containsKey(i)) {
            throw new IllegalArgumentException("El vertice " + i + " no existe.");
        }
    }

    // Método para insertar una arista en el grafo
    public boolean insertarArista(String i, String j) {
        validarVertice(i);
        validarVertice(j);
        //Si ambos vertices son iguales no se agrega la arista para evitar ciclos
        if (i.equals(j) || listaAdyacencia.get(i).contains(j)) {
            return false;
        }
        // Inserta la arista temporalmente para verificar si causa un ciclo 
        listaAdyacencia.get(i).add(j);
        if (tieneCiclos()) {
            // Si causa un ciclo, remove la arista y retorna falso 
            listaAdyacencia.get(i).remove(j);
            return false;
        }
        // Si no causa un ciclo, retorna verdadero
        return true;
    }

    // Método para eliminar todas las aristas
    public void eliminarAristas() {
        for (List<String> adyacentes : listaAdyacencia.values()) {
            adyacentes.clear();//Elimina las adyacencias
        }
        System.out.println("Aristas eliminadas");
    }

    //Metodo de busqueda profunda para saber si el grafo esta ciclado
    private boolean dfsCiclo(String actual, Set<String> visitados, Set<String> pila) {
        // Si el vértice está en la pila, es un ciclo
        if (pila.contains(actual)) {
            return true;
        }

        // Si ya ha sido completamente visitado, no hace falta explorarlo
        if (visitados.contains(actual)) {
            return false;
        }

        // Marcamos el vértice como visitado y lo agregamos a la pila de DFS
        pila.add(actual);

        // Recorremos las adyacencias del vértice actual
        if (listaAdyacencia.containsKey(actual)) {
            for (String adyacente : listaAdyacencia.get(actual)) {
                if (dfsCiclo(adyacente, visitados, pila)) {
                    return true; // Si encontramos un ciclo en alguna adyacencia
                }
            }
        }

        // Si terminamos de explorar este vértice y no encontramos ciclos, lo sacamos de la pila
        pila.remove(actual);
        visitados.add(actual); // Marcamos como visitado

        return false; // No se detectó ciclo
    }

    //Metodo para obtener los valores de la lista de adyacencia
    //mostrando vertices y aristas
    public String obtenerValores() {
        //Utiliza string builder para crear el string
        StringBuilder sb = new StringBuilder();
        sb.append("\tVertices:\n");
        for (String vertice : listaAdyacencia.keySet()) {
            sb.append(vertice).append(", ");//imprime los vertices
        }

        sb.append("\n\tAristas:\n");
        for (Map.Entry<String, List<String>> entry : listaAdyacencia.entrySet()) {
            String origen = entry.getKey();
            for (String destino : entry.getValue()) { //Imprime las aristas entre vertices
                sb.append(origen).append(" -> ").append(destino).append(", ");
            }
        }
        return sb.toString();
    }

    //Metodo para recorrer la matriz de adyacencia y verificar si hay aristas en el grafo
    public boolean tieneAristas() {
        // Recorre la matriz de adyacencia
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != 0) {
                    return true; // Si encuentra un valor diferente de 0, hay una arista
                }
            }
        }
        return false; // No hay aristas
    }

    //Metodo que regeresa el numero de vertices
    public int getNumVertices() {
        return numVertices;
    }

    //Metodo para mostrar las claves del grafo
    public String mostrarClaves() {
        StringBuilder sb = new StringBuilder();
        for (String x : claves) {
            sb.append(x).append(",");
        }
        return sb.toString();
    }

    //Metodo que regresa el array de claves
    public String[] getClaves() {
        return claves;
    }

    //Metodo que regresa la matriz de adyacencia
    public int[][] getMatriz() {
        return matriz;
    }

    //Metodo recibe una matriz y la establece en la matriz del grafo
    public void setMatriz(int[][] matriz) {
        this.matriz = matriz;
    }

    //Metodo que regresa el nombre del grafo
    public String getNombre() {
        return nombre;
    }

    //Metodo que recibe un nombre para establecerlo en el nombre del grafo
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //Metodo que regresa la fechaHora del grafo
    public String getFechaHora() {
        return fechaHora;
    }

    //Metodo que regresa la lista de adyacencia del grafo
    public Map<String, List<String>> getListaAdyacencia() {
        return listaAdyacencia;
    }

    //Metodo que recibe una matriz de adyacencia y la establece al grafo
    public void setListaAdyacencia(Map<String, List<String>> listaAdyacencia) {
        this.listaAdyacencia = listaAdyacencia;
    }

    //Metodo que regresa la lista de vecinos de un vertice ingresado
    public List<String> obtenerVecinos(String vertice) {
        // Validar que el vértice exista en el grafo
        validarVertice(vertice);

        // Retornar la lista de adyacencia (vecinos) del vértice
        return listaAdyacencia.get(vertice);
    }
}
