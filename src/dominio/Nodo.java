package dominio;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

public class Nodo {

	private LinkedList<Integer> divisoresRestantes;
	private LinkedList<Nodo> sucesores;
	private Nodo sucesorGanador;
	private int numero;
	private boolean visitado;

	public Nodo(LinkedList<Integer> divisoresRestantes, int numero) {
		this.divisoresRestantes=divisoresRestantes;
		this.sucesores = new LinkedList<Nodo>();
		this.sucesorGanador=null;
		this.numero = numero;
		this.visitado = false;
	}
		
	public boolean getVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}
	
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public void addSucesor(Nodo sucesor) {
		sucesores.add(sucesor);
	}
	
	public LinkedList<Nodo> getSucesores() {
		return sucesores;
	}

	public void setSucesores(LinkedList<Nodo> sucesores) {
		this.sucesores = sucesores;
	}

	public LinkedList<Integer> getDivisoresRestantes() {
		return divisoresRestantes;
	}

	public void setDivisoresRestantes(LinkedList<Integer> divisoresRestantes) {
		this.divisoresRestantes = divisoresRestantes;
	}
	
	public Nodo getSucesorGanador() {
		return sucesorGanador;
	}

	public void setSucesorGanador(Nodo sucesorGanador) {
		this.sucesorGanador = sucesorGanador;
	}

	public String divisoresToString() {
		String auxDivisores = "Divisores restantes: ";
		int divisorActual = 0;
		int firstOccurence = -1;
		int lastOccurence = -1;
		for (int i=0; i < this.divisoresRestantes.size(); i++) {
			divisorActual = this.divisoresRestantes.get(i);
			firstOccurence = i;
			lastOccurence = this.divisoresRestantes.lastIndexOf(divisorActual);
			i = lastOccurence ; //Dado que están ordenados, podemos avanzar hasta la siguiente posicion
			for (int j=1; j <= (lastOccurence - firstOccurence + 1); j++) {
				auxDivisores += (int) Math.pow(divisorActual, j) + " ";
			}
		}
		return auxDivisores;
	}
	
	public String numeroActualToString(int numeroMax, String forwBack) {
		if (forwBack.equals("Forward")) {
			return "El número actual es: "+numeroMax/this.numero;
		}else {
			return "El número actual es: "+this.numero;
		}
	}
	
}
