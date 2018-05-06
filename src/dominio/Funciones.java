package dominio;

public abstract class Funciones {
	double linf, lsup;// [linf,lsup]
	double[] intervalo;// para los intevalos de confianza para normales
	double[] intervaloP;// para los intevalos de confianza para proporciones

	public Funciones(double inf, double sup) {
		if (inf > sup) {
			double x = inf;
			inf = sup;
			sup = x;
		}
		linf = inf;
		lsup = sup;
		intervalo = new double[2];// para los intevalos de confianza para normales
		intervaloP = new double[2];// para los intevalos de confianza para proporciones
	}

	public double linf() {
		return linf;
	}

	public double lsup() {
		return lsup;
	}

	public void set_linf(double inf) {
		linf = inf;
	}

	public void set_lsup(double sup) {
		lsup = sup;
	}

	public double amplitud() {
		return lsup() - linf();
	}

	public boolean vale(double x) {
		boolean v = x >= linf() && x <= lsup();
		if (!v)
			System.out.println(x + " no pertenece a " + this);
		return v;
	}

	public abstract double f(double x,double y);

	public abstract double volumenNumericoP(int k);// area probabilista lanzando k puntos A=casos favorables/casos posibles

	public abstract double volumen();// area bien calculada por el metodo que conozcamos

	public String toString() {
		return "Intervalo: [" + linf() + " , " + lsup() + "]";
	}

	public void intervaloConfianza(double[] valores) {// valores.length tamaño de la muestra
		double media = media(valores);
		double S = cuasiV(valores, media);
		intervalo[0] = media - 1.96 * S / Math.sqrt(valores.length);
		intervalo[1] = media + 1.96 * S / Math.sqrt(valores.length);
	}

	public double intervaloInf() {
		return intervalo[0];
	}

	public double intervaloSup() {
		return intervalo[1];
	}

	public double media(double valores[]) {
		double media = 0;
		for (int x = 0; x < valores.length; x++) {
			media = media + valores[x];
		}
		return media / valores.length;
	}

	public double cuasiV(double[] valores, double media) {
		double S = 0;
		for (int x = 0; x < valores.length; x++) {
			S = S + Math.pow(valores[x] - media, 2);
		}
		return Math.sqrt(S / (valores.length - 1));
	}

	public void IntervaloConfProporciones(double p, int n) {
		intervaloP[0] = p - 1.96 * Math.sqrt(Math.abs(p * (1 - p) / n));
		intervaloP[1] = p + 1.96 * Math.sqrt(Math.abs(p * (1 - p) / n));
	}

	public double intervaloInfP() {
		return intervaloP[0];
	}

	public double intervaloSupP() {
		return intervaloP[1];
	}
	
	public static double randomWithRange(double min, double max) {
		double range = (max - min);
		return (Math.random() * range) + min;
	}
	
}