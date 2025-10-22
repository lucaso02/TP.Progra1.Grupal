package juego;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

    private Entorno entorno;
    private Regalo[] regalos;
    private Flor[] flores;
    private Lapida[] lapidas;
    private Nuez[] nueces;
    private Grinch[] grinches;
    private Image fondo;

    private Flor florSeleccionada = null;
    private Nuez nuezSeleccionada = null;

    private static final int filas = 5;
    private static final int columnas = 9;
    private static final double inicio_x = 40.0; 
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0; 
    private static final double alto_celda = 100.0;

    public Juego() {
        this.entorno = new Entorno(this, "Proyecto TP UNGS", 980, 750);

        // Fondo
        this.fondo = Herramientas.cargarImagen("imagenes/fondo.png");

        // Crear regalos
        this.regalos = new Regalo[filas];
        for (int i = 0; i < filas; i++) {
            this.regalos[i] = new Regalo(i, 0);
        }

        // Crear flores
        this.flores = new Flor[4];
        this.flores[0] = new Flor(0, 1);
        this.flores[1] = new Flor(1, 2);
        this.flores[2] = new Flor(2, 2);
        this.flores[3] = new Flor(4, 2);

        // Crear lápidas
        this.lapidas = new Lapida[2];
        this.lapidas[0] = new Lapida(1, 3);
        this.lapidas[1] = new Lapida(3, 5);

        // Crear nueces
        this.nueces = new Nuez[4];
        this.nueces[0] = new Nuez(0, 5);
        this.nueces[1] = new Nuez(1, 4);
        this.nueces[2] = new Nuez(2, 5);
        this.nueces[3] = new Nuez(3, 4);

        // Crear muchos Grinches aleatorios
        Random rnd = new Random();
        int cantidadGrinches = 15; // muchos más
        this.grinches = new Grinch[cantidadGrinches];
        for (int i = 0; i < cantidadGrinches; i++) {
            int fila = rnd.nextInt(filas);
            int tipo = rnd.nextInt(2) + 1;
            double columna = columnas + rnd.nextInt(5); // empiezan fuera de la pantalla
            this.grinches[i] = new Grinch(fila, (int)columna, tipo);
        }

        this.entorno.iniciar();
    }

    public void tick() {
        // Selección de planta con clic del mouse
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            int mx = entorno.mouseX();
            int my = entorno.mouseY();
            florSeleccionada = null;
            nuezSeleccionada = null;

            for (Flor f : flores) {
                if (Math.abs(f.getX() - mx) <= 30 && Math.abs(f.getY() - my) <= 30) {
                    florSeleccionada = f;
                }
            }
            for (Nuez n : nueces) {
                if (Math.abs(n.getX() - mx) <= 30 && Math.abs(n.getY() - my) <= 30) {
                    nuezSeleccionada = n;
                }
            }
        }

        // Mover solo la planta seleccionada
        if (florSeleccionada != null) florSeleccionada.mover(entorno);
        if (nuezSeleccionada != null) nuezSeleccionada.mover(entorno);

        // Dibujar fondo
        this.entorno.dibujarImagen(fondo, 400, 300, 0); 

        // Cuadrícula
        dibujarCuadricula();

        // Dibujar regalos
        for (Regalo r : regalos) r.dibujar(entorno);

        // Dibujar flores
        for (Flor f : flores) f.dibujar(entorno);

        // Dibujar lápidas
        for (Lapida l : lapidas) l.dibujar(entorno);

        // Dibujar nueces
        for (Nuez n : nueces) n.dibujar(entorno);

        // Mover y dibujar Grinches
        for (Grinch g : grinches) {
            g.moverIzquierda();
            g.dibujar(entorno);
        }
    }

    private void dibujarCuadricula() {
        double anchoTablero = columnas * ancho_celda;
        double altoTablero = filas * alto_celda;

        for (int i = 0; i <= filas; i++) {
            double y = inicio_y + i * alto_celda;
            entorno.dibujarRectangulo(inicio_x + anchoTablero/2, y, anchoTablero, 2, 0, Color.BLACK);
        }
        for (int j = 0; j <= columnas; j++) {
            double x = inicio_x + j * ancho_celda;
            entorno.dibujarRectangulo(x, inicio_y + altoTablero/2, 2, altoTablero, 0, Color.BLACK);
        }
    }

    public static void main(String[] args) {
        new Juego();
    }
}

