package dominio;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;

public class Divisores {

	private static Scanner read = new Scanner (System.in);
	
	private static LinkedHashMap<Integer, Integer> divisores = null;
	
	//Key = divisores (linkedhashmap.keys.toString())
	private static Hashtable<Integer, Nodo> nodosVisitados = null;
	private static int numeroMax = -1;	
	private static String forwBack = ""; //Controlar si usamos Forward o backward
	
	
	/*************************************************************************************************
	 * Método encargado de guiar la ejecución principal del programa, invocando otros métodos necesarios
	 * @param numero: número a utilizar durante el juego
	 * @param modo: forward o backward
	 * @param turnoInicial: si empieza a jugar la máquina o el jugador
	 **********************************************************************************************/
	public static void ejecucionPrincipal(int numero, String modo, boolean turnoInicial) {
		
		divisores = new LinkedHashMap<Integer, Integer>();
		nodosVisitados = new Hashtable<Integer, Nodo>();
		
		numeroMax = numero;
		forwBack = modo;
		boolean turno = turnoInicial;
		
		boolean finPartida = true; //true para continuar jugando, false en caso contrario
		
		calcularDivisores(numeroMax);
		LinkedHashMap<Integer, Integer> initialHash = new LinkedHashMap<Integer, Integer>();
		initialHash = (LinkedHashMap<Integer, Integer>) divisores.clone();
		Nodo estadoActual;
		
		if (forwBack.equals("Forward")) {
			estadoActual = new Nodo(initialHash, 1);
			sucesoresForw(estadoActual);
			visitarForw(estadoActual);
		}else {
			estadoActual = new Nodo(initialHash, numeroMax);
			sucesoresBack(estadoActual);
			visitarBack(estadoActual);
		}
		
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
		System.out.println("[INFO] "+actual.numeroActualToString(numeroMax, forwBack));
		System.out.println("[INFO] "+actual.divisoresToString());
		if (actual.getSucesorElegido()!=null) {
				System.out.println("\n[MAQUINA] "+jugadaRealizadaToString(actual, actual.getSucesorElegido()));
				//System.out.println("[INFO] "+actual.getSucesorElegido().numeroActualToString(numeroMax, forwBack));
				//System.out.println("[INFO] "+actual.getSucesorElegido().divisoresToString());
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
		int base = -1;
		int exponente = -1;
		System.out.println("[INFO] "+actual.numeroActualToString(numeroMax, forwBack));
		System.out.println("[INFO] "+actual.divisoresToString());
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
			if (stringDivisores.contains(""+divisor)) {
				base = encontrarBase(divisor);
				exponente = (int) (Math.log(divisor)/Math.log(base));
				//Tras hallar divisor primo y número de veces, actualizamos los divisores restantes
				actual.getDivisoresRestantes().put(base, actual.getDivisoresRestantes().get(base) - exponente);
				correcto = true;
			}
		}
		System.out.println("[JUGADOR] Dividido por "+(int)Math.pow(base, exponente));
		
		if (forwBack.equals("Forward")) {
			return nodosVisitados.get((int) actual.getNumero() * (int) Math.pow(base,  exponente));
		}else {
			return nodosVisitados.get((int) actual.getNumero() / (int) Math.pow(base,  exponente));
		}
		
	}
	
	
	/*************************************************************************************************
	 * Método utilizado para crear una String con la información sobre la jugada realizada
	 * @param predecesor: estado del problema antes de aplicar dicha acción
	 * @param sucesor: estado del problema tras aplicar la acción
	 * @return String generada
	 *************************************************************************************************/
	private static String jugadaRealizadaToString(Nodo predecesor, Nodo sucesor) {
		if (forwBack.equals("Forward")) {
			return "Dividido por "+sucesor.getNumero()/predecesor.getNumero()+".";
		}else {
			return "Dividido por "+predecesor.getNumero()/sucesor.getNumero()+".";
		}
	}

	
	/***************************************************************************************************
	 * Método utilizado para asignar los sucesores correctos a cada nodo. Versión Forward.
	 * @param predecesor: nodo cuyos sucesores, y él mismo, serán visitados
	 **************************************************************************************************/
	private static void visitarForw(Nodo predecesor) {
		for (int i=0; i<predecesor.getSucesores().size(); i++) {
			Nodo sucesorActual = predecesor.getSucesores().get(i);
			if (finForw(sucesorActual)) { 
				predecesor.setSucesorElegido(sucesorActual); //Predecesor en arbol de decisión, no de generación de nodos
			}else{
				if (!sucesorActual.getVisitado()) {
					visitarBack(sucesorActual);
				}
				if (sucesorActual.getSucesorElegido()==null) {
					predecesor.setSucesorElegido(sucesorActual);
				}
			}
		}
		predecesor.setVisitado(true);
	}
	
	
	/***************************************************************************************************
	 * Método utilizado para asignar los sucesores correctos a cada nodo. Versión Backward.
	 * @param predecesor: nodo cuyos sucesores, y él mismo, serán visitados
	 **************************************************************************************************/
	private static void visitarBack(Nodo predecesor) {
		for (int i=0; i<predecesor.getSucesores().size(); i++) {
			Nodo sucesorActual = predecesor.getSucesores().get(i);
			if (finBack(sucesorActual)) { 
				predecesor.setSucesorElegido(sucesorActual); //Predecesor en arbol de decisión, no de generación de nodos
			}else {
				if (!sucesorActual.getVisitado()) {
					visitarBack(sucesorActual);
				}
				if (sucesorActual.getSucesorElegido()==null) {
					predecesor.setSucesorElegido(sucesorActual);
				}
			}
		}
		predecesor.setVisitado(true);
	}

	
	/**************************************************************************************************
	 * Método utilizado para decidir si se ha alcanzado el último nodo, para ambas versiones.
	 * @param nodo: nodo a comprobar si es el final
	 * @return true si se ha alcanzado el final, false en caso contrario
	 *************************************************************************************************/
	private static boolean fin(Nodo nodo) {
		if (forwBack.equals("Forward")) {
			return finForw(nodo);
		}else {
			return finBack(nodo);
		}
	}
	
	
	/**************************************************************************************************
	 * Método utilizado para decidir si se ha alcanzado el final. Versión Forward.
	 * @param nodo: nodo a comprobar si es el final
	 * @return true si se ha alcanzado el final, false en caso contrario
	 *************************************************************************************************/
	private static boolean finForw(Nodo nodo) {
		if (nodo.getNumero() == numeroMax) {
			return true;
		}
		return false;
	}
	
