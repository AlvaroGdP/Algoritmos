package dominio;

public class Probabilista {
	
	public static void main(String[] args) {
		int n = 100000000;
		Parabola p = new Parabola(0,1,0,2);
		long startTime = System.nanoTime();
		double volumen = p.volumenNumericoP(n);
		long executionTime = System.nanoTime() - startTime;
		System.out.println("Volumen Numerico P ="+volumen+" Intervalo de confianza ["+p.intervaloInfP()+" , "+p.intervaloSupP()+"]");
		System.out.println("El tiempo de ejecución con "+n+" puntos es: "+executionTime+" ns\n");
		
		//Esfera de radio 1
		Esfera f = new Esfera(0,1,1);
		startTime = System.nanoTime();
		volumen = f.volumenNumericoP(n);
		executionTime = System.nanoTime() - startTime;
		System.out.println("Volumen Numerico P ="+volumen+" Intervalo de confianza ["+f.intervaloInfP()+" , "+f.intervaloSupP()+"]");
		System.out.println("El volumen real de la esfera es: "+f.volumen());
		System.out.println("El tiempo de ejecución con "+n+" puntos es: "+executionTime+" ns");
	}

}
