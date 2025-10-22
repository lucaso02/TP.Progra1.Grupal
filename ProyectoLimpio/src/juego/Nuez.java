package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Nuez {
    private double x;
    private double y;
    private int fila;
    private int columna;
    private Image imagen;

    private static final double inicio_x = 40.0; 
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0; 
    private static final double alto_celda = 100.0;

    public Nuez(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.imagen = Herramientas.cargarImagen("Imagenes/nuez.png"); 
        this.x = inicio_x + columna * ancho_celda + ancho_celda / 2.0;
        this.y = inicio_y + fila * alto_celda + alto_celda / 2.0;
    }

    public void dibujar(Entorno entorno) {
        double escala = 0.10;
        entorno.dibujarImagen(imagen, x, y, 0, escala);
    }

    public void mover(Entorno entorno) {
        double velocidad = 3.0;

        if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && y - velocidad >= inicio_y) y -= velocidad;
        if (entorno.estaPresionada(entorno.TECLA_ABAJO) && y + velocidad <= inicio_y + 5 * alto_celda) y += velocidad;
        if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && x - velocidad >= inicio_x) x -= velocidad;
        if (entorno.estaPresionada(entorno.TECLA_DERECHA) && x + velocidad <= inicio_x + 9 * ancho_celda) x += velocidad;
    }

    // Getters para selección con mouse
    public double getX() { return x; }
    public double getY() { return y; }
}

