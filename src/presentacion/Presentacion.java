package presentacion;

import java.util.Scanner;
import dominio.Divisores;

public class Presentacion {

	private static Scanner read = new Scanner (System.in);
	
	public static void main(String[] args) {
	
		while (true) {
			System.out.println("\n***************** Iniciando Nuevo Juego *****************\n");
			
			int numero = introducirNumeroMax();
			boolean turnoInicial = introducirTurnoInicial();
			String modo = introducirModo();
			Divisores.ejecucionPrincipal(numero, modo, turnoInicial);
		}
		
	}
	
	private static int introducirNumeroMax() {
		int numero = 0;
		while (numero <= 0) {
			System.out.println("[SISTEMA] Introduce el número a usar durante el juego.");
			try {
				numero = Integer.parseInt(read.next());
			}catch (NumberFormatException nfe) {
				System.err.println("[SISTEMA] Por favor, introduce un número natural.");
			}
		}
		return numero;
	}
	
	private static boolean introducirTurnoInicial() {
		boolean turnoInicial = true;
		int opcion = 0;
		System.out.println("********************************************");
		System.out.println("[SISTEMA] Decide quien comienza a jugar. (Por defecto, comienza la máquina)");
		System.out.println("-- 1) Comienza la máquina");
		System.out.println("-- 2) Si deseas comenzar");
		try {
			opcion = Integer.parseInt(read.next());
			if (opcion == 2) {
				turnoInicial = false;
			}
		}catch (NumberFormatException nfe) {
			System.out.println("[SISTEMA] Opción 1 seleccionada por defecto.");
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return turnoInicial;
	}
	
	private static String introducirModo() {
		String modo = "";
		int opcion = 0;
		System.out.println("********************************************");
		while (opcion != 1 && opcion !=2) {
			System.out.println("[SISTEMA] Decide el modo de ejecución.");
			System.out.println("-- 1) Forward.");
			System.out.println("-- 2) Backard.");
			try {
				opcion = Integer.parseInt(read.next());
			}catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			switch (opcion) {
			case 1:
				modo = "Forward";
				break;
			case 2:
				modo = "Backward";
				break;
			default:
				System.out.println("[SISTEMA] Por favor, introduce 1 o 2.");
				break;
			}
		}
		System.out.println("********************************************");
		return modo;
	}
	
}
