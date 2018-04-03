package practica1;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

public class NumeroCombinatorio {

	public static Scanner reader = new Scanner(System.in);
	
	public static void main(String[] args) {
		int option=0;
		long sol = 1;
		int numeros [] = new int[2];
		long startTime = 0, executionTime = 0;
				
		while(true){
			System.out.println("Por favor, elige una opción: ");
			System.out.println("1- Modo Iterativo\n2- Modo Recursivo\n3- Modo Recursivo con Pilas");
			System.out.println("4- Exit");
			try{
				option=reader.nextInt();
				reader.nextLine();
			}catch(InputMismatchException ime){
				System.out.println("Introduce un número entero, por favor..");
			}
			switch (option){
			case 1:
				pedirInteger(numeros);
				System.out.println("Ejecución en curso. Por favor espere mientras el programa busca la solución.");
				startTime = System.nanoTime();
				sol = iterativo(numeros[0], numeros[1]);
				executionTime = System.nanoTime() - startTime;
				System.out.println("El número combinatorio de "+numeros[0]+" "+numeros[1]+" es "+sol);
				System.out.println("Tiempo de ejecución: "+executionTime+" ns.\n");
				option=-1;
				break;
			case 2:
				pedirInteger(numeros);
				System.out.println("Ejecución en curso. Por favor espere mientras el programa busca la solución.");
				startTime = System.nanoTime();
				sol = recursivo(numeros[0], numeros[1]);
				executionTime = System.nanoTime() - startTime;
				System.out.println("El número combinatorio de "+numeros[0]+" "+numeros[1]+" es "+sol);
				System.out.println("Tiempo de ejecución: "+executionTime+" ns.\n");
				option=-1;
				break;
			case 3:
				pedirInteger(numeros);
				System.out.println("Ejecución en curso. Por favor espere mientras el programa busca la solución.");
				startTime = System.nanoTime();
				sol = pilas(numeros[0], numeros[1]);
				executionTime = System.nanoTime() - startTime;
				System.out.println("El número combinatorio de "+numeros[0]+" "+numeros[1]+" es "+sol);
				System.out.println("Tiempo de ejecución: "+executionTime+" ns.\n");
				option=-1;
				break;
			case 4:
				System.exit(0);
				break;
			default:
				System.out.println("Por favor, introduce 1, 2, 3 o 4.\n");
				option=-1;
				reader.nextLine();
			}	
		}
	}
	
	private static void pedirInteger(int numeros[]) {
		int numero = -1;
		while(numero <0) {
			System.out.println("Introduce el valor de n");
			try{
				numero=reader.nextInt();
				reader.nextLine();
			}catch(InputMismatchException ime){
				System.out.println("Introduce un número entero, por favor.");
			}
		}
		numeros[0] = numero;
		numero = -1;
		while(numero <0 || numero > numeros[0]) {
			System.out.println("Introduce el valor de k");
			try{
				numero=reader.nextInt();
				reader.nextLine();
			}catch(InputMismatchException ime){
				System.out.println("Introduce un número entero, por favor.");
			}
		}
		numeros[1] = numero;
	}

	// C(n,k)=n!/(k!*(n-k)!)
	public static long iterativo(int n, int k) {
		long sol = 1;
		long factN = 1, factK = 1, factNK = 1;
		for (long i = 1; i <= n; i++) {
			if (i <= k) {
				factK *= i;
			}
			if(i<=(n-k)) {
				factNK *= i;
			}
			factN *=i;
		}
		sol = factN/(factK*factNK);
		return sol;
	}

	public static long recursivo(int n, int k) {
		long sol = 1;
		if(k == 0 || n==k) {
			return sol;
		}else {
			sol = recursivo(n-1,k-1) + recursivo(n-1,k);
			return sol;
		}
	}

public static int pilas(int n, int k) {
		
		int sol = -1;
		
		Stack<Integer> nStack = new Stack<Integer>();
		Stack<Integer> kStack = new Stack<Integer>();
		Stack<Integer> solStack = new Stack<Integer>();
		Stack<Integer> callStack = new Stack<Integer>();
		
		nStack.push(n);
		kStack.push(k);
		solStack.push(0);
		callStack.push(1);
		
		while (nStack.size() > 0) {
			
			while (kStack.peek() != 0 && nStack.peek()!=kStack.peek() && callStack.peek() <= 2) {
				
				switch(callStack.peek()) {
				case 1:
					nStack.push(nStack.peek()-1);
					kStack.push(kStack.peek()-1);
					break;
				case 2:
					nStack.push(nStack.peek()-1);
					kStack.push(kStack.peek());
					break;
				}
				
				callStack.push(1);
				if (kStack.peek() == 0 || nStack.peek()==kStack.peek()) {
					solStack.push(1);
				}else {
					solStack.push(0);
				}
				
			}
			
			nStack.pop();
			kStack.pop();
			callStack.pop();
			sol = solStack.pop();
			
			if (!nStack.empty()) {
				callStack.push(callStack.pop()+1);
				solStack.push(sol + solStack.pop());
			}
						
		}
		return sol;
	}
}
