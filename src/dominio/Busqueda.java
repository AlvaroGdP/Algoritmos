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

	public static void ejecucionPrincipal(String patron, String metodo, int leerCada) throws IOException {
		BufferedReader reader = null;
		String linea;
		int numeroLinea = 0;
		int numeroOcurrencias = 0;
		ArrayList<LineaOcurrencia> ocurrenciasEncontradas = new ArrayList<LineaOcurrencia>();

		//Hardcode
		String path = "quijote1.txt";
		
		patron = patron.toLowerCase();
		File texto = new File(path);
		System.out.println("Abriendo archivo "+path);
		try {
			// Necesario para poder abrir el archivo con formato correcto
			// Evitar problemas por caracteres especiales
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(texto), "Cp1252"));
		} catch (FileNotFoundException e) {
			System.err.println("Error al intentar leer el archivo "+path);
			System.exit(0);
		}
		System.out.println("Recorriendo archivo...");
		while ((linea = reader.readLine())!=null) {
			if (numeroLinea%leerCada == 0) {
				//Convertir todo a minusculas
				linea = linea.toLowerCase();
				if (metodo.equals("Knuth-Morris-Prat")) {
					ArrayList<Integer> ocurrencias = KnuthMorrisPrat(patron, linea);
					//Añadimos uno para compensar la diferencia con editor de texto
					LineaOcurrencia ocurrenciasActuales = new LineaOcurrencia(numeroLinea + 1, ocurrencias);
					ocurrenciasEncontradas.add(ocurrenciasActuales);
				}else {
				}
			}
			numeroLinea++;
		}
		reader.close();
		System.out.println("Archivo recorrido correctamente.");
		System.out.println("\n***** Mostrando resultados *****\n");
		System.out.println("Patrón encontrado en: ");
		numeroOcurrencias = mostrarResultados(ocurrenciasEncontradas);
		System.out.println("Ocurrencias estimadas: "+leerCada * numeroOcurrencias);
	}

	private static int mostrarResultados(ArrayList<LineaOcurrencia> ocurrenciasTotales) {
		int total = 0;
		for (int i=0; i<ocurrenciasTotales.size(); i++) {
			//Si hemos encontrado coincidencias
			if (ocurrenciasTotales.get(i).getOcurrencias().size()!=0) {
				System.out.println(ocurrenciasTotales.get(i).toString());
				total += ocurrenciasTotales.get(i).getOcurrencias().size();
			}
		}
		System.out.println("\nOcurrencias totales: "+total);
		return total;
	}
	
	public static ArrayList<Integer> KnuthMorrisPrat(String patron,String texto){
		ArrayList<Integer> ocurrencias=new ArrayList<Integer>();
		if(patron.length()>0 && texto.length()>=patron.length()){
			int[] fallo=new int[patron.length()];
			fallo=preproceso(patron);//matriz de fallos
			ocurrencias=KnuthMorrisPrat(patron,texto,fallo);
		}
		return ocurrencias;
	}
	
	public static ArrayList<Integer> KnuthMorrisPrat(String patron, String texto, int[] fallo) {
		ArrayList<Integer> pos = new ArrayList<Integer>();
		int t = 0;
		int p = 0;
		while (texto.length() - t >= patron.length()) {
			if (p == patron.length()) {
				pos.add(t);
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
	
}
