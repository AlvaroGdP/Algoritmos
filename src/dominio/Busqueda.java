package dominio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Busqueda {

	/*******************************************************************************************************
	 * Metodo encargado de guiar la ejecucion del programa, invocando otros metodos necesarios
	 * @param patron a localizar en el texto
	 * @param metodo utilizado para ello
	 * @param leerCada, integer que contiene la informacion sobre cada cuantas lineas realizar la busqueda
	 * @throws IOException
	 *******************************************************************************************************/
	public static void ejecucionPrincipal(String patron, String metodo, int leerCada) throws IOException {
		BufferedReader reader = null;
		int numeroOcurrencias = 0;
		ArrayList<LineaOcurrencia> ocurrencias = new ArrayList<LineaOcurrencia>();

		// Hardcode
		String path = "quijote1.txt";

		File texto = new File(path);
		System.out.println("Abriendo archivo " + path);
		try {
			// Necesario para poder abrir el archivo con formato correcto
			// Evitar problemas por caracteres especiales
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(texto), "Cp1252"));
		} catch (FileNotFoundException e) {
			System.err.println("Error al intentar leer el archivo " + path);
			System.exit(0);
		}
		System.out.println("Recorriendo archivo...");
		
		if (metodo.equals("Knuth-Morris-Prat")) {
			ocurrencias = KnuthMorrisPrat(patron, reader, leerCada);
		} else {
			ocurrencias = KarpRabin(patron, reader, leerCada);
		}
		
		reader.close();
		System.out.println("Archivo recorrido correctamente.");
		System.out.println("\n***** Mostrando resultados *****\n");
		System.out.println("Patrón encontrado con método "+metodo+" en: ");
		numeroOcurrencias = mostrarResultados(ocurrencias);
		if (leerCada!=1)
			System.out.println("Ocurrencias estimadas: " + leerCada * numeroOcurrencias);
	}

	/****************************************************************************************************************
	 * Metodo utilizado para mostrar las posiciones de las ocurrencias encontradas, asi como devolver el numero total
	 * @param ocurrenciasTotales, ArrayList que contiene las ocurrencias encontradas, y en qué linea
	 * @return numero total de ocurrencias
	 *****************************************************************************************************************/
	private static int mostrarResultados(ArrayList<LineaOcurrencia> ocurrenciasTotales) {
		int total = 0;
		for (int i = 0; i < ocurrenciasTotales.size(); i++) {
			// Si hemos encontrado coincidencias
			if (ocurrenciasTotales.get(i).getOcurrencias().size() != 0) {
				System.out.println(ocurrenciasTotales.get(i).toString());
				total += ocurrenciasTotales.get(i).getOcurrencias().size();
			}
		}
		System.out.println("\nOcurrencias totales: " + total);
		return total;
	}

	//-------------------   Knuth-Morris-Prat -----------------------------------------//
	
	/***************************************************************************************
	 * Metodo encargado de procesar el fichero, invocando al metodo que lo hace linea a linea
	 * Version de las transparencias modificada
	 * @param patron a reconocer en el texto
	 * @param reader, objeto Java que nos permite leer el fichero
	 * @param leerCada, integer que indica en que lineas debemos buscar el patron
	 * @return ArrayList que contiene las ocurrencias encontradas y en que linea
	 * @throws IOException
	 **************************************************************************************/
	public static ArrayList<LineaOcurrencia> KnuthMorrisPrat(String patron, BufferedReader reader, int leerCada) throws IOException {
		ArrayList<Integer> ocurrencias = new ArrayList<Integer>();
		ArrayList<LineaOcurrencia> ocurrenciasTotales = new ArrayList<LineaOcurrencia>();
		String linea;
		int numeroLinea = 0;
		while ((linea = reader.readLine()) != null) {
			// Comprobar si debemos procesar esta linea
			if (numeroLinea % leerCada == 0) {
				if (patron.length() > 0 && linea.length() >= patron.length()) {
					int[] fallo = new int[patron.length()];
					fallo = preproceso(patron);// matriz de fallos
					ocurrencias = KnuthMorrisPrat(patron, linea, fallo);
					LineaOcurrencia actual = new LineaOcurrencia(numeroLinea+1, ocurrencias);
					ocurrenciasTotales.add(actual);
				}
			}
			numeroLinea++;
		}
		return ocurrenciasTotales;
	}

	/***************************************************************************************
	 * Metodo de busqueda Knuth-Morris-Prat (Diapositivas)
	 * @param patron a localizar
	 * @param texto en el que realizar la busqueda
	 * @param fallo matriz de fallos
	 * @return lista con las posiciones de las ocurrencias
	 ****************************************************************************************/
	public static ArrayList<Integer> KnuthMorrisPrat(String patron, String texto, int[] fallo) {
		ArrayList<Integer> pos = new ArrayList<Integer>();
		int t = 0;
		int p = 0;
		while (texto.length() - t >= patron.length()) {
			if (p == patron.length()) {
				pos.add(t+1); //Añadimos uno por consistencia con editores de texto
				p = 0;
				t++;
			} else {
				if (texto.charAt(t + p) == patron.charAt(p))
					p++;
				else {
					t = t + p - fallo[p];
					if (p > 0)
						p = fallo[p];
				}
			}
		}
		return pos;
	}

	/************************************************************************
	 * Metodo utilizado para realizar el preproceso del patron (Diapositivas)
	 * @param patron a preprocesar
	 * @return matriz de fallos
	 ***********************************************************************/
	public static int[] preproceso(String patron) {
		int[] fallo = new int[patron.length()];
		int i = 2;
		int j = 0;
		fallo[0] = -1;
		if (patron.length() > 1) {
			fallo[1] = 0;
			while (i < patron.length()) {
				if (patron.charAt(i - 1) == patron.charAt(j)) {
					j++;
					fallo[i] = j;
					i++;
				} else {
					if (j > 0)
						j = fallo[j];
					else {
						fallo[i] = 0;
						i++;
					}
				}
			}
		}
		return fallo;
	}

	//-------------------   Karp-Rabin ---------------------------------------------//
	
	/********************************************************************************************
	 * Metodo utilizado para buscar coincidencias, invocando al metodo que lo hace linea a linea
	 * Version de las transparencias modificada
	 * @param patron  a localizar en el texto
	 * @param reader, objeto en java utilizado para leer el texto
	 * @param leerCada, integer que indica en que lineas debemos leer
	 * @return ArrayList que contiene informacion sobre ocurrencias encontradas y en que linea
	 * @throws IOException
	 ******************************************************************************************/
	public static ArrayList<LineaOcurrencia> KarpRabin(String patron, BufferedReader reader, int leerCada) throws IOException {
		ArrayList<LineaOcurrencia> ocurrenciasTotales = new ArrayList<LineaOcurrencia>();
		String linea = "";
		int numeroLinea = 0;
		
		while ((linea = reader.readLine()) != null) {
			// Comprobar si debemos procesar esta linea
			if (numeroLinea % leerCada == 0) {
				ArrayList<Integer> ocurrencias = new ArrayList<Integer>();
				if (patron.length() > 0 && linea.length() >= patron.length()) {
					KarpRabin(patron, linea, ocurrencias);
					LineaOcurrencia actual = new LineaOcurrencia(numeroLinea+1, ocurrencias);
					ocurrenciasTotales.add(actual);
				}
			}
			numeroLinea++;
		}
		return ocurrenciasTotales;
	}

	/****************************************************************************************
	 * Metodo de busqueda KarpRabin (Diapositivas)
	 * @param patron a localizar
	 * @param texto en el que realizar la busqueda
	 * @param ocurrencias, ArrayList con las posiciones numericas
	 *****************************************************************************************/
	private static void KarpRabin(String patron, String texto, ArrayList<Integer> ocurrencias) {
		int m = patron.length();
		for (int n = 0; n <= texto.length() - m; n++) {
			String aux = texto.substring(n, n + m);
			if (aux.hashCode() == patron.hashCode() && aux.equals(patron))
				ocurrencias.add(n+1); //Añadimos uno por consistencia con editores de texto
		}
	}

}
