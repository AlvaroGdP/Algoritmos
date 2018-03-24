package dominio;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;

public class Divisores {

	private static Scanner read = new Scanner (System.in);
	
	private static LinkedHashMap<Integer, Integer> divisores = new LinkedHashMap<Integer, Integer>();
	
	//Key = divisores (linkedhashmap.keys.toString()) concatenado con turno (true, false)
	private static Hashtable<String, Nodo> nodosVisitados = new Hashtable<String, Nodo>();
	private static int numeroMax = -1;	
	private static String forwBack = ""; //Controlar si usamos Forward o backward
	
	public static void ejecucionPrincipal(int numero, String modo, boolean turnoInicial) {
		
		numeroMax = numero;
		forwBack = modo;
		boolean turno = turnoInicial;
		
		calcularDivisores(numeroMax);
		LinkedHashMap<Integer, Integer> initialHash = new LinkedHashMap<Integer, Integer>();
		initialHash = (LinkedHashMap<Integer, Integer>) divisores.clone();
		Nodo estadoActual;
		
		if (forwBack.equals("Forward")) {
			estadoActual = new Nodo(initialHash, turno, 1);
			sucesoresForw(estadoActual, 1);
			visitarForw(estadoActual);
		}else {
			estadoActual = new Nodo(initialHash, turno, numeroMax);
			sucesoresBack(estadoActual);
			visitarBack(estadoActual);
		}
		
		do {
			
			if (turno) { 
				if (!fin(estadoActual)) {
					estadoActual = turnoMaquina(estadoActual);
					turno = false;
				}else {
					System.out.println("[MAQUINA] Tú ganas :(");
					break;
				}
			}else {
				if (!fin(estadoActual)) {
					estadoActual = turnoJugador(estadoActual);
					turno = true;
				}else {
					break;
				}
			}
			
		}while (true);		
	}

	
	
	private static Nodo turnoMaquina(Nodo actual) {
		int jugadaRandom;
		System.out.println("\n[INFO] "+actual.numeroActualToString(numeroMax, forwBack));
		System.out.println("[INFO] "+actual.divisoresToString());
		if (actual.getSucesorGanador()!=null) {
			System.out.println("\n[MAQUINA] "+jugadaRealizadaToString(actual, actual.getSucesorGanador()));
			System.out.println("[INFO] "+actual.getSucesorGanador().numeroActualToString(numeroMax, forwBack));
			System.out.println("[INFO] "+actual.getSucesorGanador().divisoresToString());
			System.out.println("[MAQUINA] He ganado :)");
			return actual.getSucesorGanador();
		}else {
			jugadaRandom = (int) (Math.random() * (actual.getSucesores().size()));
			System.out.println("\n[MAQUINA] "+jugadaRealizadaToString(actual, actual.getSucesores().get(jugadaRandom)));
			return actual.getSucesores().get(jugadaRandom);
		}
	}
	
