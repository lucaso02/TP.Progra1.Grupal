package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Regalo {
    private double x;
    private double y;
    private int fila;
    private int columna;
    private Image imagen;

    private static final double inicio_x = 40.0; 
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0; 
    private static final double alto_celda = 100.0;

    public Regalo(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.imagen = Herramientas.cargarImagen("Imagenes/Regaloss.png");

        this.x = inicio_x + columna * ancho_celda + ancho_celda / 2.0;
        this.y = inicio_y + fila * alto_celda + alto_celda / 2.0;
    }

    public void dibujar(Entorno entorno) {
        double escala = 0.35; // ajustado para entrar en la celda
        entorno.dibujarImagen(imagen, x, y, 0, escala);
    }

    // Getters para que los Grinches puedan acercarse
    public double getX() { return x; }
    public double getY() { return y; }
}
