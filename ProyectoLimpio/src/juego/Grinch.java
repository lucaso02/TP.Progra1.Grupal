package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Grinch {
    private double x;
    private double y;
    private int fila;
    private int columna;
    private int tipo; // 1 o 2
    private Image imagen;

    private static final double inicio_x = 40.0;
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0;
    private static final double alto_celda = 100.0;

    public Grinch(int fila, int columna, int tipo) {
        this.fila = fila;
        this.columna = columna;
        this.tipo = tipo;

        // Elegir imagen según el tipo
        if (tipo == 1) {
            this.imagen = Herramientas.cargarImagen("Imagenes/grinch1.png");
        } else {
            this.imagen = Herramientas.cargarImagen("Imagenes/grinch2.png");
        }

        // Posición centrada en la celda
        this.x = inicio_x + columna * ancho_celda + ancho_celda / 2.0;
        this.y = inicio_y + fila * alto_celda + alto_celda / 2.0;
    }

    public void dibujar(Entorno entorno) {
        double escala;
        if (tipo == 1) {
            escala = 0.10; // Grinch 1
        } else {
            escala = 0.19; // Grinch 2
        }
        entorno.dibujarImagen(imagen, x, y, 0, escala);
    }

    // Método para mover automáticamente hacia la izquierda
    public void moverIzquierda() {
        double velocidad = 1.5;
        if (x - velocidad >= inicio_x) {
            x -= velocidad;
        }
    }

    // Getters para interacción si hace falta
    public double getX() { return x; }
    public double getY() { return y; }
}
