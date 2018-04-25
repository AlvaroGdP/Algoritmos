package dominio;

import java.util.LinkedList;

public class Nodo {

	private LinkedList<NumeroPotencia> divisoresRestantes;
	private LinkedList<Nodo> sucesores;
	private Nodo sucesorGanador;
	private int numero;
	private boolean visitado;

	public Nodo(LinkedList<NumeroPotencia> divisoresRestantes, int numero) {
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

	public LinkedList<NumeroPotencia> getDivisoresRestantes() {
		return divisoresRestantes;
	}

	public void setDivisoresRestantes(LinkedList<NumeroPotencia> divisoresRestantes) {
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
		for (int i=0; i < this.divisoresRestantes.size(); i++) {
			for (int j=1; j <= this.divisoresRestantes.get(i).getExponente(); j++) {
				auxDivisores += (int) Math.pow(this.divisoresRestantes.get(i).getBase(), j) + " ";
			}
		}
		return auxDivisores;
	}
	
	public String numeroActualToString(int numeroMax, String forwBack) {
		return "El número actual es: "+this.numero;
	}

	public LinkedList<NumeroPotencia> cloneDivisores() {
		LinkedList <NumeroPotencia> copia = new LinkedList<NumeroPotencia>();
		for (NumeroPotencia numeroActual: this.divisoresRestantes) {
			copia.addLast(numeroActual.clone());
		}
		return copia;
	}
	
}