	/**************************************************************************************************
	 * Método utilizado para decidir si se ha alcanzado el final. Versión Backward.
	 * @param nodo: nodo a comprobar si es el final
	 * @return true si se ha alcanzado el final, false en caso contrario
	 *************************************************************************************************/
	private static boolean finBack(Nodo nodo) {
		if(nodo.getNumero() == 1) {
			return true;
		}
		return false;
		
	}

	
	/**************************************************************************************************
	 * Método utilizado para generar los sucesores de un nodo dado, de forma recursiva. Versión Forward.
	 * @param predecesor: nodo cuyos sucesores serán generados
	 **************************************************************************************************/
	public static void sucesoresForw(Nodo predecesor) {
		// Obtener todos los divisores
		Set<Integer> keySet = predecesor.getDivisoresRestantes().keySet();
		Iterator<Integer> keys = keySet.iterator();
		// Iterar sobre ellos
		while (keys.hasNext()) {
			int divisorActual = keys.next();
			for (int i = 1; i <= predecesor.getDivisoresRestantes().get(divisorActual); i++) {
				// Copiar divisores actuales
				LinkedHashMap<Integer, Integer> aux = (LinkedHashMap<Integer, Integer>) predecesor.getDivisoresRestantes().clone();
				if (numeroMax % (predecesor.getNumero() * (Math.pow(divisorActual, i))) == 0) {
					// Actualizar divisores
					aux.replace(divisorActual, aux.get(divisorActual) - i);
					Nodo sucesor = new Nodo(aux, (int) ((predecesor.getNumero() * (Math.pow(divisorActual, i)))));
					// Si no ha sido visitado o ha sido visitado en otro turno ("por el otro jugador")
					if ((!nodosVisitados.containsKey(sucesor.getNumero()))){
						predecesor.addSucesor(sucesor);
						nodosVisitados.put(sucesor.getNumero(), sucesor);
						sucesoresForw(sucesor);
						// Si ha sido visitado solo se añade como sucesor
					} else {
						predecesor.addSucesor(nodosVisitados.get(sucesor.getNumero()));
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
		//Obtener todos los divisores
		Set<Integer> keySet = predecesor.getDivisoresRestantes().keySet();
		Iterator<Integer> keys = keySet.iterator();
		//Iterar sobre ellos
		while (keys.hasNext()) {
			int divisorActual = keys.next();
			for (int i=1; i<=predecesor.getDivisoresRestantes().get(divisorActual); i++) {
				//Copiar divisores actuales
				LinkedHashMap<Integer, Integer> aux = (LinkedHashMap<Integer, Integer>) predecesor.getDivisoresRestantes().clone();
				//Actualizar divisores
				aux.replace(divisorActual, predecesor.getDivisoresRestantes().get(divisorActual)-i);
				Nodo sucesor = new Nodo(aux, (int) (predecesor.getNumero() / (Math.pow(divisorActual, i))));
				//Si no ha sido visitado o ha sido visitado en otro turno ("por el otro jugador")
				if ((!nodosVisitados.containsKey(sucesor.getNumero()))) {
					predecesor.addSucesor(sucesor);
					nodosVisitados.put(sucesor.getNumero(), sucesor);
					sucesoresBack(sucesor);
				//Si ha sido visitado solo se añade como sucesor
				}else {
					predecesor.addSucesor(nodosVisitados.get(sucesor.getNumero()));
				} 
			}
		}
	}
	
	
	/**************************************************************************************
	 * Método utilizado para hallar la base (prima y única) de un número dado 
	 * @param numero
	 * @return la base, si existe. -1 en caso de error
	 ***************************************************************************************/
	private static int encontrarBase(int numero) {
		if (!esPrimo(numero)) {
			for (int i = 2; i <= (numero / 2 + 2); i++) {
				if (numero % i == 0) {
					return i;
				}
			}
		} else {
			return numero;
		}
		return -1; //Error	
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
					if (divisores.containsKey(i)) {
						divisores.replace(i, divisores.get(i)+1);
						numero = numero / i;
					} else {
						divisores.put(i, 1);
						numero = numero / i;
					}
				}
			}
		} else {
			divisores.put(numero, 1);
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
