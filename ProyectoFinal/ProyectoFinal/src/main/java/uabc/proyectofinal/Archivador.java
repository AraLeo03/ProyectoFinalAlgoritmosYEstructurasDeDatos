/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uabc.proyectofinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Clase para leer y escribir los grafos en archivos
 * @author Dell
 */
public class Archivador {

    private static final String RUTA = "src\\main\\java\\files\\";//Atributo Ruta donde se encuentran y encontraran los archivos

    //Metodo que recibe un grafo dirigido aciclico y el nombre del archivo para guardarlo en un archivo de texto plano
    public static void guardarGrafo(GrafoDirigidoAciclico grafo, String nombreArchivo) {
        //Copio la lista de adyacencia del grafo ingresado
        Map<String, List<String>> listaAdyacencia = grafo.getListaAdyacencia();
        //Obtengo la fecha y la hora del grafo
        String fechahora = grafo.getFechaHora();
        String nombre = grafo.getNombre();

        //Crear la ruta del archivo y el archivo
        String archivoNombre = RUTA + nombreArchivo + ".txt";
        File archivo = new File(archivoNombre);

        // Verificar si ya existe un grafo con el mismo nombre en el archivo
        if (existeGrafoEnArchivo(nombre, archivoNombre)) {
            System.out.println("Ya existe un grafo con ese nombre en el archivo.");
            return;  // Salir sin guardar el grafo
        }

        // Verifica si el archivo existe, y si no, lo crea
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }

            // Usar un BufferedWriter para escribir en el archivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
                writer.write("# Inicio de Grafo\n");
                // Escribimos la fecha y hora, y el nombre del grafo
                writer.write("Nombre del grafo: " + nombre + "\n");
                writer.write("Fecha y hora: " + fechahora + "\n");

                // Escribimos la lista de adyacencia
                writer.write("Lista de Adyacencia:\n");
                for (Map.Entry<String, List<String>> entry : listaAdyacencia.entrySet()) {
                    String vertice = entry.getKey();
                    List<String> adyacentes = entry.getValue();
                    writer.write(vertice + " -> ");
                    for (String adyacente : adyacentes) {
                        writer.write(adyacente + " ");
                    }
                    writer.write("\n");
                }

                // Se agrega una línea en blanco al final para separar los registros si se guarda otro grafo
                writer.write("# Fin de Grafo \n\n");

