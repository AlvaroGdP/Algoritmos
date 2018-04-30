package dominio;

import java.util.ArrayList;

public class LineaOcurrencia {

	private int linea;
	private ArrayList<Integer> ocurrencias;
	
	public LineaOcurrencia(int linea, ArrayList<Integer> ocurrencias) {
		this.linea=linea;
		this.ocurrencias = ocurrencias;
	}

	public int getLinea() {
		return linea;
	}

	public void setLinea(int linea) {
		this.linea = linea;
	}

	public ArrayList<Integer> getOcurrencias() {
		return ocurrencias;
	}

	public void setOcurrencias(ArrayList<Integer> ocurrencias) {
		this.ocurrencias = ocurrencias;
	}
	
	public String toString() {
		String cadenaPosicion = "; Posición: ";
		for (int pos: this.ocurrencias) {
			cadenaPosicion += pos+", ";
		}
		cadenaPosicion = cadenaPosicion.substring(0, cadenaPosicion.length()-2);
		return "Línea "+this.linea+cadenaPosicion;
	}
}
