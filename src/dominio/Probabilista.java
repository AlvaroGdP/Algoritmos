package dominio;

public class Probabilista {

	private static double[] intervaloP = new double[2];
	
	public static void main(String[] args) {
		int n = 100000000, counter = 0;
		for (int i = 0; i < n; i++) {
			double x = randomWithRange(0, 1);
			double y = randomWithRange(0, 1);
			double z = randomWithRange(0, 2);
			if (z >= f(x, y)) {
				counter++;
			}
		}
		double volumen = 2.0*((double)counter/n);
		IntervaloConfProporciones(2.0*((double)counter/n),n);
		System.out.println("Volumen Numerico P ="+volumen+" Intervalo de confianza ["+intervaloP[0]+" , "+intervaloP[1]+"]");
	}

	public static double f(double x, double y) {
		double z = 0;
		z = Math.pow(x, 2) + Math.pow(y, 2);
		return z;
	}

	public static int randomWithRange(int min, int max) {
		int range = (max - min) + 1;
		return (int) (Math.random() * range) + min;
	}

	public static void IntervaloConfProporciones(double p, int n) {
		intervaloP[0] = p - 1.96 * Math.sqrt(Math.abs(p * (1 - p) / n));
		intervaloP[1] = p + 1.96 * Math.sqrt(Math.abs(p * (1 - p) / n));
	}

}
