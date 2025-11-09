package juego;

import java.awt.Image;
import java.awt.Color;
import java.util.Arrays;
import java.util.Random;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// private de los objetos principales del juego
    private Entorno entorno;
    private Flor[] flores;
    private Grinch[] grinches;
    private Proyectil[] proyectiles;
    private Lapida[] lapidas;
    private Nuez[] nueces;
    private Regalo[] regalos;
    private Mina[] minas; 
    
    
    private Flor florParaColocar = null; 
    private Nuez nuezParaColocar = null; 
    private Mina minaParaColocar = null; 
    private int filaColocacion; 
    private int columnaColocacion; 

    private Image fondo;

    private static final int MAX_PROYECTILES = 50;
    
    // VALORES DE LA MATRIZ PRINCIPAL
    private static final int filas = 5;
    private static final int columnas = 9;
    private static final double inicio_x = 40.0;
    private static final double inicio_y = 50.0;
    private static final double ancho_celda = 80.0;
    private static final double alto_celda = 100.0;
    
    private static final double ESPACIO_ENTRE_TABLEROS = 20.0;
    private static final double inicio_y_tableros_inferiores = inicio_y + filas * alto_celda + ESPACIO_ENTRE_TABLEROS;

    
    // variables del TIEMPO del juego
    private int zombiesEliminados = 0; 
    private double tiempoDeJuego;
    private boolean juegoTerminado = false;
    
    
    // COOLDOWN
    private static final double COOLDOWN_SEGUNDOS_FLOR = 5.0; 
    private double tiempoDisponibleFlor = 0.0; 
    
    private Nuez iconoNuez;
    private static final double COOLDOWN_SEGUNDOS_NUEZ = 10.0; 
    private double tiempoDisponibleNuez = 0.0; 
    
    private Mina iconoMina;
    private static final double COOLDOWN_SEGUNDOS_MINA = 15.0; 
    private double tiempoDisponibleMina = 0.0; 

    
    // NIVEL Y OLEADAS
    private int nivelActual;
    private int grinchesTotalNivel; 
    private int grinchesAparecidosNivel; 
    private int grinchesPorGenerar; 
    private int grinchesEliminadosNivel; 
    private static final int MAX_NIVEL = 5; 
    private static final double TIEMPO_ENTRE_GRINCHES = 3.0; 
    private double tiempoUltimaGeneracion = 0.0;



    public Juego() {
        this.entorno = new Entorno(this, "Proyecto TP UNGS", 980, 750);
        this.fondo = Herramientas.cargarImagen("imagenes/fondo.png");

        inicializarIconosSeleccion(); 
        inicializarElementosEstaticos(); 

        
        this.grinches = new Grinch[0]; 
        this.nueces = new Nuez[0]; 
        this.minas = new Mina[0]; 
        this.proyectiles = new Proyectil[MAX_PROYECTILES];
        
        // inicio del juego/nivel
        this.nivelActual = 1;
        configurarNivel(); 
        
        this.entorno.iniciar();
    }
    
    private void inicializarIconosSeleccion() {
        double iconoY = inicio_y_tableros_inferiores + alto_celda / 2.0; 
        double iconoXFlor = inicio_x + ancho_celda / 2.0; 
        //Flores 0
        this.flores = new Flor[1];
        flores[0] = new Flor(-1, -1);
        flores[0].setPosicionEnPixeles(iconoXFlor, iconoY);
        // 1. NUEZ (Columna 1)
        double iconoXNuez = iconoXFlor + ancho_celda; 
        this.iconoNuez = new Nuez(-1, -1); 
        this.iconoNuez.setPosicionEnPixeles(iconoXNuez, iconoY);
        // MINA 2
        double iconoXMina = iconoXNuez + ancho_celda;
        this.iconoMina = new Mina(-1, -1);
        this.iconoMina.setPosicionEnPixeles(iconoXMina, iconoY);
    }
    
    private void inicializarElementosEstaticos() {
        // LÁPIDAS
        this.lapidas = new Lapida[2];
        lapidas[0] = new Lapida(1, 3);
        lapidas[1] = new Lapida(3, 5);
        // REGALOS 
        this.regalos = new Regalo[filas];
        for (int i = 0; i < filas; i++) {
            regalos[i] = new Regalo(i, 0); 
        }
    }

    
    
    
    
    public void tick() {
        if (this.juegoTerminado) {
            dibujarFinal(entorno);
            return;
        }

        this.tiempoDeJuego = this.entorno.numeroDeTick() / 60.0;

        // MANEJO DE CLIC IZQUIERDO Y SELECCIÓN DE ÍCONO 
        if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            int mx = entorno.mouseX();
            int my = entorno.mouseY();
            
            Flor iconoFlor = flores[0]; 

            // Solo permite seleccionar si no hay otra planta/muro ya seleccionada
            if (this.florParaColocar == null && this.nuezParaColocar == null && this.minaParaColocar == null) { 
                
                // LÓGICA DE SELECCIÓN DE FLOR
                if (this.tiempoDeJuego >= this.tiempoDisponibleFlor && 
                    Math.abs(iconoFlor.getX() - mx) <= 30 && Math.abs(iconoFlor.getY() - my) <= 30) 
                {
                    this.florParaColocar = new Flor(0, 0); 
                    this.filaColocacion = 0;
                    this.columnaColocacion = 1; 
                    this.florParaColocar.setPosicionEnCelda(filaColocacion, columnaColocacion);
                    iconoFlor.setSeleccionada(true);
                // LÓGICA DE SELECCIÓN DE NUEZ
                } else if (this.tiempoDeJuego >= this.tiempoDisponibleNuez && 
                           Math.abs(iconoNuez.getX() - mx) <= 30 && Math.abs(iconoNuez.getY() - my) <= 30) 
                {
                    this.nuezParaColocar = new Nuez(0, 0);
                    this.filaColocacion = 0;
                    this.columnaColocacion = 1; 
                    this.nuezParaColocar.setPosicionEnCelda(filaColocacion, columnaColocacion);
                    this.iconoNuez.setSeleccionada(true);
                // LÓGICA DE SELECCIÓN DE MINA
                } else if (this.tiempoDeJuego >= this.tiempoDisponibleMina && 
                           Math.abs(iconoMina.getX() - mx) <= 30 && Math.abs(iconoMina.getY() - my) <= 30) 
                {
                    this.minaParaColocar = new Mina(0, 0); 
                    this.filaColocacion = 0;
                    this.columnaColocacion = 1; 
                    this.minaParaColocar.setPosicionEnCelda(filaColocacion, columnaColocacion);
                }
            } 
        }
        
        // MANEJO DE CLIC DERECHO PLANTAR
        if ((this.florParaColocar != null || this.nuezParaColocar != null || this.minaParaColocar != null) && 
            entorno.sePresionoBoton(entorno.BOTON_DERECHO)) 
        {
            if (!celdaEstaOcupada(this.filaColocacion, this.columnaColocacion)) {
                
                if (this.florParaColocar != null) {
                    añadirFlorAlArreglo(florParaColocar); 
                    this.tiempoDisponibleFlor = this.tiempoDeJuego + COOLDOWN_SEGUNDOS_FLOR;
                    flores[0].setSeleccionada(false);
                    
                } else if (this.nuezParaColocar != null) {
                    añadirNuezAlArreglo(nuezParaColocar); 
                    this.tiempoDisponibleNuez = this.tiempoDeJuego + COOLDOWN_SEGUNDOS_NUEZ;
                    this.iconoNuez.setSeleccionada(false);

                // PLANTAR MINA
                } else if (this.minaParaColocar != null) {
                    añadirMinaAlArreglo(minaParaColocar); 
                    this.tiempoDisponibleMina = this.tiempoDeJuego + COOLDOWN_SEGUNDOS_MINA;
                }
                
                this.florParaColocar = null;
                this.nuezParaColocar = null;
                this.minaParaColocar = null; 
            }
        }
        
        // MOVIMIENTO DE LA PLANTA DE COLOCACIÓN W,A,S,D
        if (this.florParaColocar != null || this.nuezParaColocar != null || this.minaParaColocar != null) {
            
            if (entorno.sePresiono('W') && filaColocacion > 0) filaColocacion--;
            if (entorno.sePresiono('S') && filaColocacion < filas - 1) filaColocacion++;
            if (entorno.sePresiono('A') && columnaColocacion > 1) columnaColocacion--; 
            if (entorno.sePresiono('D') && columnaColocacion < columnas - 1) columnaColocacion++;
            
            if (this.florParaColocar != null) {
                this.florParaColocar.setPosicionEnCelda(filaColocacion, columnaColocacion);
            } else if (this.nuezParaColocar != null) {
                this.nuezParaColocar.setPosicionEnCelda(filaColocacion, columnaColocacion);
            } else if (this.minaParaColocar != null) { 
                this.minaParaColocar.setPosicionEnCelda(filaColocacion, columnaColocacion);
            }
        }

        // DIBUJADO DE FONDO Y TABLERO 
        entorno.dibujarImagen(fondo, 400, 300, 0);
        dibujarCuadricula();
        dibujarTableroTresCeldas();
        dibujarTableroDosCeldas();
        
        // Dibujar planta fantasma
        if (this.florParaColocar != null) {
            this.florParaColocar.dibujar(entorno);
        } else if (this.nuezParaColocar != null) {
            this.nuezParaColocar.dibujar(entorno);
        } else if (this.minaParaColocar != null) { 
            this.minaParaColocar.dibujar(entorno);
        }

        
        for (Lapida l : lapidas) { l.dibujar(entorno); }
        for (Regalo r : regalos) { r.dibujar(entorno); }
        
        // Dibuja las Nueces
        for (Nuez n : nueces) {
            if (n.tieneSalud()) { 
                n.dibujar(entorno);
            }
        }
        
        // Dibuja las Flores y Genera Proyectiles 
        for (int i = 0; i < flores.length; i++) {
            Flor f = flores[i];
            f.dibujar(entorno);
            
            if (i != 0 && f.tieneSalud()) { 
                Proyectil nuevo = f.disparoAutomatico();
                if (nuevo != null) {
                    agregarProyectil(nuevo);
                }
            }
        }
        
        // DIBUJAR ICONOS Y COOLDOWN
        flores[0].dibujar(entorno);
        this.iconoNuez.dibujar(entorno);
        this.iconoMina.dibujar(entorno); 
        
        dibujarCooldownIcono(flores[0], this.tiempoDisponibleFlor, COOLDOWN_SEGUNDOS_FLOR);
        dibujarCooldownIcono(this.iconoNuez, this.tiempoDisponibleNuez, COOLDOWN_SEGUNDOS_NUEZ);
        dibujarCooldownIcono(this.iconoMina, this.tiempoDisponibleMina, COOLDOWN_SEGUNDOS_MINA); 


        // PRINCIPAL GENERAR GRINCHES
        if (this.grinchesPorGenerar > 0 && 
            this.tiempoDeJuego >= this.tiempoUltimaGeneracion + TIEMPO_ENTRE_GRINCHES) 
        {
            Random rnd = new Random();
            int filaAleatoria = rnd.nextInt(filas);
            int columnaInicial = columnas + rnd.nextInt(2); 
            int tipoGrinch = 1 + rnd.nextInt(2);

            Grinch nuevoGrinch = new Grinch(filaAleatoria, columnaInicial, tipoGrinch);
            agregarGrinch(nuevoGrinch); 
            
            this.grinchesPorGenerar--;
            this.grinchesAparecidosNivel++;
            this.tiempoUltimaGeneracion = this.tiempoDeJuego;
        }

        // GRINCH ATAKAR MOVER
        boolean atacando = false; 
        for (Grinch g : grinches) {
            if (!g.estaVivo()) continue; 
            
            g.liberar(); 
            atacando = false; 

            
            // GRINCH y NUECES
            for (Nuez n : nueces) {
                if (!n.tieneSalud()) continue; 
                
                if (g.getFila() == n.getFila() && g.getX() - n.getX() <= ancho_celda / 2.0 && g.getX() > n.getX()) {
                    g.detener();
                    n.recibirDaño(g.getDaño()); 
                    atacando = true;
                    break; 
                }
            }
            //GRINCH y FLORES
            if (!atacando) {
                for (int i = 1; i < flores.length; i++) { 
                    Flor f = flores[i];
                    if (!f.tieneSalud()) continue;
                    
                    if (g.getFila() == f.getFila() && g.getX() - f.getX() <= ancho_celda / 2.0 && g.getX() > f.getX()) {
                        g.detener();
                        f.recibirDaño(g.getDaño()); 
                        atacando = true;
                        break;
                    }
                }
            }

            //GRINCH y REGALOS
            for (Regalo r : regalos) {
                if (g.getFila() == r.getFila() && g.getX() <= inicio_x + ancho_celda / 2.0) {
                    this.juegoTerminado = true; 
                    return; 
                }
            }
            if (!atacando) {
                g.moverIzquierda(); 
            }
            g.dibujar(entorno);
        }
        
        
        // ELIMINAR LAS FLORES SIN VIDA
        Flor[] floresVivas = new Flor[this.flores.length];
        floresVivas[0] = this.flores[0]; 
        int contadorFloresVivas = 1;

        for (int i = 1; i < this.flores.length; i++) {
            Flor f = this.flores[i];
            if (f.tieneSalud()) {
                floresVivas[contadorFloresVivas] = f;
                contadorFloresVivas++;
            }
        }
        this.flores = Arrays.copyOf(floresVivas, contadorFloresVivas);

        // ELIMINAR NUECES SIN VIDA
        Nuez[] nuecesVivas = new Nuez[this.nueces.length];
        int contadorNuecesVivas = 0;

        for (Nuez n : this.nueces) {
            if (n.tieneSalud()) {
                nuecesVivas[contadorNuecesVivas] = n;
                contadorNuecesVivas++;
            }
        }
        this.nueces = Arrays.copyOf(nuecesVivas, contadorNuecesVivas);
        
        // EXPLOSIONES MINAS
        Mina[] minasRestantes = new Mina[this.minas.length];
        int contadorMinasRestantes = 0;

        for (Mina m : this.minas) {
            m.dibujar(entorno); 
            
            if (m.estaActiva()) {
                
            	//COLICIOON MINA GRINCH
                for (Grinch g : grinches) {
                    if (g.estaVivo() && m.colisionaCon(g)) {
                        m.explotar();
                        g.recibirDisparo(); 
                        break; 
                    }
                }
                if (m.estaActiva()) { 
                    minasRestantes[contadorMinasRestantes] = m;
                    contadorMinasRestantes++;
                }
            }
        }
        this.minas = Arrays.copyOf(minasRestantes, contadorMinasRestantes);
        
        
        //PROYECTILES Y COLICIONES
        for (int i = 0; i < proyectiles.length; i++) {
            Proyectil p = proyectiles[i];
            if (p != null && p.estaActivo()) {
                p.mover();
                for (Grinch g : grinches) {
                    if (g.colisionarConProyectil(p)) { 
                        this.zombiesEliminados++; 
                        this.grinchesEliminadosNivel++; 
                        proyectiles[i] = null; 
                        break;
                    }
                }

                p.dibujar(entorno);
                if (p.getX() > entorno.ancho()) {
                     proyectiles[i] = null; 
                }
            }
        }
        
        
        // CHEQUEO DE ESTADO DEL JUEGO Y AVANCE DE NIVEL 
        if (this.grinchesAparecidosNivel == this.grinchesTotalNivel && 
            this.grinchesEliminadosNivel == this.grinchesTotalNivel)    
        {
            if (this.nivelActual < MAX_NIVEL) {
                this.nivelActual++;
                configurarNivel();
                eliminarGrinchesMuertos();
            } else {
                this.juegoTerminado = true;
            }
        }
        
        
        // TEXTO
        int textoX = 600; 
        int textoY = 680; 
        int interlineado = 20;

        entorno.cambiarFont("SansSerif", 16, Color.WHITE);
        entorno.escribirTexto("NIVEL: " + this.nivelActual + " / " + MAX_NIVEL, textoX, textoY); 
        
        String progreso = this.grinchesEliminadosNivel + " / " + this.grinchesTotalNivel;
        entorno.escribirTexto("ELIMINADOS EN NIVEL: " + progreso, textoX, textoY + interlineado);
        
        entorno.escribirTexto("TOTAL ELIMINADOS: " + this.zombiesEliminados, textoX, textoY + (interlineado * 2));
        entorno.escribirTexto("TIEMPO: " + (int)this.tiempoDeJuego + "s", textoX, textoY + (interlineado * 3));
    }

    
    private void dibujarFinal(Entorno entorno) {
        String mensajePrincipal;
        String mensajeSecundario;
        Color colorBase;
        int alfa = 200; 
        
        if (this.nivelActual >= MAX_NIVEL && this.grinchesEliminadosNivel == this.grinchesTotalNivel) {
             mensajePrincipal = "¡VICTORIA!";
             mensajeSecundario = "Has superado todos los niveles.";
             colorBase = Color.GREEN;
        } else {
             mensajePrincipal = "¡DERROTA!";
             mensajeSecundario = "Un Grinch ha tocado la zona de Regalos.";
             colorBase = Color.RED;
        }
        Color colorFondo = new Color(colorBase.getRed(), colorBase.getGreen(), colorBase.getBlue(), alfa);

        entorno.dibujarRectangulo(entorno.ancho() / 2, entorno.alto() / 2, entorno.ancho(), entorno.alto(), 0, colorFondo);
        entorno.cambiarFont("SansSerif", 72, Color.YELLOW);
        entorno.escribirTexto(mensajePrincipal, entorno.ancho() / 2 - 180, entorno.alto() / 2 - 50);
        entorno.cambiarFont("SansSerif", 32, Color.WHITE);
        entorno.escribirTexto(mensajeSecundario, entorno.ancho() / 2 - 250, entorno.alto() / 2 + 30);
    }
    
    
    //logica del juego RESPECTO AL (nivel,muertes,cooldown)
    private void configurarNivel() {
        this.grinchesEliminadosNivel = 0;
        this.grinchesAparecidosNivel = 0;
        this.tiempoUltimaGeneracion = this.tiempoDeJuego; 
        int cantidadBase = 5;
        int cantidadCalculada = cantidadBase * (int)Math.pow(2, this.nivelActual - 1);

        if (cantidadCalculada > 50) {
            this.grinchesTotalNivel = 50;
        } else {
            this.grinchesTotalNivel = cantidadCalculada;
        }
        
        this.grinchesPorGenerar = this.grinchesTotalNivel;

        System.out.println("--- NIVEL " + this.nivelActual + " INICIADO ---. Meta: " + this.grinchesTotalNivel + " Grinches.");
    }
    
    private void eliminarGrinchesMuertos() {
        Grinch[] grinchesVivos = new Grinch[this.grinches.length];
        int contadorGrinchesVivos = 0;

        for (Grinch g : this.grinches) {
            if (g.estaVivo()) {
                grinchesVivos[contadorGrinchesVivos] = g;
                contadorGrinchesVivos++;
            }
        } 
        this.grinches = Arrays.copyOf(grinchesVivos, contadorGrinchesVivos);
    }
    
    
    private void dibujarCooldownIcono(Object icono, double tiempoDisp, double cooldownTotal) {
        double iconoX = 0;
        double iconoY = 0;
        
        if (icono instanceof Flor) {
            Flor f = (Flor) icono;
            iconoX = f.getX();
            iconoY = f.getY();
        } else if (icono instanceof Nuez) {
            Nuez n = (Nuez) icono;
            iconoX = n.getX();
            iconoY = n.getY();
        } else if (icono instanceof Mina) { 
            Mina m = (Mina) icono;
            iconoX = m.getX();
            iconoY = m.getY();
        } else {
            return;
        }
        
        if (this.tiempoDeJuego < tiempoDisp) {
            double tiempoRestante = tiempoDisp - this.tiempoDeJuego;
            double progreso = (cooldownTotal - tiempoRestante) / cooldownTotal; 
            
            double anchoBarraTotal = 60; 
            double altoBarra = 5;

            entorno.dibujarRectangulo(iconoX, iconoY, 60, 60, 0, new Color(0, 0, 0, 150)); 
            entorno.dibujarRectangulo(iconoX, iconoY + 40, anchoBarraTotal, altoBarra, 0, Color.BLACK);
            
            double anchoProgreso = anchoBarraTotal * progreso;
            double centroProgresoX = iconoX - anchoBarraTotal / 2.0 + anchoProgreso / 2.0;
            
            entorno.dibujarRectangulo(centroProgresoX, iconoY + 40, anchoProgreso, altoBarra, 0, Color.GREEN);
            
        } else {
            entorno.dibujarRectangulo(iconoX, iconoY, 65, 65, 0, new Color(0, 255, 0, 50)); 
        }
    }
    
    
    
    // DUPLICA LOS ITEMS AL SER SELECCIONADOS
    private void añadirFlorAlArreglo(Flor nuevaFlor) {
        Flor[] nuevoArreglo = Arrays.copyOf(this.flores, this.flores.length + 1);
        nuevoArreglo[nuevoArreglo.length - 1] = nuevaFlor;
        this.flores = nuevoArreglo;
    }
    private void añadirNuezAlArreglo(Nuez nuevaNuez) {
        Nuez[] nuevoArreglo = Arrays.copyOf(this.nueces, this.nueces.length + 1);
        nuevoArreglo[nuevoArreglo.length - 1] = nuevaNuez;
        this.nueces = nuevoArreglo;
    }
    private void añadirMinaAlArreglo(Mina nuevaMina) {
        Mina[] nuevoArreglo = Arrays.copyOf(this.minas, this.minas.length + 1);
        nuevoArreglo[nuevoArreglo.length - 1] = nuevaMina;
        this.minas = nuevoArreglo;
    }
    private void agregarGrinch(Grinch nuevoGrinch) {
        Grinch[] nuevoArreglo = Arrays.copyOf(this.grinches, this.grinches.length + 1);
        nuevoArreglo[nuevoArreglo.length - 1] = nuevoGrinch;
        this.grinches = nuevoArreglo;
    }
    private void agregarProyectil(Proyectil nuevo) {
        for (int i = 0; i < proyectiles.length; i++) {
            if (proyectiles[i] == null || !proyectiles[i].estaActivo()) {
                proyectiles[i] = nuevo;
                return;
            }
        }
    }
    
    
    // VER SI LA CELDA ESTA OCUPADA O NO
    private boolean celdaEstaOcupada(int fila, int columna) {
        for (Flor f : flores) {
            if (f.getFila() == fila && f.getColumna() == columna && f != flores[0]) {
                return true;
            }
        }
        for (Lapida l : lapidas) {
            if (l.getFila() == fila && l.getColumna() == columna) {
                return true;
            }
        }
        for (Nuez n : nueces) {
            if (n.getFila() == fila && n.getColumna() == columna && n.tieneSalud()) {
                return true;
            }
        }
        for (Mina m : minas) { 
            if (m.getFila() == fila && m.getColumna() == columna && m.estaActiva()) {
                return true;
            }
        }
        return false;
    }
    
    
    //  METODOS DE DIBUJO DE CUADRICULA 
    private void dibujarCuadricula() {
        double anchoTablero = columnas * ancho_celda;
        double altoTablero = filas * alto_celda;

        for (int i = 0; i <= filas; i++) {
            double y = inicio_y + i * alto_celda;
            entorno.dibujarRectangulo(inicio_x + anchoTablero / 2, y, anchoTablero, 2, 0, Color.BLACK);
        }

        for (int j = 0; j <= columnas; j++) {
            double x = inicio_x + j * ancho_celda;
            entorno.dibujarRectangulo(x, inicio_y + altoTablero / 2, 2, altoTablero, 0, Color.BLACK);
        }
    }
    private void dibujarTableroTresCeldas() {
        double ancho_celda_aux = ancho_celda; 
        double alto_celda_aux = alto_celda;
        double inicio_x_aux = inicio_x; 
        double inicio_y_aux = inicio_y_tableros_inferiores; 

        int columnas_aux = 3;
        double anchoTableroNuevo = columnas_aux * ancho_celda_aux;

        for (int i = 0; i <= 1; i++) {
            double y = inicio_y_aux + i * alto_celda_aux;
            entorno.dibujarRectangulo(inicio_x_aux + anchoTableroNuevo / 2, y, anchoTableroNuevo, 2, 0, Color.BLACK);
        }

        for (int j = 0; j <= columnas_aux; j++) {
            double x = inicio_x_aux + j * ancho_celda_aux;
            entorno.dibujarRectangulo(x, inicio_y_aux + alto_celda_aux / 2, 2, alto_celda_aux, 0, Color.BLACK);
        }
    }
    private void dibujarTableroDosCeldas() {
        double ancho_celda_aux = ancho_celda; 
        double alto_celda_aux = alto_celda;
        
        double inicio_x_aux = inicio_x + 3 * ancho_celda_aux; 
        double inicio_y_aux = inicio_y_tableros_inferiores; 

        int columnas_aux = 2;
        double anchoTableroNuevo = columnas_aux * ancho_celda_aux;

        for (int i = 0; i <= 1; i++) {
            double y = inicio_y_aux + i * alto_celda_aux;
            entorno.dibujarRectangulo(inicio_x_aux + anchoTableroNuevo / 2, y, anchoTableroNuevo, 2, 0, Color.BLACK);
        }

        for (int j = 0; j <= columnas_aux; j++) {
            double x = inicio_x_aux + j * ancho_celda_aux;
            entorno.dibujarRectangulo(x, inicio_y_aux + alto_celda_aux / 2, 2, alto_celda_aux, 0, Color.BLACK);
        }
    }
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        new Juego();
    }
}