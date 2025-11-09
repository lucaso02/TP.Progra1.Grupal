package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Proyectil {
    private double x, y;
    private boolean activo = true;
    private double velocidad = 5.0;
    private Image imagen;

    public Proyectil(double x, double y) {
        this.x = x;
        this.y = y;
        this.imagen = Herramientas.cargarImagen("imagenes/disparo.gif");
    }

    public void mover() { x += velocidad; }

    public void dibujar(Entorno e) { e.dibujarImagen(imagen, x, y, 0, 0.1); }

    public boolean estaActivo() { return activo; }

    public void desactivar() { activo = false; }

    public double getX() { return x; }
    public double getY() { return y; }

    public boolean colisionaCon(Grinch g) {
        return Math.abs(this.x - g.getX()) < 20 && Math.abs(this.y - g.getY()) < 20;
    }
}