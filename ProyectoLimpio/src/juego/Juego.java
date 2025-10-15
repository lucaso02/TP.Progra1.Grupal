package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;



public class Juego extends InterfaceJuego {
	private Entorno entorno;
	private Image fondo;
	private Regalo[] regalos;
	

	Juego() {
		// Crear entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 1200, 800);

		// Cargar fondo
		this.fondo = Herramientas.cargarImagen("Imagenes/fondo.png");

		// Crear regalos en posiciones separadas
		this.regalos = new Regalo[5];
		this.regalos[0] = new Regalo(200, 150);
		this.regalos[1] = new Regalo(400, 300);
		this.regalos[2] = new Regalo(600, 450);
		this.regalos[3] = new Regalo(800, 200);
		this.regalos[4] = new Regalo(1000, 350);

		// Iniciar entorno
		this.entorno.iniciar();
	}

	public void tick() {
		// Dibujar fondo
		if (this.fondo != null) {
			this.entorno.dibujarImagen(this.fondo, 650, 400, 0, 1.85);
		}
		
		// Dibujar regalos
		for (int i = 0; i < this.regalos.length; i++) {
			this.regalos[i].dibujar(this.entorno );
		}
	}

	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
