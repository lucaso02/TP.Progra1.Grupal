package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Mina {
    private double x;
    private double y;
    private int fila;
    private int columna;
    private boolean activa;
    private Image imagen;
    private Image imagenExplosion;
    private boolean explotando;
    private int tiempoExplosion;

    private static final double inicio_x = 40.0;
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0;
    private static final double alto_celda = 100.0;

    public Mina(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.activa = true;
        this.explotando = false;
        this.tiempoExplosion = 0;

        this.imagen = Herramientas.cargarImagen("imagenes/Potato_Mine.png");
        this.imagenExplosion = Herramientas.cargarImagen("imagenes/explosion.gif");

        if (fila != -1 && columna != -1) {
            setPosicionEnCelda(fila, columna);
        }
    }
    
    public void setPosicionEnPixeles(double x, double y) {
        this.x = x;
        this.y = y;
        this.fila = -1; 
        this.columna = -1;
    }
    
    // PARA MOVER LA MINA FANTASMA (W/A/S/D) Y COLOCARLA 
    public void setPosicionEnCelda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.x = inicio_x + columna * ancho_celda + ancho_celda / 2.0;
        this.y = inicio_y + fila * alto_celda + alto_celda / 2.0;
    }

    public void dibujar(Entorno entorno) {
        if (activa && !explotando) {
            entorno.dibujarImagen(imagen, x, y, 0, 0.2);
        } else if (explotando) {
            entorno.dibujarImagen(imagenExplosion, x, y, 0, 0.3);
            tiempoExplosion++;
            if (tiempoExplosion > 30) { 
                activa = false;
                explotando = false;
            }
        }
    }

    // Verifica si colisiona con un Grinch
    public boolean colisionaCon(Grinch g) {
        return activa && !explotando && g != null && Math.abs(x - g.getX()) < 40 && Math.abs(y - g.getY()) < 40;
    }

    // logica de explosión
    public void explotar() {
        if (activa && !explotando) {
            explotando = true;
        }
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public boolean estaActiva() { 
        return activa || explotando; 
    }
}
