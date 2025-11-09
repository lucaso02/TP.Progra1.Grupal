package juego;

import java.awt.Color; 
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Nuez {
    private double x;
    private double y;
    private int fila;
    private int columna;
    private Image imagen;


    private int salud;
    private static final int SALUD_MAXIMA = 300; 
    private boolean seleccionada; 


    private static final double inicio_x = 40.0; 
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0; 
    private static final double alto_celda = 100.0;

    public Nuez(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.imagen = Herramientas.cargarImagen("Imagenes/nuez.png"); 
        
        if (fila != -1 && columna != -1) {
            this.x = inicio_x + columna * ancho_celda + ancho_celda / 2.0;
            this.y = inicio_y + fila * alto_celda + alto_celda / 2.0;
        }
        
        this.salud = SALUD_MAXIMA;
        this.seleccionada = false;
    }
    
    public boolean chequearYAtacar(Grinch g, double anchoCelda) {
        // Lógica de colisión: misma fila, y el Grinch está tocando la Nuez desde la derecha.
        if (g.getFila() == this.fila && g.getX() - this.x <= anchoCelda / 2.0 && g.getX() > this.x) {
            g.detener();
            this.recibirDaño(g.getDaño()); 
            return true; 
        }
        return false;
    }


    public void dibujar(Entorno entorno) {
        double escala = 0.10;
        entorno.dibujarImagen(imagen, x, y, 0, escala);
        
        // Solo dibuja la barra de vida
        if (this.fila >= 0) {
            dibujarBarraVida(entorno);
        }
        if(this.seleccionada) {
            entorno.dibujarRectangulo(x, y, 70, 70, 0, new Color(255, 255, 0, 100)); 
        }
    }

    private void dibujarBarraVida(Entorno entorno) {
        double anchoBarra = 40;
        double altoBarra = 5;
        double progreso = (double)this.salud / SALUD_MAXIMA;
        
        // fondo de la barra en rojo
        entorno.dibujarRectangulo(x, y - 40, anchoBarra, altoBarra, 0, Color.RED);
        
        // progreso de la vida en verde
        double anchoVida = anchoBarra * progreso;
        double centroVidaX = x - anchoBarra / 2.0 + anchoVida / 2.0;
        
        entorno.dibujarRectangulo(centroVidaX, y - 40, anchoVida, altoBarra, 0, Color.GREEN);
    }
    
    // usado al colocar/arrastrar
    public void setPosicionEnCelda(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.x = inicio_x + columna * ancho_celda + ancho_celda / 2.0;
        this.y = inicio_y + fila * alto_celda + alto_celda / 2.0;
    }
    

    public void setPosicionEnPixeles(double x, double y) {
        this.x = x;
        this.y = y;
    }
    

    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
    }

    // daño
    public void recibirDaño(int daño) {
        this.salud -= daño;
    }
    
    public boolean tieneSalud() {
        return this.salud > 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
}