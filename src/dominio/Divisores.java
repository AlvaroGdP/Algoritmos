package dominio;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

public class Divisores {

	private static Scanner read = new Scanner(System.in);
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


		nodosVisitados = new Hashtable<Integer, Nodo>();

		numeroMax = numero;
		forwBack = modo;
		boolean turno = turnoInicial;
		boolean continuar = true; // true para continuar jugando, false en caso contrario
		LinkedList<NumeroPotencia> divisores = calcularDivisores(numeroMax);
		LinkedList<NumeroPotencia> initialList = new LinkedList<NumeroPotencia>();
		initialList = (LinkedList<NumeroPotencia>) divisores.clone();
		Nodo estadoActual = new Nodo(initialList, numeroMax);
		
		if (forwBack.equals("Forward")) {
			estadoActual = new Nodo(initialList, 1);
			estadoActual = sucesoresForw(estadoActual);
		} else {
			estadoActual = new Nodo(initialList, numeroMax);
			sucesoresBack(estadoActual);
		}

		do {
			if (turno) {
				if (estadoActual.getSucesores().size() != 0) {
					estadoActual = turnoMaquina(estadoActual);
					turno = false;
				} else {
					System.out.println("\n[MAQUINA] Tú ganas :(\n");
					continuar = false;
				}
			} else {
				if (estadoActual.getSucesores().size() != 0) {
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
			//Si la string contiene el divisor (rodeado de espacios), es válido
			if (stringDivisores.contains(" " + divisor + " ")) {
				sucesor = nodosVisitados.get(actual.getNumero() / divisor);
				correcto = true;
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
		/*if (forwBack.equals("Forward")) {
			return "Dividido por " + sucesor.getNumero() / predecesor.getNumero() + ".";
		} else {*/
			return "Dividido por " + predecesor.getNumero() / sucesor.getNumero() + ".";
		//}
	}

	/**************************************************************************************************
	 * Método utilizado para generar los sucesores de un nodo dado, de forma
	 * iterativa (BFS). Versión Forward.
	 * 
	 * @param predecesor:
	 *            nodo cuyos sucesores serán generados
	 **************************************************************************************************/
	public static Nodo sucesoresForw(Nodo predecesor) {
		NumeroPotencia actual;
		LinkedList<Nodo> sucesoresEnCola = new LinkedList<Nodo>();
		sucesoresEnCola.add(predecesor);
		nodosVisitados.put(1, predecesor);
		while (!sucesoresEnCola.isEmpty()) {
			predecesor = sucesoresEnCola.remove();
			
			// Obtener todos los divisores
			for (int i = 0; i < predecesor.getDivisoresRestantes().size(); i++) {
				actual = predecesor.getDivisoresRestantes().get(i);
				
				// Generaremos tantos sucesores como exponente del divisor Actual
				for (int j = 1; j <= actual.getExponente(); j++) {
					LinkedList<NumeroPotencia> copiaDivisores = predecesor.cloneDivisores();
					copiaDivisores.get(i).setExponente(actual.getExponente() - j);
					
					// Si ya se ha dividido todas las veces posibles, se elimina de la lista
					if (copiaDivisores.get(i).getExponente()==0){
						copiaDivisores.remove(i);
					}
					
					int numeroActual = (int) (predecesor.getNumero() * (Math.pow(actual.getBase(), j)));
					// Si aun no ha sido creado
					if ((!nodosVisitados.containsKey(numeroActual))) {
						Nodo sucesor = new Nodo(copiaDivisores, numeroActual);
						sucesor.addSucesor(predecesor);
						nodosVisitados.put(numeroActual, sucesor);
						sucesoresEnCola.add(sucesor);
						// Si ha sido creado solo se añade como sucesor
					} else {
						nodosVisitados.get(numeroActual).addSucesor(predecesor);
					}
					esGanador(predecesor, nodosVisitados.get(numeroActual));
				}
			}
			predecesor.setDivisoresRestantes(calcularDivisores(predecesor.getNumero()));
		}
		return predecesor;
	}

	/*************************************************************************************************************
	 * Metodo utilizado para comprobar si un nodo es ganador o no, y actualizar el atributo esGanador del nodo padre
	 * 	en consecuencia
	 * @param padre
	 * @param hijo
	 *************************************************************************************************************/
	public static void esGanador(Nodo padre, Nodo hijo) {
		if (padre.getSucesorGanador() == null) {
			hijo.setSucesorGanador(padre);
		}
	}

	/**************************************************************************************************
	 * Método utilizado para generar los sucesores de un nodo dado, de forma
	 * recursiva (DFS). Versión Backward.
	 * 
	 * @param predecesor:
	 *            nodo cuyos sucesores serán generados
	 **************************************************************************************************/
	public static void sucesoresBack(Nodo predecesor) {
		NumeroPotencia actual;
		
		for (int i = 0; i < predecesor.getDivisoresRestantes().size(); i++) {
			actual = predecesor.getDivisoresRestantes().get(i);
			
			// Generaremos tantos sucesores como exponente del divisor Actual
			for (int j = 1; j <= actual.getExponente(); j++) {
				LinkedList<NumeroPotencia> copiaDivisores = predecesor.cloneDivisores();
				copiaDivisores.get(i).setExponente(actual.getExponente() - j);
				
				// Si ya se ha dividido todas las veces posibles, se elimina de la lista
				if (copiaDivisores.get(i).getExponente()==0){
					copiaDivisores.remove(i);
				}
				
				int numeroActual = (int) (predecesor.getNumero() / (Math.pow(actual.getBase(), j)));
				// Si aun no ha sido creado
				if ((!nodosVisitados.containsKey(numeroActual))) {
					Nodo sucesor = new Nodo(copiaDivisores, numeroActual);
					predecesor.addSucesor(sucesor);
					nodosVisitados.put(sucesor.getNumero(), sucesor);
					sucesoresBack(sucesor);
				// Si ha sido creado solo se añade como sucesor
				} else {
					predecesor.addSucesor(nodosVisitados.get(numeroActual));
				}
				
				// En cualquier caso, comprobar si es ganador o perdedor
				esGanador(nodosVisitados.get(numeroActual),predecesor);
			}
		}
	}

	/************************************************************************************************
	 * Método utilizado para calcular los divisores de un número
	 * 
	 * @param numero
	 * @return 
	 ************************************************************************************************/
	public static LinkedList<NumeroPotencia> calcularDivisores(int numero) {
		int numInicial = numero;
		LinkedList<NumeroPotencia> divisores = new LinkedList<NumeroPotencia>();
		NumeroPotencia divisoresBaseX = new NumeroPotencia(2, 0);
		
		// Dividir por 2 hasta que el número sea impar
		while (numero % 2 == 0) { 
			divisoresBaseX.setExponente(divisoresBaseX.getExponente() + 1);
			numero = numero / 2;
		}
		//Si hemos dividido alguna vez por 2
		if (divisoresBaseX.getExponente()!=0) {
			divisores.addLast(divisoresBaseX);
		}
		
		// Solo comprobamos divisores impares
		for (int i = 3; i <= numInicial / 3; i += 2) { 
			divisoresBaseX = new NumeroPotencia(i, 0); 
			while (numero % i == 0) {
				divisoresBaseX.setExponente(divisoresBaseX.getExponente() + 1);
				numero = numero / i;
			}
			//Si hemos dividido alguna vez por i
			if (divisoresBaseX.getExponente()!=0) {
				divisores.addLast(divisoresBaseX);
			}
			//Escapar de bucle for si ya hemos terminado de dividir
			if (numero == 1) {
				break;
			}
		}
		if (numero != 1) {
			divisores.addLast(new NumeroPotencia(numero, 1)); // Solo ocurrirá si el numero es primo
		}
		return divisores;
	}

}
