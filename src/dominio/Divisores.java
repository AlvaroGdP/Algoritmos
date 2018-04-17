package dominio;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class Divisores {

	private static Scanner read = new Scanner (System.in);
	private static LinkedList<Integer> divisores = null;
	private static Hashtable<Integer, Nodo> nodosVisitados = null;
	private static int numeroMax = -1;	
	private static String forwBack = ""; //Controlar si usamos Forward o backward
	private static LinkedList<Nodo> sucesoresEnCola = null; //Estructura auxiliar utilizada para implementar DFS
	
	
	/*************************************************************************************************
	 * Método encargado de guiar la ejecución principal del programa, invocando otros métodos necesarios
	 * @param numero: número a utilizar durante el juego
	 * @param modo: forward o backward
	 * @param turnoInicial: si empieza a jugar la máquina o el jugador
	 **********************************************************************************************/
	public static void ejecucionPrincipal(int numero, String modo, boolean turnoInicial) {
		
		divisores = new LinkedList<Integer>();
		nodosVisitados = new Hashtable<Integer, Nodo>();
		sucesoresEnCola = new LinkedList<Nodo>();
		
		numeroMax = numero;
		forwBack = modo;
		boolean turno = turnoInicial;
		boolean finPartida = true; //true para continuar jugando, false en caso contrario
		calcularDivisores(numeroMax);
		LinkedList<Integer> initialList = new LinkedList<Integer>();
		initialList = (LinkedList<Integer>) divisores.clone();
		Nodo estadoActual = new Nodo(initialList, numeroMax);

		if (forwBack.equals("Forward")) {
			sucesoresEnCola.add(estadoActual);
			sucesoresForw();
		}else {
			sucesoresBack(estadoActual);
		}
		
		visitar(estadoActual);
		
		do {
			if (turno) { 
				if (!fin(estadoActual)) {
					estadoActual = turnoMaquina(estadoActual);
					turno = false;
				}else {
					System.out.println("\n[MAQUINA] Tú ganas :(\n");
					finPartida = false;
				}
			}else {
				if (!fin(estadoActual)) {
					estadoActual = turnoJugador(estadoActual);
					turno = true;
				}else {
					System.out.println("\n[MAQUINA] He ganado :)\n");
					finPartida = false;
				}
			}
		}while (finPartida);		
	}

	
	/*************************************************************************************************
	 * Método encargado de simular la toma de decisiones del oponente(máquina)
	 * @param actual: estado actual del problema
	 * @return estado del problema tras aplicar alguna acción
	 ************************************************************************************************/
	private static Nodo turnoMaquina(Nodo actual) {
		int jugadaRandom;
		System.out.println(estadoToString(actual));
		if (actual.getSucesorElegido()!=null) {
				System.out.println("\n[MAQUINA] "+jugadaRealizadaToString(actual, actual.getSucesorElegido()));
				return actual.getSucesorElegido();
		}
		jugadaRandom = (int) (Math.random() * (actual.getSucesores().size()));
		System.out.println("\n[MAQUINA] "+jugadaRealizadaToString(actual, actual.getSucesores().get(jugadaRandom)));
		return actual.getSucesores().get(jugadaRandom);
	}
	
	
	/*************************************************************************************************
	 * Método utilizado para que el jugador decida la acción a tomar
	 * @param actual: estado actual del problema
	 * @return estado del problema tras aplicar alguna acción
	 *************************************************************************************************/
	private static Nodo turnoJugador(Nodo actual) {
		boolean correcto = false;
		int divisor = -1;
		Nodo sucesor = null;
		System.out.println(estadoToString(actual));
		String stringDivisores = actual.divisoresToString();
		while (!correcto) {
			System.out.println("[SISTEMA] Introduce un divisor válido de los anteriores por el que divir al número actual.");
			try {
				divisor = Integer.parseInt(read.next());
			}catch (NumberFormatException nfe) {
				System.err.println("[SISTEMA] Por favor, introduce un número natural.");
			}catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			if (stringDivisores.contains(" "+divisor+" ")) {
				sucesor = nodosVisitados.get(actual.getNumero() / divisor);
				correcto = true;
			}
		}
		System.out.println("[JUGADOR] Dividido por "+divisor);
		return sucesor;
	}
	
	/*************************************************************************************************
	 * Método utilizado para crear una String con la información del estado actual
	 * @param n Nodo que contiene la información del estado actual
	 * @return String generada
	 *************************************************************************************************/
	private static String estadoToString(Nodo n) {
		return "[INFO] "+n.numeroActualToString() + "\n" + "[INFO] "+n.divisoresToString();
	}
	
	/*************************************************************************************************
	 * Método utilizado para crear una String con la información sobre la jugada realizada
	 * @param predecesor: estado del problema antes de aplicar dicha acción
	 * @param sucesor: estado del problema tras aplicar la acción
	 * @return String generada
	 *************************************************************************************************/
	private static String jugadaRealizadaToString(Nodo predecesor, Nodo sucesor) {
			return "Dividido por "+predecesor.getNumero()/sucesor.getNumero()+".";
	}

	
	/***************************************************************************************************
	 * Método utilizado para asignar los sucesores correctos a cada nodo. Versión Forward.
	 * @param predecesor: nodo cuyos sucesores, y él mismo, serán visitados
	 **************************************************************************************************/
	private static void visitar(Nodo predecesor) {
		for (int i=0; i<predecesor.getSucesores().size(); i++) {
			Nodo sucesorActual = predecesor.getSucesores().get(i);
			if (fin(sucesorActual)) { 
				predecesor.setSucesorElegido(sucesorActual);
			}else{
				if (!sucesorActual.getVisitado()) {
					visitar(sucesorActual);
				}
				if (sucesorActual.getSucesorElegido()==null) {
					predecesor.setSucesorElegido(sucesorActual);
					predecesor.setVisitado(true);
					return;
				}
			}
		}
		predecesor.setVisitado(true);
	}
	
	
	/**************************************************************************************************
	 * Método utilizado para decidir si se ha alcanzado el final. Versión Backward.
	 * @param nodo: nodo a comprobar si es el final
	 * @return true si se ha alcanzado el final, false en caso contrario
	 *************************************************************************************************/
	private static boolean fin(Nodo nodo) {
		if(nodo.getNumero() == 1) {
			return true;
		}
		return false;
		
	}

	
	/**************************************************************************************************
	 * Método utilizado para generar los sucesores de un nodo dado, de forma recursiva. Versión Forward.
	 * @param predecesor: nodo cuyos sucesores serán generados
	 **************************************************************************************************/	
	public static void sucesoresForw() {
		while (!sucesoresEnCola.isEmpty()) {
			Nodo predecesor = sucesoresEnCola.remove();
			// Obtener todos los divisores
			Set<Integer> keySet = predecesor.getDivisoresRestantes().keySet();
			Iterator<Integer> keys = keySet.iterator();
			// Iterar sobre ellos
			while (keys.hasNext()) {
				int divisorActual = keys.next();
				for (int i = 1; i <= predecesor.getDivisoresRestantes().get(divisorActual); i++) {
					// Copiar divisores actuales
					LinkedHashMap<Integer, Integer> aux = (LinkedHashMap<Integer, Integer>) predecesor.getDivisoresRestantes().clone();
					if (numeroMax % (predecesor.getNumero() / (Math.pow(divisorActual, i))) == 0) {
						// Actualizar divisores
						aux.replace(divisorActual, aux.get(divisorActual) - i);
						int numeroActual = (int) (predecesor.getNumero() / (Math.pow(divisorActual, i)));
						if ((!nodosVisitados.containsKey(numeroActual))) {
							Nodo sucesor = new Nodo(aux, numeroActual);
							predecesor.addSucesor(sucesor);
							nodosVisitados.put(sucesor.getNumero(), sucesor);
							sucesoresEnCola.addLast(sucesor);
							// Si ha sido creado solo se añade como sucesor
						} else {
							predecesor.addSucesor(nodosVisitados.get(numeroActual));
						}
					}
				}
			}
		}
	}
	
	
	/**************************************************************************************************
	 * Método utilizado para generar los sucesores de un nodo dado, de forma recursiva. Versión Backward.
	 * @param predecesor: nodo cuyos sucesores serán generados
	 **************************************************************************************************/
	public static void sucesoresBack(Nodo predecesor) {
		int divisorActual = -1;
		int firstOccurence = -1;
		int lastOccurence = -1;
		for (int i=0; i < predecesor.getDivisoresRestantes().size(); i++) {
			divisorActual = predecesor.getDivisoresRestantes().get(i);
			firstOccurence = i;
			lastOccurence = predecesor.getDivisoresRestantes().lastIndexOf(divisorActual);
			i = lastOccurence; //Dado que están ordenados, podemos avanzar hasta la siguiente posicion
			
			//Generaremos tantos sucesores como exponente del divisor Actua
			for (int j=1; j <= (lastOccurence - firstOccurence + 1); j++) { 
				LinkedList<Integer> copiaDivisoresRestantes = (LinkedList<Integer>) predecesor.getDivisoresRestantes().clone();
				
				//Eliminamos tantos divisores como exponente
				for (int k=0; k<j; k++) { 
					copiaDivisoresRestantes.remove(firstOccurence);
				}
				
				int numeroActual = (int) (predecesor.getNumero() / (Math.pow(divisorActual, j)));
				//Si aun no ha sido creado
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
	 * @param numero
	 ************************************************************************************************/
	public static void calcularDivisores(int numero) {
		int numInicial = numero;
		if (!esPrimo(numero)) {
			for (int i = 2; i <= (numInicial / 2 + 1); i++) { //Ya hemos comprobado si es primo. El mayor divisor posible es el numero / 2
				while (numero % i == 0) {
					divisores.addLast(i);
					numero = numero / i;
				}
			}
		} else {
			divisores.addLast(numero);
		}
	}

	
	/***************************************************************************************
	 * Método utilizado para detectar si un número dado es primo
	 * @param n: dicho úmero
	 * @return: true en caso afirmativo, false en caso contrario
	 ***************************************************************************************/
	public static boolean esPrimo(long n) {
		boolean primo = (n == 2 || n % 2 != 0);
		int raiz = (int) Math.sqrt(n);
		if (n != 2) {
			for (long i = 3; primo && i <= raiz; i = i + 2)
				if (n % i == 0)
					primo = false;
		}
		return primo;
	}
	
}