	private static Nodo turnoJugador(Nodo actual) {
		boolean correcto = false;
		int divisor = -1;
		int base = -1;
		int exponente = -1;
		String stringDivisores = actual.divisoresToString();
		System.out.println("\n[INFO] "+actual.numeroActualToString(numeroMax, forwBack));
		System.out.println("[INFO] "+stringDivisores);
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
				exponente = encontrarExponente(base, divisor);
				//Tras hallar divisor primo y número de veces, actualizamos los divisores restantes
				actual.getDivisoresRestantes().put(base, actual.getDivisoresRestantes().get(base) - exponente);
				correcto = true;
			}
		}
		System.out.println("[JUGADOR] Dividido por "+(int)Math.pow(base, exponente));
		return nodosVisitados.get(actual.getDivisoresRestantes().toString()+!actual.getTurno());
	}
	
	private static String jugadaRealizadaToString(Nodo predecesor, Nodo sucesor) {
		if (forwBack.equals("Forward")) {
			return "Dividido por "+sucesor.getNumero()/predecesor.getNumero()+".";
		}else {
			return "Dividido por "+predecesor.getNumero()/sucesor.getNumero()+".";
		}
	}

	private static void visitarForw(Nodo predecesor) {
		for (int i=0; i<predecesor.getSucesores().size(); i++) {
			Nodo sucesorActual = predecesor.getSucesores().get(i);
			if (finForw(sucesorActual) && predecesor.getTurno()) { 
				predecesor.setSucesorGanador(sucesorActual); //Predecesor en arbol de decisión, no de generación de nodos
			}else if(!finForw(sucesorActual)) {
				visitarForw(sucesorActual);
			}
		}
	}
	
	private static void visitarBack(Nodo predecesor) {
		for (int i=0; i<predecesor.getSucesores().size(); i++) {
			Nodo sucesorActual = predecesor.getSucesores().get(i);
			if (finBack(sucesorActual) && predecesor.getTurno()) { 
				predecesor.setSucesorGanador(sucesorActual); //Predecesor en arbol de decisión, no de generación de nodos
			}else if(!finBack(sucesorActual)) {
				visitarBack(sucesorActual);
			}
		}
	}

	private static boolean fin(Nodo nodo) {
		if (forwBack.equals("Forward")) {
			return finForw(nodo);
		}else {
			return finBack(nodo);
		}
	}
	
	private static boolean finForw(Nodo nodo) {
		if (nodo.getNumero() == numeroMax) {
			return true;
		}
		return false;
	}
	
	private static boolean finBack(Nodo nodo) {
		if(nodo.getNumero() == 1) {
			return true;
		}
		return false;
		
	}

	public static void sucesoresForw(Nodo predecesor, int numeroActual) {
		// Obtener todos los divisores
		Set<Integer> keySet = predecesor.getDivisoresRestantes().keySet();
		Iterator<Integer> keys = keySet.iterator();
		// Iterar sobre ellos
		while (keys.hasNext()) {
			int divisorActual = keys.next();
			for (int i = 1; i <= predecesor.getDivisoresRestantes().get(divisorActual); i++) {
				// Copiar divisores actuales
				LinkedHashMap<Integer, Integer> aux = (LinkedHashMap<Integer, Integer>) predecesor.getDivisoresRestantes().clone();
				if (numeroMax % (numeroActual * (Math.pow(divisorActual, i))) == 0) {
					// Actualizar divisores
					aux.replace(divisorActual, aux.get(divisorActual) - i);
					// Si no ha sido visitado o ha sido visitado en otro turno ("por el otro jugador")
					if ((!nodosVisitados.containsKey(aux.toString() + !predecesor.getTurno()) 
							|| nodosVisitados.get(aux.toString() + !predecesor.getTurno()).getTurno() != predecesor.getTurno())) {
						
						Nodo sucesor = new Nodo(aux, !predecesor.getTurno(), (int) ((numeroActual * (Math.pow(divisorActual, i)))));
						predecesor.addSucesor(sucesor);
						nodosVisitados.put(aux.toString() + !predecesor.getTurno(), sucesor);
						sucesoresForw(sucesor, (int) (numeroActual * (Math.pow(divisorActual, i))));
						// Si ha sido visitado --Y-- en el mismo turno, solo se añade como sucesor
					} else {
						predecesor.addSucesor(nodosVisitados.get(aux.toString() + !predecesor.getTurno()));
					}

				}
			}
		}
	}
	
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
				
				//Si no ha sido visitado o ha sido visitado en otro turno ("por el otro jugador")
				if ((!nodosVisitados.containsKey(aux.toString()+!predecesor.getTurno()) 
						|| nodosVisitados.get(aux.toString()+!predecesor.getTurno()).getTurno() != predecesor.getTurno())) {
					
					Nodo sucesor = new Nodo(aux, !predecesor.getTurno(), (int) (predecesor.getNumero() / (Math.pow(divisorActual, i))));
					predecesor.addSucesor(sucesor);
					nodosVisitados.put(aux.toString()+!predecesor.getTurno(), sucesor);
					sucesoresBack(sucesor);
					
				//Si ha sido visitado --Y-- en el mismo turno, solo se añade como sucesor
				}else {
					predecesor.addSucesor(nodosVisitados.get(aux.toString()+!predecesor.getTurno()));
				} 
			}
		}
	}
	
	private static int encontrarExponente(int base, int numero) {
		int exponente = 0;
		while (numero != 1) {
			numero = numero/base;
			exponente++;
		}
		return exponente;
	}

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
	
	public static void calcularDivisores(int numero) {
		if (!esPrimo(numero)) {
			for (int i = 2; i <= (numero / 2 + 1); i++) { //Ya hemos comprobado si es primo. El mayor divisor posible es el numero / 2
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
