package dominio;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

public class Nodo {

	private LinkedHashMap<Integer, Integer> divisoresRestantes;
	private LinkedList<Nodo> sucesores;
	private Nodo sucesorGanador;
	private int numero;
	private boolean visitado;

	public Nodo(LinkedHashMap<Integer, Integer> divisoresRestantes, int numero) {
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

	public LinkedHashMap<Integer, Integer> getDivisoresRestantes() {
		return divisoresRestantes;
	}

	public void setDivisoresRestantes(LinkedHashMap<Integer, Integer> divisoresRestantes) {
		this.divisoresRestantes = divisoresRestantes;
	}

	public Nodo getSucesorGanador() {
		return sucesorGanador;
	}
	
	public void setSucesorGanador(Nodo predecesor) {
		this.sucesorGanador = predecesor;
	}
	
	public String divisoresToString() {
		String auxDivisores = "Divisores restantes: ";
		Set<Integer> keySet = this.getDivisoresRestantes().keySet();
		Iterator<Integer> keys = keySet.iterator();
		while (keys.hasNext()) {
			int divisorActual = keys.next();
			for (int i=1; i<=this.divisoresRestantes.get(divisorActual); i++) {
				auxDivisores +=  (int) Math.pow(divisorActual, i)+" ";
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
