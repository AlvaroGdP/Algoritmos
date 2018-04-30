package presentacion;

import java.io.IOException;
import java.util.Scanner;

import dominio.Busqueda;

public class Presentacion {
	
	private static Scanner read = new Scanner (System.in);
	
	public static void main(String[] args) throws IOException {
		
		while (true) {
			System.out.println("\n***************** Iniciando Ejecución *****************\n");
			String patron = introducirPatron();
			String metodo = introducirMetodo();
			int leerCada = introducirCadaXLineas();
			Busqueda.ejecucionPrincipal(patron, metodo, leerCada);
		}
	}

	private static String introducirPatron() {
		String patron = "";
		while (patron.equals("")) {
			System.out.println("[SISTEMA] Introduce el patrón a localizar en el texto.");
			try {
				patron = read.next();
			}catch (Exception e) {
				System.err.println("[SISTEMA] Error al introducir el patrón.");
			}
		}
		return patron;
	}
	
	private static String introducirMetodo() {
		String metodo = "";
		int opcion = 0;
		System.out.println("********************************************");
		while (opcion != 1 && opcion !=2) {
			System.out.println("[SISTEMA] Elije el algoritmo de búsqueda.");
			System.out.println("-- 1) Knuth-Morris-Prat.");
			System.out.println("-- 2) Karp-Rabin.");
			try {
				opcion = Integer.parseInt(read.next());
			}catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			switch (opcion) {
			case 1:
				metodo = "Knuth-Morris-Prat";
				break;
			case 2:
				metodo = "Karp-Rabin";
				break;
			default:
				System.out.println("[SISTEMA] Por favor, introduce 1 o 2.");
				break;
			}
		}
		System.out.println("********************************************");
		return metodo;
	}

	private static int introducirCadaXLineas() {
		int leerCada = 0;
		while (leerCada <= 0) {
			System.out.println("[SISTEMA] ¿Cada cuántas lineas deseas realizar la busqueda? (1 para todo el texto).");
			try {
				leerCada = Integer.parseInt(read.next());
			}catch (NumberFormatException nfe) {
				System.err.println("[SISTEMA] Por favor, introduce un número natural.");
			}
		}
		return leerCada;
	}

	
}