package dominio;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class Divisores {

	private static Scanner read = new Scanner(System.in);
	private static LinkedList<Integer> divisores = null;
	private static Hashtable<Integer, Nodo> nodosVisitados = null;
	private static int numeroMax = -1;
	private static String forwBack = ""; // Controlar si usamos Forward o backward

	/*************************************************************************************************
	 * Método encargado de guiar la ejecución principal del programa, invocando
	 * otros métodos necesarios
	 * 
	 * @param numero:
	 *            número a utilizar durante el juego
	 * @param modo:
	 *            forward o backward
	 * @param turnoInicial:
	 *            si empieza a jugar la máquina o el jugador
	 **********************************************************************************************/
	public static void ejecucionPrincipal(int numero, String modo, boolean turnoInicial) {

		divisores = new LinkedList<Integer>();
		nodosVisitados = new Hashtable<Integer, Nodo>();

		numeroMax = numero;
		forwBack = modo;
		boolean turno = turnoInicial;
		boolean continuar = true; // true para continuar jugando, false en caso contrario
		calcularDivisores(numeroMax);
		LinkedList<Integer> initialList = new LinkedList<Integer>();
		initialList = (LinkedList<Integer>) divisores.clone();
		Nodo estadoActual = new Nodo(initialList, numeroMax);

		if (forwBack.equals("Forward")) {
			estadoActual = new Nodo(initialList, 1);
			sucesoresForw(estadoActual);
		} else {
			estadoActual = new Nodo(initialList, numeroMax);
			sucesoresBack(estadoActual);
		}

		visitar(estadoActual);

		do {
			if (turno) {
				if (estadoActual.getSucesores().size()!=0) {
					estadoActual = turnoMaquina(estadoActual);
					turno = false;
				} else {
					System.out.println("\n[MAQUINA] Tú ganas :(\n");
					continuar = false;
				}
			} else {
				if (estadoActual.getSucesores().size()!=0) {
					estadoActual = turnoJugador(estadoActual);
					turno = true;
				} else {
					System.out.println("\n[MAQUINA] He ganado :)\n");
					continuar = false;
				}
			}
		} while (continuar);
	}

	/*************************************************************************************************
	 * Método encargado de simular la toma de decisiones del oponente(máquina)
	 * 
	 * @param actual:
	 *            estado actual del problema
	 * @return estado del problema tras aplicar alguna acción
	 ************************************************************************************************/
	private static Nodo turnoMaquina(Nodo actual) {
		int jugadaRandom;
		System.out.println(estadoToString(actual));
		if (actual.getSucesorGanador() != null) {
			System.out.println("\n[MAQUINA] " + jugadaRealizadaToString(actual, actual.getSucesorGanador()));
			return actual.getSucesorGanador();
		}
		jugadaRandom = (int) (Math.random() * (actual.getSucesores().size()));
		System.out.println("\n[MAQUINA] " + jugadaRealizadaToString(actual, actual.getSucesores().get(jugadaRandom)));
		return actual.getSucesores().get(jugadaRandom);
	}

	/*************************************************************************************************
	 * Método utilizado para que el jugador decida la acción a tomar
	 * 
	 * @param actual:
	 *            estado actual del problema
	 * @return estado del problema tras aplicar alguna acción
	 *************************************************************************************************/
	private static Nodo turnoJugador(Nodo actual) {
		boolean correcto = false;
		int divisor = -1;
		Nodo sucesor = null;
		System.out.println(estadoToString(actual));
		String stringDivisores = actual.divisoresToString();
		while (!correcto) {
			System.out.println(
					"[SISTEMA] Introduce un divisor válido de los anteriores por el que divir al número actual.");
			try {
				divisor = Integer.parseInt(read.next());
			} catch (NumberFormatException nfe) {
				System.err.println("[SISTEMA] Por favor, introduce un número natural.");
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			if (stringDivisores.contains(" " + divisor + " ")) {
				if (forwBack.equals("Forward")) {
					sucesor = nodosVisitados.get(actual.getNumero() * divisor);
					correcto = true;
				} else {
					sucesor = nodosVisitados.get(actual.getNumero() / divisor);
					correcto = true;
				}
			}
		}
		System.out.println("[JUGADOR] Dividido por " + divisor);
		return sucesor;
	}

	/*************************************************************************************************
	 * Método utilizado para crear una String con la información del estado actual
	 * 
	 * @param n
	 *            Nodo que contiene la información del estado actual
	 * @return String generada
	 *************************************************************************************************/
	private static String estadoToString(Nodo n) {
		return "[INFO] " + n.numeroActualToString(numeroMax, forwBack) + "\n" + "[INFO] " + n.divisoresToString();
	}

	/*************************************************************************************************
	 * Método utilizado para crear una String con la información sobre la jugada
	 * realizada
	 * 
	 * @param predecesor:
	 *            estado del problema antes de aplicar dicha acción
	 * @param sucesor:
	 *            estado del problema tras aplicar la acción
	 * @return String generada
	 *************************************************************************************************/
	private static String jugadaRealizadaToString(Nodo predecesor, Nodo sucesor) {
		if (forwBack.equals("Forward")) {
			return "Dividido por " + sucesor.getNumero() / predecesor.getNumero() + ".";
		} else {
			return "Dividido por " + predecesor.getNumero() / sucesor.getNumero() + ".";
		}
	}

	/***************************************************************************************************
	 * Método utilizado para asignar los sucesores correctos a cada nodo. Versión
	 * Forward.
	 * 
	 * @param forwBack
	 * @param predecesor:
	 *            nodo cuyos sucesores, y él mismo, serán visitados
	 **************************************************************************************************/
	private static void visitar(Nodo predecesor) {
		for (int i = 0; i < predecesor.getSucesores().size(); i++) {
			Nodo sucesorActual = predecesor.getSucesores().get(i);
			//Si el sucesor no ha sido visitado, invocar recursivamente
			if (!sucesorActual.getVisitado()) { 
				visitar(sucesorActual);
			}else {	
				if (sucesorActual.getSucesorGanador() == null) { 
					predecesor.setSucesorGanador(sucesorActual); 
					predecesor.setVisitado(true);
					return;
				}
			}
		}
		predecesor.setVisitado(true);
	}

	/**************************************************************************************************
	 * Método utilizado para generar los sucesores de un nodo dado, de forma
	 * recursiva. Versión Forward.
	 * 
	 * @param predecesor:
	 *            nodo cuyos sucesores serán generados
	 **************************************************************************************************/
	public static void sucesoresForw(Nodo predecesor) {
		int divisorActual = -1;
		int firstOccurence = -1;
		int lastOccurence = -1;
		LinkedList<Nodo> sucesoresEnCola = new LinkedList<Nodo>();
		sucesoresEnCola.add(predecesor);
		
		while (!sucesoresEnCola.isEmpty()) {
			predecesor = sucesoresEnCola.remove();
			// Obtener todos los divisores
			for (int i = 0; i < predecesor.getDivisoresRestantes().size(); i++) {
				divisorActual = predecesor.getDivisoresRestantes().get(i);
				firstOccurence = i;
				lastOccurence = predecesor.getDivisoresRestantes().lastIndexOf(divisorActual);
				i = lastOccurence; // Dado que están ordenados, podemos avanzar hasta la ultima posicion para la siguiente iteracion

				//Generamos tantos sucesores como exponente del divisor actual
				for (int j = 1; j <= (lastOccurence - firstOccurence + 1); j++) {
					LinkedList<Integer> copiaDivisoresRestantes = (LinkedList<Integer>) predecesor.getDivisoresRestantes().clone();
					// Eliminamos tantos divisores como exponente
					for (int k = 0; k < j; k++) {
						copiaDivisoresRestantes.remove(firstOccurence);
					}
					int numeroActual = (int) (predecesor.getNumero() * (Math.pow(divisorActual, j)));

					// Si aun no ha sido creado 
					if ((!nodosVisitados.containsKey(numeroActual))) {
						Nodo sucesor = new Nodo(copiaDivisoresRestantes, numeroActual);
						predecesor.addSucesor(sucesor);
						nodosVisitados.put(numeroActual, sucesor);
						sucesoresEnCola.add(sucesor);
					// Si ha sido creado solo se añade como sucesor
					} else {
						predecesor.addSucesor(nodosVisitados.get(numeroActual));
					}
				}
			}
		}
	}

	/**************************************************************************************************
	 * Método utilizado para generar los sucesores de un nodo dado, de forma
	 * recursiva. Versión Backward.
	 * 
	 * @param predecesor:
	 *            nodo cuyos sucesores serán generados
	 **************************************************************************************************/
	public static void sucesoresBack(Nodo predecesor) {
		int divisorActual = -1;
		int firstOccurence = -1;
		int lastOccurence = -1;
		
		for (int i = 0; i < predecesor.getDivisoresRestantes().size(); i++) {
			divisorActual = predecesor.getDivisoresRestantes().get(i);
			firstOccurence = i;
			lastOccurence = predecesor.getDivisoresRestantes().lastIndexOf(divisorActual);
			i = lastOccurence; // Dado que están ordenados, podemos avanzar hasta la ultima posicion para la siguiente iteracion

			// Generaremos tantos sucesores como exponente del divisor Actual
			for (int j = 1; j <= (lastOccurence - firstOccurence + 1); j++) {
				LinkedList<Integer> copiaDivisoresRestantes = (LinkedList<Integer>) predecesor.getDivisoresRestantes().clone();
				// Eliminamos tantos divisores como exponente
				for (int k = 0; k < j; k++) {
					copiaDivisoresRestantes.remove(firstOccurence);
				}

				int numeroActual = (int) (predecesor.getNumero() / (Math.pow(divisorActual, j)));
				// Si aun no ha sido creado
				if ((!nodosVisitados.containsKey(numeroActual))) {
					Nodo sucesor = new Nodo(copiaDivisoresRestantes, numeroActual);
					predecesor.addSucesor(sucesor);
					nodosVisitados.put(sucesor.getNumero(), sucesor);
					sucesoresBack(sucesor);
				// Si ha sido creado solo se añade como sucesor
				} else {
					predecesor.addSucesor(nodosVisitados.get(numeroActual));
				}
			}
		}
	}

	/************************************************************************************************
	 * Método utilizado para calcular los divisores de un número
	 * 
	 * @param numero
	 ************************************************************************************************/
	public static void calcularDivisores(int numero) {
		int numInicial = numero;
		while (numero % 2 == 0) { //Dividir por 2 hasta que el número sea impar
			divisores.addLast(2);
			numero = numero / 2;
		}
		for (int i = 3; i <= numInicial / 3; i+=2) { // El numero restante no es par
			while (numero % i == 0) {               // 	Solo comprobamos divisores impares
				divisores.addLast(i);
				numero = numero / i;
			}
		}
		if (numero != 1) {
			divisores.addLast(numero); //Solo ocurrirá si el numero es primo
		}
	}

}
