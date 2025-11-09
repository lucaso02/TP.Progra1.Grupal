package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Color;

public class Flor {
    private double x, y;
    private int fila, columna;
    private Image imagen;
    private boolean seleccionada = false;
    private int contadorTicks = 0;
    

    private int saludMaxima = 50; 
    private int saludActual;

    private static final double inicio_x = 40.0;
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0;
    private static final double alto_celda = 100.0;

    public Flor(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.imagen = Herramientas.cargarImagen("imagenes/rosaa.png");
        
        
        this.saludActual = this.saludMaxima;

        setPosicionEnCelda(fila, columna); 
    }
    
    public void recibirDaño(int daño) {
        this.saludActual -= daño;
        if (this.saludActual < 0) {
            this.saludActual = 0;
        }
    }
    
    public boolean tieneSalud() {
        return this.saludActual > 0;
    }
    
    public void dibujar(Entorno entorno) {
        entorno.dibujarImagen(imagen, x, y, 0, 0.11);
        
        //divuja barra de vida de la flor
        if (this.saludActual < this.saludMaxima && this.saludActual > 0) {
            double progreso = (double)this.saludActual / this.saludMaxima;
            double anchoBarra = ancho_celda * 0.7;
            double anchoProgreso = anchoBarra * progreso;
            double centroBarraX = x;
            double centroBarraY = y + alto_celda / 2.0 - 5;
            
            entorno.dibujarRectangulo(centroBarraX, centroBarraY, anchoBarra, 3, 0, Color.RED);
            entorno.dibujarRectangulo(centroBarraX - anchoBarra / 2.0 + anchoProgreso / 2.0, centroBarraY, anchoProgreso, 3, 0, Color.GREEN);
        }
    }

    
    public void mover(Entorno entorno) {
        double velocidad = 3.0;
        if (entorno.estaPresionada('W') && y - velocidad >= inicio_y) y -= velocidad;
        if (entorno.estaPresionada('S') && y + velocidad <= inicio_y + 5 * alto_celda) y += velocidad;
        if (entorno.estaPresionada('A') && x - velocidad >= inicio_x) x -= velocidad;
        if (entorno.estaPresionada('D') && x + velocidad <= inicio_x + 9 * ancho_celda) x += velocidad;
    }

    public Proyectil disparoAutomatico() {
        // La flor del ícono no dispara
        contadorTicks++;
        if (contadorTicks >= 100) { 
            contadorTicks = 0;
            return new Proyectil(x, y); 
        }
        return null;
    }
    

    public void setPosicionEnPixeles(double x, double y) {
        this.x = x;
        this.y = y;

        this.fila = -1; 
        this.columna = -1;
    }
    
    // establecer la posición final en la cuadrícula
    public void setPosicionEnCelda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;

        this.x = inicio_x + columna * ancho_celda + ancho_celda / 2.0;
        this.y = inicio_y + fila * alto_celda + alto_celda / 2.0;
    }

    // getters/setters
    public void setSeleccionada(boolean s) { seleccionada = s; }
    public boolean estaSeleccionada() { return seleccionada; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    
    public int getSaludActual() { return saludActual; }
}
