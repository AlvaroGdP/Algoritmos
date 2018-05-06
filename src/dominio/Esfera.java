package dominio;

public class Esfera extends Funciones{

	private double radio;
	
	public Esfera(double inf, double sup, double r) {
		super(inf, sup);
		this.radio = r;
	}

	@Override
	public double f(double x, double y) {
		//x2 + y2 + z2 = r2
		double z = Math.sqrt(Math.pow(radio, 2) - Math.pow(x, 2) - Math.pow(y, 2));
		return z;
	}

	@Override
	public double volumenNumericoP(int k) {
		int buenos = 0;
		for (int i = 0; i < k; i++) {
			double x = randomWithRange((-1)*radio, radio);
			double y = randomWithRange((-1)*radio, radio);
			//Calculamos la proporcion de la semi esfera
			double z = randomWithRange(0, radio);
			if (z <= f(x, y)) {
				buenos++;
			}
		}
		double volumen = (Math.pow(radio*2, 3)) * ((double) buenos / k);
		IntervaloConfProporciones(volumen, k);
		return volumen;
	}

	@Override
	public double volumen() {
		double volumen = (4*Math.PI*Math.pow(radio, 3))/3.0;
		return volumen;
	}

}
