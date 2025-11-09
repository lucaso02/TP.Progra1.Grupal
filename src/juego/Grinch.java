package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Grinch {
    private double x;
    private double y;
    private int fila;
    private int columna;
    private int tipo; 
    private Image imagen;
    private int vida; 


    private boolean estaDetenido;
    private static final int DAÑO = 1;

    private static final double inicio_x = 40.0;
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0;
    private static final double alto_celda = 100.0;

    public Grinch(int fila, int columna, int tipo) {
        this.fila = fila;
        this.columna = columna;
        this.tipo = tipo;

        
        if (tipo == 1) {
            this.imagen = Herramientas.cargarImagen("Imagenes/grinch1.png");
        } else {
            this.imagen = Herramientas.cargarImagen("Imagenes/grinch2.png");
        }

        //posicion de entrada en la matriz
        this.x = inicio_x + columna * ancho_celda + ancho_celda / 2.0;
        this.y = inicio_y + fila * alto_celda + alto_celda / 2.0;

        this.vida = 3;
        this.estaDetenido = false; 
    }

    public void dibujar(Entorno entorno) {
        if (!estaVivo()) return;

        double escala = (tipo == 1) ? 0.10 : 0.19;
        entorno.dibujarImagen(imagen, x, y, 0, escala);
    }

    public void moverIzquierda() {
        if (!estaVivo()) return;
        if (!this.estaDetenido) {
            double velocidad = 0.8;
            if (x - velocidad >= inicio_x) {
                x -= velocidad;
            }
        }
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    // recibe un disparo y devuelve true si muere
    public boolean recibirDisparo() {
        if (estaVivo()) {
            vida--;
            return !estaVivo();
        }
        return false;
    }

    // detectar colision con proyectil
    public boolean colisionarConProyectil(Proyectil p) {
        if (p == null || !p.estaActivo() || !estaVivo()) return false;

        if (Math.abs(p.getX() - x) < 40 && Math.abs(p.getY() - y) < 40) {
            p.desactivar();
            return recibirDisparo(); 
        }
        return false;
    }

    
    
    public double getX() { return x; }
    public double getY() { return y; }
    
    public int getFila() { 
        return fila; 
    }
    public int getColumna() { 
        return columna; 
    }

    public void detener() {
        this.estaDetenido = true;
    }

    public void liberar() {
        this.estaDetenido = false;
    }

    public int getDaño() {
        return DAÑO;
    }
}
