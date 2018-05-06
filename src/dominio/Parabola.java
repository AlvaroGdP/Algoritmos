package dominio;

public class Parabola extends Funciones {

	private double min;
	private double max;
	
	public Parabola(double inf, double sup, double min, double max) {
		super(inf, sup);
		this.min = min;
		this.max = max;
	}

	public double f(double x, double y) {
		// z=x^2+y^2
		double z = 0;
		z = Math.pow(x, 2) + Math.pow(y, 2);
		return z;
	}

	@Override
	public double volumenNumericoP(int k) {
		int buenos = 0;
		for (int i = 0; i < k; i++) {
			double x = randomWithRange(this.linf, this.lsup);
			double y = randomWithRange(this.linf, this.lsup);
			double z = randomWithRange(this.min, this.max);
			if (z >= f(x, y)) {
				buenos++;
			}
		}
		double volumen = (this.lsup*this.lsup*(this.max-this.min)) * ((double) buenos / k);
		IntervaloConfProporciones(volumen, k);
		return volumen;
	}

	@Override
	public double volumen() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*public double intervaloInfP() {
		return 2.0 * super.intervaloInfP();
	}

	public double intervaloSupP() {
		return 2.0 * super.intervaloSupP();
	}*/

}