                // Mensaje de confirmación
                System.out.println("El grafo '" + nombre + "' ha sido guardado correctamente en " + RUTA);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo que recibe el nombre de un archivo, busca el archivo y carga el primer grafo que encuentra
    public static GrafoDirigidoAciclico cargarGrafo(String nombreArchivo) {
        //Crea la ruta para buscar el archivo
        String archivoNombre = RUTA + nombreArchivo + ".txt";
        File archivo = new File(archivoNombre);

        // Comprobar si el archivo existe
        if (!archivo.exists()) {
            System.out.println("El archivo no existe.");
            return null; // Retorna null si el archivo no existe
        }

        // Inicializo las variables para almacenar los valores del grafo
        Map<String, List<String>> listaAdyacencia = new HashMap<>();
        String fechaHora = "";
        String nombre = "";

        // Leer el archivo y extraer la información
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;

            // Leer las primeras líneas (fecha, nombre del grafo)
            reader.readLine(); //Ignora epicamente la linea # Inicio de Grafo
            nombre = reader.readLine().split(":")[1].trim();
            fechaHora = reader.readLine().split(":")[1].trim();

            // Leer la lista de adyacencia
            while ((linea = reader.readLine()) != null) {
                if(linea.contains("# Fin de Grafo")){
                    break;
                }
                if (linea.contains("->")) {
                    String[] partes = linea.split("->");
                    String vertice = partes[0].trim();
                    String[] adyacentes = partes[1].trim().split(" ");

                    // Asegurarse de que la lista de adyacentes exista en el mapa
                    List<String> lista = listaAdyacencia.getOrDefault(vertice, new ArrayList<>());
                    for (String adyacente : adyacentes) {
                        lista.add(adyacente.trim());
                    }
                    listaAdyacencia.put(vertice, lista);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Determinar el número de vértices
        int numVertices = listaAdyacencia.size();

        GrafoDirigidoAciclico grafo = new GrafoDirigidoAciclico(listaAdyacencia, numVertices, nombre, fechaHora);
        return grafo;
    }

    // Método para verificar si ya existe un grafo con el mismo nombre en el archivo
    public static boolean existeGrafoEnArchivo(String nombreGrafo, String archivoNombre) {

        File archivo = new File(archivoNombre);

        // Verificar si el archivo existe
        if (!archivo.exists()) {
            return false;  // Si el archivo no existe, no hay ningún grafo guardado
        }

        // Leer el archivo y buscar el nombre del grafo
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.contains("Nombre del grafo: " + nombreGrafo)) {
                    return true;  // Si se encuentra el nombre del grafo, retornar true
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // Si no se encuentra, retornar false
    }

    //Metodo que recibe el nombre de un archivop y regresa una lista con los grafos guardados en el archivo
    public static ArrayList<GrafoDirigidoAciclico> cargarGrafos(String nombreArchivo) {
        //Crear la ruta del archivo
        String archivoNombre = RUTA + nombreArchivo + ".txt";
        File archivo = new File(archivoNombre);

        // Comprobar si el archivo existe
        if (!archivo.exists()) {
            System.out.println("El archivo no existe.");
            return null; // Retorna null si el archivo no existe
        }

        //Inicializar las variables para guardar los grafos
        ArrayList<GrafoDirigidoAciclico> grafos = new ArrayList<>();
        String fechaHora = "";
        String nombre = "";
        Map<String, List<String>> listaAdyacencia = new HashMap<>();

        // Leer el archivo y extraer la información de varios grafos
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            StringBuilder grafoData = new StringBuilder();

            // Leer todo el archivo línea por línea
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("# Inicio de Grafo")) { //Si encuentra esto en la linea significa que encontro otro grafo
                    // Encontramos un nuevo grafo, procesamos el grafo anterior si existe
                    if (grafoData.length() > 0) {
                        // Procesamos el grafo almacenado en grafoData
                        GrafoDirigidoAciclico grafo = procesarGrafo(grafoData.toString());
                        if (grafo != null) {
                            grafos.add(grafo);
                        }
                        grafoData.setLength(0); // Limpiar el StringBuilder para el siguiente grafo
                    }
                }

                // Agregar la línea al StringBuilder del grafo actual
                grafoData.append(linea).append("\n");
            }

            // Procesar el último grafo (si el archivo no termina con un # Inicio de Grafo)
            if (grafoData.length() > 0) {
                GrafoDirigidoAciclico grafo = procesarGrafo(grafoData.toString());
                if (grafo != null) {
                    grafos.add(grafo);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return grafos;
    }

    // Método para procesar los datos de un grafo y devolver el objeto GrafoDirigidoAciclico
    private static GrafoDirigidoAciclico procesarGrafo(String grafoData) {
        //Inicializar las variables para cada grafo
        Map<String, List<String>> listaAdyacencia = new HashMap<>();
        String fechaHora = "";
        String nombre = "";

        // Separar los datos del grafo
        String[] lineas = grafoData.split("\n");
        for (String linea : lineas) {
            if (linea.startsWith("Nombre:")) {
                nombre = linea.split(":")[1].trim();
            } else if (linea.startsWith("FechaHora:")) {
                fechaHora = linea.split(":")[1].trim();
            } else if (linea.contains("->")) {
                String[] partes = linea.split("->");
                String vertice = partes[0].trim();
                String[] adyacentes = partes[1].trim().split(" ");

                // Asegurarse de que la lista de adyacentes exista en el mapa
                List<String> lista = listaAdyacencia.getOrDefault(vertice, new ArrayList<>());
                for (String adyacente : adyacentes) {
                    lista.add(adyacente.trim());
                }
                listaAdyacencia.put(vertice, lista);
            }
        }

        // Determinar el número de vértices (puedes hacerlo calculando la cantidad de vértices únicos)
        int numVertices = listaAdyacencia.size();

        // Crear el objeto GrafoDirigidoAciclico con la información procesada
        return new GrafoDirigidoAciclico(listaAdyacencia, numVertices, nombre, fechaHora);
    }
}
