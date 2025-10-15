package juego;

import entorno.Herramientas;
import entorno.Entorno;
import java.awt.Image;

public class Regalo {
	private double x;
	private double y;
	private Image imagen;

	public Regalo(double x, double y) {
		this.x = x;
		this.y = y;
		this.imagen = Herramientas.cargarImagen("Imagenes/Regaloss.png");
	}

	public void dibujar(Entorno entorno) {
		double escala = 0.3; // ajustá como quieras
		entorno.dibujarImagen(imagen, x, y, 0, escala);
	}

	public void setX(double x) {
	    this.x = x;
	}

	public void setY(double y) {
	    this.y = y;
	}

	public void setPos(double x, double y) {
	    this.x = x;
	    this.y = y;
	}
}




