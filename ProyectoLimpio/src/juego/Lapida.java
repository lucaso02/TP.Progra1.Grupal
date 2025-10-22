package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Lapida {
    private double x;
    private double y;
    private int fila;
    private int columna;
    private Image imagen;

    private static final double inicio_x = 40.0; 
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0; 
    private static final double alto_celda = 100.0;

    public Lapida(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        // Corregido el nombre de la imagen (sin doble extensión)
        this.imagen = Herramientas.cargarImagen("Imagenes/lapida.png"); 

        // Posición centrada en la celda
        this.x = inicio_x + columna * ancho_celda + ancho_celda / 2.3;
        this.y = inicio_y + fila * alto_celda + alto_celda / 2.0;
    }

    public void dibujar(Entorno entorno) {
        // Ajustar escala según el tamaño de la celda
        double escala = 0.1; 
        entorno.dibujarImagen(imagen, x, y, 0, escala);
    }
}

