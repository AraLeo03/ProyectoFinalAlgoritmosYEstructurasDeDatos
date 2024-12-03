/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uabc.proyectofinal;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 *  Clase que dibuja los grafos para mostrar en la GUI
 * Extiende de la clase JPanel
 * @author Dell
 */
public class GrafoGUI extends JPanel {

    private GrafoDirigidoAciclico grafo;

    //Metodo constructor que recibe un grafo de la clase GrafoDirigidoAciclico y establece dicho grafo como atributo
    public GrafoGUI(GrafoDirigidoAciclico grafo) {
        this.grafo = grafo;
    }

    //Metodo para dibujar los vertices y las aristas del grafo
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        Map<String, Point> posiciones = new HashMap<>(); //Map para guardar las posiciones de cada vertice

        // Dibujar los nodos
        int i = 0;
        Font fuente = new Font("Arial", Font.BOLD, 20);  // Cambia el tamaño de letra a 20 y fuente Arial en Negritas
        g.setFont(fuente);
        //For para recorrer la lista de adyacencia del grafo
        for (String nodo : grafo.getListaAdyacencia().keySet()) {
            //Establezco los puntos de inicio en el que se va a dibujar el grafo
            int x = 30 + (i % 5) * 100; 
            int y = 30 + (i / 5) * 100;
            //Ingreso los vertices con su posicion al map de posiciones
            posiciones.put(nodo, new Point(x, y));
            //Dibuja los circulos
            g.fillOval(x - 20, y - 20, 40, 40);
            g.setColor(Color.WHITE);
            g.drawString(nodo, x - 10, y + 5);  //Mostrar el nombre del nodo
            g.setColor(Color.BLACK);
            i++;
        }

        // Dibujar las aristas con flechas
        for (String nodo : grafo.getListaAdyacencia().keySet()) {
            Point origen = posiciones.get(nodo);
            for (String vecino : grafo.getListaAdyacencia().get(nodo)) {
                Point destino = posiciones.get(vecino);
                if (destino != null) {
                    // Dibujar la línea
                    g.drawLine(origen.x, origen.y, destino.x, destino.y);

                    // Calcular la posición de la flecha en el medio de la línea
                    int midX = (origen.x + destino.x) / 2;
                    int midY = (origen.y + destino.y) / 2;

                    // Dibujar la flecha en la mitad de la línea
                    dibujarFlecha(g, origen.x, origen.y, destino.x, destino.y, midX, midY);
                }
            }
        }
    }

    //Metodo que dibuja las flechas de direccion de cada arista
    private void dibujarFlecha(Graphics g, int x1, int y1, int x2, int y2, int midX, int midY) {
        // Calcular el ángulo de la línea y aplicar el ajuste
        double angulo = Math.atan2(y2 - y1, x2 - x1);

        // Definir el tamaño de la flecha
        int tamañoFlecha = 10;

        // Coordenadas de la flecha con ajuste angular
        int[] xPoints = {
            midX,
            (int) (midX - tamañoFlecha * Math.cos(angulo - Math.PI / 6)),
            (int) (midX - tamañoFlecha * Math.cos(angulo + Math.PI / 6))
        };
        int[] yPoints = {
            midY,
            (int) (midY - tamañoFlecha * Math.sin(angulo - Math.PI / 6)),
            (int) (midY - tamañoFlecha * Math.sin(angulo + Math.PI / 6))
        };

        // Dibujar el triángulo con ajuste angular y de color rojo
        g.setColor(Color.red);
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Color.black);
    }
    
    //Metodo que recibe el nombre del grafo y los busca en los archivos para mostrarlo
    public static GrafoGUI creaGrafo(String nombre) {
        // Crear un grafo dirigido acíclico con 4 vértices
        GrafoDirigidoAciclico grafo = Archivador.cargarGrafo(nombre);

        GrafoGUI panel = new GrafoGUI(grafo);
        panel.setSize(800,800);
        panel.setVisible(true);
        return  panel;
    }
    
    //Metodo que recibe un grafo dirigido aciclico para mdibuajrlo en la GUI
    public static GrafoGUI creaGrafo(GrafoDirigidoAciclico grafo) {

        GrafoGUI panel = new GrafoGUI(grafo);
        panel.setSize(800,800);
        panel.setVisible(true);
        return  panel;
    }
}
