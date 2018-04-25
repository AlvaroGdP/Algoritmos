package dominio;

public class NumeroPotencia implements Cloneable{

	private int base;
	private int exponente;
	
	public NumeroPotencia (int base, int exponente) {
		this.base = base;
		this.exponente = exponente;
	}

	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}

	public int getExponente() {
		return exponente;
	}

	public void setExponente(int exponente) {
		this.exponente = exponente;
	}
	
	@Override
	public NumeroPotencia clone() {
		return new NumeroPotencia(this.base, this.exponente);
	}
}
