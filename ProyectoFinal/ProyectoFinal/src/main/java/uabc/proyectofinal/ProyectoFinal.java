/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package uabc.proyectofinal;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase para probar los metodos que las instrucciones piden
 * @author Dell
 */
public class ProyectoFinal {

    public static int n;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese n de vertices: ");
        n = sc.nextInt();
        GrafoDirigidoAciclico grafo;

        if (n < 1) {
            System.out.println("Ingresar mayor a 1 o 0");
            return;
        } else if (n == 0) {
            grafo = new GrafoDirigidoAciclico();
            n = grafo.getNumVertices();
        } else {
            grafo = new GrafoDirigidoAciclico(n,false,true);
        }
        System.out.println("Vertices en el grafo: ");
        System.out.println(grafo.mostrarClaves());
        String[] keys = grafo.getClaves();
        
        System.out.println("Ingrese cantidad de aristas a ingresar: ");
        int maxEdges = sc.nextInt();
        if (maxEdges < 0 || maxEdges > ((n * (n - 1)) / 2)) {
            System.out.println("Ingresar cantidad valida entre "+0+" y "+((n * (n - 1)) / 2));
            return;
        }

        sc.nextLine();
        for (int i = 0; i < maxEdges; i++) {
            System.out.println("Ingrese arista "+(i+1));
            System.out.println("Ingrese vertice origen: ");
            String fir = sc.nextLine();
            System.out.println("Ingrese vertice destino: ");
            String sec = sc.nextLine();
            if(grafo.insertarArista(fir, sec)){
                System.out.println("Ingresado");
            }else{
                System.out.println("Genera ciclo, imposible ingresar");
            }
        }
        //Prueba Funcionamiento metodo gradoEntrada
        for (String key : keys) {
            System.out.println("Grado de entrada del vertice " + key + ": " + grafo.gradoDeEntrada(key));
        }
        //Prueba Funcionamiento metodo gradoSalida
        for (String key : keys) {
            System.out.println("Grado de salida del vertice " + key + ": " + grafo.gradoDeSalida(key));
        }
        //Prueba Funcionamiento metodo cuantasAristas
        System.out.println("Hay " + grafo.cuantasAristasHay() + " aristas en el grafo");
        //Prueba Funcionamiento metodo adyacente
        for (String key : keys) {
            for (String key1 : keys) {
                if (grafo.adyacente(key, key1)) {
                    System.out.println("Los vertices " + key + " y " + key1 + " son adyacentes?: " + grafo.adyacente(key, key1));
                }
            }
        }
        //Prueba Funcionamiento metodo conectados
        for (String key : keys) {
            for (String key1 : keys) {
                if (grafo.conectados(key, key1)) {
                    System.out.println("Hay conexion entre los vertices " + key + " y " + key1 + "?: " + grafo.conectados(key, key1));
                }
            }
        }
        //Prueba Funcionamiento metodo topologicalSort
        System.out.println("Ordenamiento Topologico: " + grafo.topologicalSort());
        //Prueba Funcionamiento metodo tieneCiclos
        System.out.println("Ciclico: " + grafo.tieneCiclos());
        //Prueba Funcionamiento metodo mostrarEstructura
        System.out.println("Estructura del Grafo:\n" + grafo.mostrarEstructura());
        //Prueba funcionamiento metodo mostrar grafo
        System.out.println("Grafo: \n" + grafo.obtenerValores());
        
        //Lista de adyacencia
        System.out.println("Lista de adyacencia\n"+grafo.mostrarListaAdyacencia());

        //Prueba Funcionamiento eliminarAristas
        grafo.eliminarAristas();
        grafo.crearVertice("45");
        grafo.crearVertice("55");
        //Prueba Funcionamiento metodo topologicalSort
        System.out.println("Ordenamiento Topologico: " + grafo.topologicalSort());
        //Prueba Funcionamiento metodo tieneCiclos
        System.out.println("Ciclico: " + grafo.tieneCiclos());
        //Prueba Funcionamiento metodo mostrarEstructura
        System.out.println("Estructura del Grafo:\n" + grafo.mostrarEstructura());
        //Prueba funcionamiento metodo mostrar grafo
        System.out.println("Grafo: \n" + grafo.obtenerValores());
        
        grafo.setNombre("grafo2");
        Archivador.guardarGrafo(grafo,"grafos");
        
        
        GrafoDirigidoAciclico grafoX = Archivador.cargarGrafo("grafos");
        System.out.println(grafoX.mostrarClaves());
        System.out.println(grafoX.mostrarEstructura());
        System.out.println(grafoX.mostrarListaAdyacencia());
        
        
        List<GrafoDirigidoAciclico> grapfList = Archivador.cargarGrafos("grafos");
        GrafoDirigidoAciclico graph = grapfList.remove(0);
        System.out.println(graph.mostrarClaves());
        System.out.println(graph.mostrarEstructura());
        System.out.println(graph.mostrarListaAdyacencia());
        
        System.out.println(graph.mostrarClaves());
        System.out.println(graph.mostrarEstructura());
        System.out.println(graph.mostrarListaAdyacencia());
    }
}
